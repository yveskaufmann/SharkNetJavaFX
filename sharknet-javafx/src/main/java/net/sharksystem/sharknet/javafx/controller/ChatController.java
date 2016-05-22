package net.sharksystem.sharknet.javafx.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import net.sharksystem.sharknet.api.ImplChat;
import net.sharksystem.sharknet.api.Message;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

import java.util.ArrayList;
import java.util.List;


@Controller( title = "%sidebar.chat")
public class ChatController extends AbstractController {

	private AppController appController;
	private ImplChat chatModel;

	private final String chatPrefix = "<You>";

	@FXML
	private TextField textFieldMessage;
	@FXML
	private ImageView imageViewAttachment;
	@FXML
	private ImageView imageViewAdd;
	@FXML
	private ImageView imageViewContactProfile;
	@FXML
	private ImageView imageViewVote;
	@FXML
	private TextArea textAreaChat;
	@FXML
	private ListView listViewChatHistory;

	public ChatController(AppController appController) {
		super(App.class.getResource("views/chatView.fxml"));
		this.appController = appController;
		this.chatModel = new ImplChat(null);
	}

	@Override
	protected void onFxmlLoaded() {
		//TODO: Implement chat controller

		// set onMouseClick for Attachment ImageView
		imageViewAttachment.setOnMouseClicked(event -> {
			onAttachmentClick();
			event.consume();
		});
		// set onMouseClick for Add Chat Contact ImageView
		imageViewAdd.setOnMouseClicked(event -> {
			onAddClick();
			event.consume();
		});
		// set onMouseClick for Contact Profile ImageView
		imageViewContactProfile.setOnMouseClicked(event -> {
			onContactProfileClick();
			event.consume();
		});
		// set onMouseClick for Vote ImageView
		imageViewVote.setOnMouseClicked(event -> {
			onVoteClick();
			event.consume();
		});

		loadChatHistory();
	}

	private void onAttachmentClick() {
		System.out.println("onAttachmentClick");
	}

	private void onAddClick() {
		System.out.println("onAddClick");
	}

	private void onContactProfileClick() {
		System.out.println("onContactProfileClick");
	}

	private void onVoteClick() {
		// TODO: vote window
		System.out.println("onVoteClick");
	}

	@FXML
	private void onSendClick(ActionEvent event) {
		String message = textFieldMessage.getText();
		chatModel.sendMessage(message);
		textAreaChat.appendText('\n' + chatPrefix + " " + message);
	}

	@FXML
	private void onNewChatClick(ActionEvent event) {
		// TODO: new window with contacts?
		System.out.println("onNewChatClick");
	}

	private void loadChatHistory() {
		// TODO: seperate chats in today, yesterday, earlier...
		// get all Messages from SharkKB
		List<Message> msgList = chatModel.getMessages();
		// ArrayList of senders for later converting
		List<String> senderList = new ArrayList<>();

		// extract senders
		for (int i = 0; i < msgList.size(); i++) {
			Message msg = msgList.get(i);
			String senderName = msg.getSender().getNickname();
			senderList.add(senderName);
		}

		// Add senders to listview
		ObservableList<String> senders = FXCollections.observableArrayList(senderList);
		listViewChatHistory.setItems(senders);
	}
}
