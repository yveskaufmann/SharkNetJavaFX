package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benni on 06.06.2016.
 */
public class EmojiController extends AbstractController {

	private @FXML
	GridPane gridPaneEmojis;

	private Emoji emoji;
	private List<String> emojis;
	private List<ChatListener> listeners;

	public EmojiController() {
		super(App.class.getResource("views/chat/emojiView.fxml"));

		listeners = new ArrayList<>();

		Parent root = super.getRoot();
		Stage stage = new Stage();
		stage.setScene(new Scene(root, 494, 414));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();

		emoji = Emoji.getInstance();
		emojis = emoji.getEmojis();

		int counter = 0;
		// loading emojis from .png using css
		for (int i = 0; i < Emoji.cols; i++) {
			for (int j = 0; j < Emoji.rows; j++) {
				if (counter < emojis.size()) {
					Pane pane = new Pane();
					pane.getStyleClass().addAll("emojione", emojis.get(counter));
					//pane.setPrefHeight(64);
					//pane.setPrefWidth(64);
					// set onmouseclick event for each emoji
					pane.setOnMouseClicked(event -> {
						onEmojiClicked(pane);
						event.consume();
					});
					// add emoji to grid
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
		for (ChatListener listener : listeners) {
			// notify listener about chosen emoji
			listener.onEmojiChoose(p.getStyleClass().get(1));
		}
	}

	public void addListener(ChatListener listener) {
		listeners.add(listener);
	}
}
