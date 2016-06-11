package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import net.sharksystem.sharknet.api.Message;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCellController;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by Benni on 04.06.2016.
 */
public class ChatWindowListController extends MediaListCellController<Message> {

	@FXML
	private Label labelMessage;
	@FXML
	private Label labelTime;
	@FXML
	private ImageView imageViewEncrypted;
	@FXML
	private ImageView imageViewSigned;
	@FXML
	private HBox hboxGridContainer;
	@FXML
	private HBox hboxMessage;
	@FXML
	private GridPane gridPaneMessages;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm");

	public ChatWindowListController(MediaListCell<Message> chatHistoryListCell) {
		super(App.class.getResource("views/chat/chatWindowEntry.fxml"), chatHistoryListCell);
	}

	@Override
	protected void onItemChanged(Message message) {
		if (message == null) {
			return;
		}

		TextFlow textFlow = new TextFlow();
		textFlow.setPadding(new Insets(30, 5, 0, 5));
		textFlow.setLineSpacing(0);


		if (message.getContent().getMessage().matches(".*[:emojione-].*[:].*")) {
			String[] splitted = message.getContent().getMessage().split(":");
			for (int i = 0; i < splitted.length; i++) {
				System.out.println(splitted[i]);

				if (splitted[i].matches("[emojione-].*")) {
					Pane smileyPane = new Pane();
					//smileyPane.setPrefHeight(128);
					//smileyPane.setPrefWidth(128);
					smileyPane.getStyleClass().addAll("emojione", splitted[i].trim());
					//smileyPane.setPadding(new Insets(50, 5, 0, 5));
					textFlow.getChildren().add(smileyPane);
				} else {
					//Text text = new Text();
					//text.setText(splitted[i].trim());
					Label label = new Label();
					label.setText(splitted[i].trim());
					label.setWrapText(true);
					textFlow.getChildren().add(label);
				}
			}
			hboxMessage.getChildren().add(textFlow);
			hboxMessage.getChildren().remove(labelMessage);
			//gridPaneMessages.getChildren().add(textFlow);
			labelMessage.setVisible(false);

			System.out.println("found emoji");
		}
		// if message doesn't contain any emoji...
		else {
			labelMessage.setText("<" + message.getSender().getNickname() + ">" + " " + message.getContent().getMessage());
		}


		java.sql.Timestamp timestamp = message.getTimestamp();
		labelTime.setText(dateFormat.format(timestamp));

		if (!message.isEncrypted()) {
			imageViewEncrypted.setOpacity(0.25);
		}
		if (!message.isSigned()) {
			imageViewSigned.setOpacity(0.25);
		}
		if (!message.isMine()) {
			hboxGridContainer.setAlignment(Pos.TOP_RIGHT);
			//labelMessage.setAlignment(Pos.TOP_RIGHT);
			//labelMessage.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		} else {
			hboxGridContainer.setAlignment(Pos.TOP_LEFT);
			//labelMessage.setAlignment(Pos.TOP_LEFT);
			//labelMessage.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		}


	}

	@Override
	protected void onFxmlLoaded() {

	}
}
