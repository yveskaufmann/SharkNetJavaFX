package net.sharksystem.sharknet.javafx.controller.chat;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

import java.util.ArrayList;
import java.util.List;


@Controller( title = "%sidebar.chat")
public class ChatController extends AbstractController implements ChatHistoryListener{

	private FrontController frontController;
	public static ChatController chatControllerInstance;
	private ImplChat chatModel;
	private ImplSharkNet sharkNetModel;

	private final String CHATPREFIX = "<You>";

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
	private ChatHistoryList chatHistoryListView;
	@FXML
	private Button buttonSend;

	public ChatController(FrontController frontController) {
		super(App.class.getResource("views/chat/chatView.fxml"));
		this.frontController = frontController;

		sharkNetModel = new ImplSharkNet();
		sharkNetModel.fillWithDummyData();
		//chatModel = new ImplChat(sharkNetModel.getContacts());
		chatControllerInstance = this;
	}

	public static ChatController getInstance() {
		if (chatControllerInstance != null) {
			return chatControllerInstance;
		}
		else {
			return null;
		}
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

		buttonSend.setOnMouseClicked(event -> {
			onSendClick();
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


	private void onSendClick() {
		String message = textFieldMessage.getText();
		//chatModel.sendMessage(message);
		textAreaChat.appendText('\n' + CHATPREFIX + " " + message);
	}

	@FXML
	private void onNewChatClick(ActionEvent event) {
		// TODO: new window with contacts?
		System.out.println("onNewChatClick");
	}

	private void loadChatHistory() {
		// TODO: seperate chats in today, yesterday, earlier...

		List<Chat> chatList = sharkNetModel.getChats();

		for (int i = 0; i < chatList.size(); i++) {
			chatHistoryListView.getItems().add(chatList.get(i));
		}
	}

	@Override
	public void onChatSelected(Chat c) {
		System.out.println(c.getContacts().get(0).getNickname());
		System.out.println("onchatselected");
	}
}
