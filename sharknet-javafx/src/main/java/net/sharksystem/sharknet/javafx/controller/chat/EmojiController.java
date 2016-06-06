package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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

/**
 * Created by Benni on 06.06.2016.
 */
public class EmojiController extends AbstractController {

	private @FXML
	GridPane gridPaneEmojis;

	private Emoji emoji;
	private BufferedImage[][] emojis;

	public EmojiController() {
		super(App.class.getResource("views/chat/emojiView.fxml"));

		Parent root = super.getRoot();
		Stage stage = new Stage();
		stage.setScene(new Scene(root, 494, 414));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();

		// TODO: relative path

		try {
			emoji = new Emoji(App.class.getResource("images/emojione-sprites.png").openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		emoji.loadEmojis();
		emojis = emoji.getEmojis();

		for (int i = 0; i < Emoji.cols; i++) {
			for (int j = 0; j < Emoji.rows; j++) {
				ImageView smiley = new ImageView();
				smiley.setFitHeight(32);
				smiley.setFitWidth(32);
				smiley.setPreserveRatio(true);

				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {

					ImageIO.write(emojis[i][j], "png", os);
					InputStream is = new ByteArrayInputStream(os.toByteArray());
					smiley.setImage(new Image(is));
					gridPaneEmojis.add(smiley, i, j);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	@Override
	protected void onFxmlLoaded() {



	}
}