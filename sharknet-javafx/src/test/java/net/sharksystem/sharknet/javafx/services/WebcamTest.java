package net.sharksystem.sharknet.javafx.services;


import com.github.sarxos.webcam.*;
import com.github.sarxos.webcam.ds.dummy.WebcamDummyDriver;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import net.sharksystem.sharknet.javafx.App;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import javax.naming.OperationNotSupportedException;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;

/**
 * Created by fxdapokalypse on 07.06.2016.
 */
public class WebcamTest extends Application {


	private StackPane capturePane;
	private ImageView capturedImageView;
	private ComboBox<Webcam> availableWebCamBox;
 	private Button captureButton;
	private Button stopCaptureButton;
	private Task<ObservableList<Webcam>> cameraDiscoveryTask;
	private Task<Image> captureImageTask;
	private Webcam currentCamera;
	private ExecutorService threadPool = Executors.newFixedThreadPool(3);
	private WebcamPanel panel;

	public static void main(String[] args)  {
		enableLogging();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Scene scene = createScene(primaryStage);
		primaryStage.setOnCloseRequest(this::onClose);
		primaryStage.setScene(scene);
		primaryStage.setWidth(1024);
		primaryStage.setHeight(768);
		primaryStage.show();
		startCameraDiscovery();
	}

