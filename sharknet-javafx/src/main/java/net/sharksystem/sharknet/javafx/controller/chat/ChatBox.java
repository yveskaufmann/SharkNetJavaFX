package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import static net.sharksystem.sharknet.javafx.i18n.I18N.getString;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Benni on 07.07.2016.
 * This Class is used as replacement for listview items. Listview items with
 * different sizes caused a visual bug with duplicated entries.
 */
public class ChatBox extends HBox {

	@FXML
	private Label labelMessage;
	@FXML
	private Label labelTime;
	@FXML
	private ImageView imageViewEncrypted;
	@FXML
	private ImageView imageViewSigned;
	@FXML
	private ImageView imageViewDirectContact;
	@FXML
	private HBox hboxGridContainer;
	@FXML
	private HBox hboxMessage;
	@FXML
	private GridPane gridPaneMessages;

	private static final SimpleDateFormat timeformat = new SimpleDateFormat("H:mm");
	private static final SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");

	private Message msg;
	// to fix resize problem, not working 100%
	private double height = -1.0;

	public ChatBox(Message msg) {
		super();
		this.msg = msg;
		FXMLLoader loader = new FXMLLoader(App.class.getResource("views/chat/chatWindowEntry.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try{
			loader.load();
		}catch(IOException exception){
			throw new RuntimeException(exception);
		}
		update();
	}

	/**
	 * update all elements
	 */
	private void update() {
		// somehow css wasnt working...
		imageViewEncrypted.setOpacity(0.25);
		imageViewSigned.setOpacity(0.25);
		imageViewDirectContact.setOpacity(0.25);

		if (msg == null) {
			return;
		}

		// if it's just a dummy message for a date divider
		if (msg.getContent().getMessage().equals(":datedivider:")) {
			getStyleClass().add("datedivider");
			hboxGridContainer.getChildren().clear();
			Label label = new Label();
			label.getStyleClass().add("chatDivider");
			// reference dates for determing yesterday, today, ...
			LocalDate msgDate = msg.getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate yesterday = LocalDate.now().minusDays(1);
			LocalDate today = LocalDate.now();

			// if the message is from yesterday
			if (yesterday.equals(msgDate)) {
				label.setText(getString("chat.divider.yesterday"));
			}
			// if the msg is not from today or yesterday...
			else if (today.equals(msgDate)) {
				label.setText(getString("chat.divider.today"));
			}
			// if the messag is not from yesterday or today
			else {
				label.setText(dateformat.format(msg.getTimestamp()));
			}
			// add divider
			hboxGridContainer.getChildren().add(label);
			return;
		}
		getStyleClass().add("chatbox");
		// if emoji was found
		if (msg.getContent().getMessage().matches(".*[:emojione-].*[:].*")) {
			// using textflow for emoji support
			TextFlow textFlow = new TextFlow();
			//textFlow.setPadding(new Insets(50, 0, 0, 0));
			Label sender = new Label();
			sender.setText("<" + msg.getSender().getNickname() + ">");
			textFlow.getChildren().add(sender);
			// split the whole message
			String[] splitted = msg.getContent().getMessage().split(":");
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
			if (msg.getContent().getVoting() != null) {
				hboxMessage.getChildren().remove(labelMessage);
				GridPane grid = new GridPane();
				grid.getColumnConstraints().add(new ColumnConstraints(35));
				grid.getColumnConstraints().add(new ColumnConstraints(175));
				// first row
				grid.getRowConstraints().add(new RowConstraints(75));
				//grid.setPrefHeight(25);
				grid.setPrefWidth(210);
				Label labelSender = new Label();
				labelSender.setText("<" + msg.getSender().getNickname() + ">");
				Label questionLabel = new Label();
				questionLabel.setMaxHeight(100);
				questionLabel.setPrefWidth(300);
				questionLabel.setText(msg.getContent().getVoting().getQuestion());
				questionLabel.setWrapText(true);
				grid.add(labelSender, 0, 0);
				grid.add(questionLabel, 1, 0);

				HashMap<String, Contact> answerList = msg.getContent().getVoting().getAnswers();
				Iterator it = answerList.entrySet().iterator();
				int index = 1;
				ToggleGroup toggle = new ToggleGroup();

				while (it.hasNext()) {
					grid.getRowConstraints().add(new RowConstraints(25));
					Map.Entry pair = (Map.Entry)it.next();
					Label answerLabel = new Label();
					answerLabel.setText(pair.getKey().toString());
					answerLabel.setPrefWidth(gridPaneMessages.getPrefWidth() - 100);
					answerLabel.setWrapText(true);
					// single choice
					if (msg.getContent().getVoting().isSingleqoice()) {
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
				labelMessage.setText("<" + msg.getSender().getNickname() + ">" + " " + msg.getContent().getMessage());
			}
		}

		java.sql.Timestamp timestamp = msg.getTimestamp();
		labelTime.setText(timeformat.format(timestamp));
		if (!msg.isEncrypted()) {
			InputStream in = null;
			in = App.class.getResourceAsStream("images/ic_no_encryption_black_24dp.png");
			if (in != null) {
				imageViewEncrypted.setImage(new Image(in));
			}

		}
		if (!msg.isDierectRecived()) {
			imageViewDirectContact.setVisible(false);
		}
		if (!msg.isSigned()) {
			//imageViewSigned.setOpacity(0.25);
			imageViewSigned.setVisible(false);
		}
		// if message is signed but not verified, change picture to just 1 check
		else if (msg.isSigned() && !msg.isVerified()) {
			// profile picture
			InputStream in = null;
			in = App.class.getResourceAsStream("images/check.png");
			if (in != null) {
				imageViewSigned.setImage(new Image(in));
			}
		}
		// position message
		if (!msg.isMine()) {
			hboxGridContainer.setAlignment(Pos.TOP_RIGHT);
		} else {
			hboxGridContainer.setAlignment(Pos.TOP_LEFT);
		}
	}

	public Message getMessage() {
		return msg;
	}

	/**
	 * try to fix the resize problem, not working 100%
	 */
	@Override
	protected void layoutChildren() {
		if (height < 0) {
			height = computePrefHeight(-1);
			setMaxHeight(height);
			setMinHeight(height);
			setPrefHeight(height);
		}
		super.layoutChildren();
	}
}
