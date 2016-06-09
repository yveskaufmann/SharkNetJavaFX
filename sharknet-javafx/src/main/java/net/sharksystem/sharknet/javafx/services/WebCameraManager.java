package net.sharksystem.sharknet.javafx.services;

import com.github.sarxos.webcam.Webcam;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

public class WebCameraManager  {

	private static final Logger Log = LoggerFactory.getLogger(WebCameraManager.class);

	private Task<ObservableList<Webcam>> createCameraDiscoveryTask() {
		return new Task<ObservableList<Webcam>>() {
			@Override
			protected ObservableList<Webcam> call() throws Exception {
				Log.info("Retrieve connected web cameras");
				ObservableList<Webcam> webcamList = FXCollections.observableArrayList();
				for (Webcam cam : Webcam.getWebcams()) {
					if (isCancelled()) break;
					webcamList.add(cam);
				}
				updateMessage(String.format("Found %d Webcams\n", webcamList.size()));
				return webcamList;
			}
		};
	};

	private Task<Image> createImageStreamTask(final Webcam camera) {
		return  new Task<Image>() {
			@Override
			protected Image call() throws Exception {
				Image image = null;
				if (camera.open()) {
					while (!isCancelled()  && camera.isOpen()) {
						if (! camera.isImageNew()) continue;
						BufferedImage bufferedImage = camera.getImage();
						image = null;
						image = SwingFXUtils.toFXImage(bufferedImage, null);
						updateValue(image);
					}
					camera.close();
				}
				return image;
			}
		};
	}
}