	private Scene createScene(Stage primaryStage) {
		capturePane = new StackPane();
		capturePane.setStyle("-fx-background-color: #212121;");
		capturedImageView = new ImageView();
		capturedImageView.setSmooth(true);
		capturedImageView.setPreserveRatio(true);
		capturedImageView.fitHeightProperty().bind(primaryStage.heightProperty().multiply(0.8));
		capturedImageView.fitWidthProperty().bind(capturedImageView.fitHeightProperty().multiply(16/9));

		StackPane.setAlignment(capturedImageView, Pos.CENTER);
		capturePane.getChildren().add(capturedImageView);
		capturePane.maxWidthProperty().bind(capturedImageView.fitWidthProperty());
		capturePane.maxHeightProperty().bind(capturedImageView.fitHeightProperty());

		captureButton = new Button("Capture Image");
		captureButton.setOnAction((e) -> captureImage());
		captureButton.setDisable(true);

		Button photo = new Button("Photo");
		photo.setDisable(false);

		stopCaptureButton = new Button("Stop");
		stopCaptureButton.setDisable(true);
		stopCaptureButton.setOnAction((e) -> stopCapture());

		availableWebCamBox = new ComboBox<>();
		availableWebCamBox.setPlaceholder(new Text("No webcams found"));
		availableWebCamBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			currentCamera = newValue;
			captureButton.setDisable(newValue == null);
		});

		availableWebCamBox.setConverter(new StringConverter<Webcam>() {
			@Override
			public String toString(Webcam object) {
				return object.getName();
			}

			@Override
			public Webcam fromString(String string) {
				throw new IllegalArgumentException("not supported");
			}
		});
		availableWebCamBox.setPrefWidth(100);

		FlowPane controls = new FlowPane(Orientation.HORIZONTAL, 10.0, 10.0);
		controls.setAlignment(Pos.CENTER);
		controls.getChildren().addAll(availableWebCamBox, captureButton, stopCaptureButton);
		controls.getChildren().add(photo);

		BorderPane root = new BorderPane();
		//root.setCenter(capturePane);
		root.setTop(controls);

		photo.setOnAction((e) -> {
			Task<Image> photoTask =	new Task<Image>() {
				@Override
				protected Image call() throws Exception {
					if (currentCamera != null) {
						if (!currentCamera.isOpen()) currentCamera.open();
						BufferedImage bufferdimage = currentCamera.getImage();
						System.out.println(bufferdimage);
						Image image = SwingFXUtils.toFXImage(bufferdimage, null);
						return image;
					}
					return null;
				}
			};
			photoTask.onSucceededProperty().addListener((observable, oldValue, newValue) -> {
				System.out.println("yeah");
				FileChooser fileChooser = new FileChooser();
				File file = fileChooser.showOpenDialog(primaryStage);
				BufferedImage image = null;
				if (file != null)
				try {
					image = SwingFXUtils.fromFXImage(photoTask.getValue(), null);
					ImageIO.write(image, "png", new FileOutputStream(file));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

            });
			threadPool.submit(photoTask);

		});
		Webcam.getDefault().getLock().unlock();
		panel = new WebcamPanel(Webcam.getDefault(), false);
		SwingNode swingNode = new SwingNode();
		SwingUtilities.invokeLater(() -> {
			swingNode.setContent(panel);
		});
		panel.setDisplayDebugInfo(true);

		root.setCenter(swingNode);
		panel.start();



		return new Scene(root);
	}

	private void stopCapture() {
		if (captureImageTask != null && captureImageTask.isRunning()) {
            captureImageTask.cancel();
        }
	}

	void startCameraDiscovery() {
		if (cameraDiscoveryTask == null) {
			cameraDiscoveryTask = new Task<ObservableList<Webcam>>() {
				@Override
				protected ObservableList<Webcam> call() throws Exception {
					updateMessage("Retrieve connected web cameras");
					ObservableList<Webcam> webcamList = FXCollections.observableArrayList();
					// Webcam.setDriver(new WebcamDummyDriver(3));
					for (Webcam cam : Webcam.getWebcams()) {
						if (isCancelled()) break;
						webcamList.add(cam);
					}
					updateMessage(String.format("Found %d Webcams\n", webcamList.size()));
					return webcamList;
				}

			};

			cameraDiscoveryTask.messageProperty().addListener((observable, oldValue, newValue) -> {
				System.out.println(newValue);
			});
			cameraDiscoveryTask.setOnSucceeded((e) -> {
				try {
					availableWebCamBox.setItems(cameraDiscoveryTask.get());
				} catch (InterruptedException | ExecutionException ex) {
					ex.printStackTrace();
				}
			});
		}
		System.out.println(threadPool.isShutdown());
		threadPool.submit(cameraDiscoveryTask);
	}

	private void onClose(WindowEvent windowEvent) {
		if (captureImageTask != null) captureImageTask.cancel();
		if (cameraDiscoveryTask != null) cameraDiscoveryTask.cancel();
	}

	private void captureImage() {
		final Webcam cam = availableWebCamBox.getValue();
		if (cameraDiscoveryTask != null) cameraDiscoveryTask.cancel();
		captureImageTask = new Task<Image>() {
			@Override
			protected Image call() throws Exception {
				Image image = null;
				WebcamLock lock = cam.getLock();
				if (lock.isLocked()) {
					lock.unlock();
				}
				lock.lock();

				if (cam.open()) {
					while (!isCancelled()  && cam.isOpen() && cam.isImageNew()) {
						BufferedImage bufferdimage = cam.getImage();
						image = null;
						image = SwingFXUtils.toFXImage(bufferdimage, null);
						if (image == null) {
							updateMessage("Created snapshot");
						} else {
							updateMessage("Failed to create snapshot");
						}
						updateValue(image);
					}
					updateMessage("Stopped capturing");
					cam.close();
					synchronized (lock) {
						lock.unlock();
					}
				} else {
					updateMessage("Camera is already inuse");
				}
				return image;
			}
		};

		EventHandler<WorkerStateEvent> onEnd = (e) -> {
			availableWebCamBox.setDisable(false);
			captureButton.setDisable(false);
			stopCaptureButton.setDisable(true);
		};

		captureImageTask.exceptionProperty().addListener((observable, oldValue, newValue) -> {
			newValue.printStackTrace();
		});
		captureImageTask.setOnSucceeded(onEnd);
		captureImageTask.setOnCancelled(onEnd);
		captureImageTask.setOnFailed(onEnd);
		captureImageTask.messageProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println(newValue);
		});
		captureImageTask.valueProperty().addListener((observable, oldValue, newValue) -> {
			capturedImageView.setImage(newValue);
		});
		availableWebCamBox.setDisable(true);
		captureButton.setDisable(true);
		stopCaptureButton.setDisable(false);
		threadPool.submit(captureImageTask);


	}

	private static void enableLogging() {
		try {
			LogManager.getLogManager().readConfiguration(
				App.class.getResource("logging.properties").openStream()
			);
		} catch (IOException ex) {
			// When no logging.properties is provided, no logging is required.
		}
	}
}
