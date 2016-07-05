package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.sharksystem.sharknet.api.Message;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Benni on 01.07.2016.
 */
public class NewMessageController extends AbstractController {

	@FXML
	private ImageView imageViewClose;
	@FXML
	private Label labelHeader;
	@FXML
	private Label labelTime;
	@FXML
	private Label labelMessage;

	private Stage stage;
	private Message message;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm");
	private static final int KEYFRAMEDURATION = 3000;
	private static final int KEYFRAMEDELAY = 5000;
	private static final int WINDOWWIDTH = 300;
	private static final int WINDOWHEIGHT = 150;

	public NewMessageController(Message m) {
		super(App.class.getResource("views/chat/newMessageView.fxml"));
		message = m;
		Parent root = super.getRoot();
		stage = new Stage();
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setTitle("Create your Profile");
		Scene scene = new Scene(root, WINDOWWIDTH, WINDOWHEIGHT);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		// get monitor settings
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		// get effective screen size (subtract windows taskbar)
		Rectangle bounds = gc.getBounds();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		Rectangle effectiveScreenArea = new Rectangle();
		effectiveScreenArea.x = bounds.x + screenInsets.left;
		effectiveScreenArea.y = bounds.y + screenInsets.top;
		effectiveScreenArea.height = bounds.height - screenInsets.top - screenInsets.bottom;
		effectiveScreenArea.width = bounds.width - screenInsets.left - screenInsets.right;
		// place window in the right lower corner
		stage.setX(effectiveScreenArea.width - WINDOWWIDTH);
		stage.setY(effectiveScreenArea.height - WINDOWHEIGHT);
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();
		// delay keyframe start
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// fade out and close window
				Timeline timeline = new Timeline();
				KeyFrame key = new KeyFrame(Duration.millis(KEYFRAMEDURATION),
					new KeyValue(root.opacityProperty(), 0));
				timeline.getKeyFrames().add(key);
				timeline.setOnFinished((ae) -> stage.close());
				timeline.play();

			}
		}, KEYFRAMEDELAY);

	}

	@Override
	protected void onFxmlLoaded() {
		imageViewClose.setOnMouseClicked(event -> {
			stage.close();
			event.consume();
		});
		imageViewClose.setPickOnBounds(true);

		loadData();
	}

	private void loadData() {
		labelMessage.setText("<" + message.getSender().getNickname() + "> " + message.getContent().getMessage());
		labelHeader.setText("New incoming Message!");
		java.sql.Timestamp timestamp = message.getTimestamp();
		labelTime.setText(dateFormat.format(timestamp));
	}


}
