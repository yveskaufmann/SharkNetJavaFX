package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Benni on 06.06.2016.
 */
public class EmojiController extends AbstractController {

	private @FXML
	GridPane gridPaneEmojis;

	private Emoji emoji;
	private List<String> emojis;
	private ChatListener listener;

	public EmojiController() {
		super(App.class.getResource("views/chat/emojiView.fxml"));

		listener = null;

		Parent root = super.getRoot();
		Stage stage = new Stage();
		stage.setScene(new Scene(root, 494, 414));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();

		emoji = new Emoji();
		emoji.loadEmojis();
		emojis = emoji.getEmojis();

		int counter = 0;
		for (int i = 0; i < Emoji.cols; i++) {
			for (int j = 0; j < Emoji.rows; j++) {
				if (counter < emojis.size()) {
					Pane pane = new Pane();
					pane.getStyleClass().addAll("emojione", emojis.get(counter));
					System.out.println(emojis.get(counter));
					pane.setPrefHeight(64);
					pane.setPrefWidth(64);
					pane.setOnMouseClicked(event -> {
						onEmojiClicked(pane);
						event.consume();
					});
					gridPaneEmojis.add(pane, i, j);
					counter += 1;
				}
			}
		}
	}

	@Override
	protected void onFxmlLoaded() {

	}

	private void onEmojiClicked(Pane p) {
		if (listener != null) {
			listener.onEmojiChoose(p.getStyleClass().get(1));
		}
	}

	public void setListener(ChatListener listener) {
		this.listener = listener;
	}
}
