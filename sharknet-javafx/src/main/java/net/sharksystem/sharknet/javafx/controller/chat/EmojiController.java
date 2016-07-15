package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import java.io.InputStream;
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
	private Stage stage;
	private static EmojiController instance = null;

	public static EmojiController getInstance() {
		if (instance == null) {
			instance = new EmojiController();
		}

		return instance;
	}

	private EmojiController() {
		super(App.class.getResource("views/chat/emojiView.fxml"));
		listeners = new ArrayList<>();
		Parent root = super.getRoot();
		stage = new Stage();
		stage.setScene(new Scene(root, 494, 414));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();
		// when user clicks outside of emoji window, the window will be closed
		stage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
			if (!isNowFocused) {
				stage.hide();
			}
		});

		InputStream in = App.class.getResourceAsStream("images/shark-icon256x256.png");
		if (in != null) {
			stage.getIcons().add(new Image(in));
		}

		emoji = Emoji.getInstance();
		emojis = emoji.getEmojis();

		int counter = 0;
		// loading emojis from .png using css
		for (int i = 0; i < Emoji.cols; i++) {
			for (int j = 0; j < Emoji.rows; j++) {
				if (counter < emojis.size()) {
					Pane pane = new Pane();
					pane.getStyleClass().addAll("emojione", emojis.get(counter));
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

	public void showWindow() {
		stage.show();
		stage.toFront();
	}
}
