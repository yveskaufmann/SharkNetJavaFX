package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextFlow;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Message;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCellController;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

	private MediaListCell<Message> cell;

	private static final SimpleDateFormat timeformat = new SimpleDateFormat("H:mm");
	private static final SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");

	public ChatWindowListController(MediaListCell<Message> chatHistoryListCell) {
		super(App.class.getResource("views/chat/chatWindowEntry.fxml"), chatHistoryListCell);
		cell = chatHistoryListCell;
	}

	@Override
	protected void onItemChanged(Message message) {
		if (message == null) {
			return;
		}
		// if it's just a dummy message for a date divider
		if (message.getContent().getMessage().equals(":datedivider:")) {
			hboxGridContainer.getChildren().clear();
			Label label = new Label();
			label.getStyleClass().add("chatDivider");

			LocalDate msgDate = message.getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate yesterday = LocalDate.now().minusDays(1);
			LocalDate today = LocalDate.now();

			// if the message is from yesterday
			if (yesterday.equals(msgDate)) {
				label.setText("Yesterday");
			}
			// if the msg is not from today or yesterday...
			else if (today.equals(msgDate)) {
				label.setText("Today");
			}
			// if the messag is not from yesterday or today
			else {
				label.setText(dateformat.format(message.getTimestamp()));
			}

			hboxGridContainer.getChildren().add(label);
			cell.setMinHeight(30);
			cell.setMaxHeight(30);
			cell.setPrefHeight(30);
			return;
		}



		// if emoji was found
		if (message.getContent().getMessage().matches(".*[:emojione-].*[:].*")) {
			// using textflow for emoji support
			TextFlow textFlow = new TextFlow();
			//textFlow.setPadding(new Insets(50, 0, 0, 0));
			Label sender = new Label();
			sender.setText("<" + message.getSender().getNickname() + ">");
			textFlow.getChildren().add(sender);
			// split the whole message
			String[] splitted = message.getContent().getMessage().split(":");
			for (String s : splitted) {
				// if emoji in substring is found
				if (s.matches("[emojione-].*")) {
					// create emoji pane
					Pane smileyPane = new Pane();
					// translate emoji... just a workaround.. fx still thinks emoji is 64x64 px...
					smileyPane.setTranslateY(10.0);
					// add css class
					smileyPane.getStyleClass().addAll("emojione", s.trim());
					smileyPane.setPrefWidth(32.0);
					smileyPane.setPrefHeight(32.0);

					// add emoji to textflow
					textFlow.getChildren().add(smileyPane);

				} else {
					Label label = new Label();
					label.setText(s.trim());
					label.setWrapText(true);
					textFlow.getChildren().add(label);

				}
			}
			// remove default message
			labelMessage.setVisible(false);
			hboxMessage.getChildren().remove(labelMessage);
			hboxMessage.getChildren().add(textFlow);

		}
		// if message doesn't contain any emoji...
		else {
			// if it's a vote
			if (message.getContent().getVoting() != null) {
				hboxMessage.getChildren().remove(labelMessage);
				GridPane grid = new GridPane();
				grid.getColumnConstraints().add(new ColumnConstraints(35));
				grid.getColumnConstraints().add(new ColumnConstraints(175));
				// first row
				grid.getRowConstraints().add(new RowConstraints(75));
				//grid.setPrefHeight(25);
				grid.setPrefWidth(210);
				Label questionLabel = new Label();
				questionLabel.setMaxHeight(100);
				questionLabel.setPrefWidth(300);
				questionLabel.setText(message.getContent().getVoting().getQuestion());
				questionLabel.setWrapText(true);
				grid.add(questionLabel, 1, 0);

				HashMap<String, Contact> answerList = message.getContent().getVoting().getAnswers();
				Iterator it = answerList.entrySet().iterator();
				int index = 1;
				ToggleGroup toggle = new ToggleGroup();

				while (it.hasNext()) {
					grid.getRowConstraints().add(new RowConstraints(25));
					Map.Entry pair = (Map.Entry)it.next();

					Label answerLabel = new Label();
					answerLabel.setText(pair.getKey().toString());
					//answerLabel.setMaxHeight();
					answerLabel.setPrefWidth(gridPaneMessages.getPrefWidth() - 100);
					answerLabel.setWrapText(true);
					// single choice
					if (message.getContent().getVoting().isSingleqoice()) {
						RadioButton rb = new RadioButton();

						rb.setToggleGroup(toggle);
						grid.add(rb, 0, index);
					}
					// multi choice
					else {
						CheckBox cb = new CheckBox();
						grid.add(cb, 0, index);
					}

					grid.add(answerLabel, 1, index);
					index++;
				}
			hboxMessage.getChildren().add(grid);
			}
			// if it's just a simple message
			else {
				labelMessage.setText("<" + message.getSender().getNickname() + ">" + " " + message.getContent().getMessage());
			}
		}

		java.sql.Timestamp timestamp = message.getTimestamp();
		labelTime.setText(timeformat.format(timestamp));

		if (!message.isEncrypted()) {
			imageViewEncrypted.setOpacity(0.25);
		}
		if (!message.isSigned()) {
			imageViewSigned.setOpacity(0.25);
		}
		// if message is signed but not verified, change picture to just 1 check
		else if (message.isSigned() && !message.isVerified()) {
			// profile picture
			InputStream in = null;
			in = App.class.getResourceAsStream("images/check.png");
			if (in != null) {
				imageViewSigned.setImage(new Image(in));
			}
		}
	
		// position message
		if (!message.isMine()) {
			hboxGridContainer.setAlignment(Pos.TOP_RIGHT);
		} else {
			hboxGridContainer.setAlignment(Pos.TOP_LEFT);
		}
	}

	@Override
	protected void onFxmlLoaded() {

	}
}
