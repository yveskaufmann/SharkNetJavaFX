package net.sharksystem.sharknet.javafx.controller.chat;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller( title = "%sidebar.chat")
public class ChatController extends AbstractController implements ChatHistoryListener, ChatContactsListener{

	private FrontController frontController;
	public static ChatController chatControllerInstance;
	private Chat activeChat;
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
		activeChat = null;
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
		if (activeChat != null) {
			FileChooser fileChooser = new FileChooser();
			Stage stage = new Stage();
			fileChooser.setTitle("Select Attachment");
			File file = fileChooser.showOpenDialog(stage);
			//TODO: finish..
			if (file != null) {

			}
		}

	}

	private void onAddClick() {
		System.out.println("onAddClick");

		if (activeChat != null) {
			ChatContactsController c = new ChatContactsController();
			c.setContactListListener(this);
		}
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
		textAreaChat.appendText('\n' + CHATPREFIX + " " + message);
		if (activeChat != null) {
			activeChat.sendMessage(message);
		}
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
		fillChatArea(c);
		activeChat = c;

		if (activeChat.getPicture() != null && activeChat.getPicture().length() > 0) {
			imageViewContactProfile.setImage(new Image(activeChat.getPicture()));
		}
	}

	private void fillChatArea(Chat c) {
		textAreaChat.clear();

		for (int i = 0; i < c.getMessages().size(); i++) {
			if (c.getMessages().get(i).getSender() != null && c.getMessages().get(i).getContent() != null) {
				textAreaChat.appendText("<" + c.getMessages().get(i).getSender().getNickname() + "> " + c.getMessages().get(i).getContent() + '\n');
			}
		}
	}

	// triggered when the add contacts window is closed
	@Override
	public void onContactListChanged(List<Contact> c) {
		System.out.println("oncontactslistchanged");
		if (c.size() > 0) {
			activeChat.getContacts().addAll(c);
		}
	}
}
