package net.sharksystem.sharknet.javafx.controller.chat;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.model.SharkNetModel;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import java.io.File;
import java.util.List;


@Controller( title = "%sidebar.chat")
public class ChatController extends AbstractController implements ChatContactsListener{

	private FrontController frontController;
	public static ChatController chatControllerInstance;
	private Chat activeChat;
	private ImplSharkNet sharkNetModel;

	private final String CHATPREFIX = "<You>";
	// used so we can use the same window / class for adding contacts and new chats
	private boolean newChat;
	private boolean addChatContacts;
	private Contact me;

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
	@FXML
	private ChatWindowList chatWindowListView;


	public ChatController() {
		super(App.class.getResource("views/chat/chatView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);

		/*
		sharkNetModel = new ImplSharkNet();
		sharkNetModel.fillWithDummyData();
		sharkNetModel.setProfile(sharkNetModel.getProfiles().get(1), "");
		*/
		sharkNetModel = SharkNetModel.getInstance().getSharkNetImpl();

		activeChat = null;
		chatControllerInstance = this;
		newChat = false;
		addChatContacts = false;
		// TODO: change me contact...
		me = new ImplContact("me", "meuid", "pk", sharkNetModel.getMyProfile());
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


		chatHistoryListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		chatHistoryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			     onChatSelected(chatHistoryListView.getSelectionModel().getSelectedItem());
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
			addChatContacts = true;
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

		if (activeChat != null) {
			Message message = new ImplMessage(new ImplContent(null, "", textFieldMessage.getText()), activeChat.getContacts(), me, sharkNetModel.getMyProfile());
			chatWindowListView.getItems().add(message);
			activeChat.sendMessage(message.getContent());
			//TODO: saving... seems not to work
			//activeChat.save();
		}
	}

	@FXML
	private void onNewChatClick(ActionEvent event) {
		// TODO: new window with contacts?
		System.out.println("onNewChatClick");

		newChat = true;
		ChatContactsController c = new ChatContactsController();
		c.setContactListListener(this);
	}

	private void loadChatHistory() {
		// TODO: seperate chats in today, yesterday, earlier...
		chatHistoryListView.getItems().clear();
		List<Chat> chatList = sharkNetModel.getChats();

		for (int i = 0; i < chatList.size(); i++) {
			chatHistoryListView.getItems().add(chatList.get(i));
		}
	}

	public void onChatSelected(Chat c) {
		fillChatArea(c);
		activeChat = c;

		if (activeChat.getPicture() != null && activeChat.getPicture().length() > 0) {
			imageViewContactProfile.setImage(new Image(activeChat.getPicture()));
		}
	}

	private void fillChatArea(Chat c) {
		chatWindowListView.getItems().clear();

		if (c.getMessages() != null) {
			for (int i = 0; i < c.getMessages().size(); i++) {
				chatWindowListView.getItems().add(c.getMessages().get(i));
			}
		}
	}

	// triggered when the add contacts window is closed
	@Override
	public void onContactListChanged(List<Contact> c) {
		System.out.println("oncontactslistchanged");

		if (addChatContacts) {
			if (c.size() > 0) {
				activeChat.getContacts().addAll(c);
			}
		} else if (newChat) {

			Chat chat = new ImplChat(c, me, sharkNetModel.getMyProfile());
			chatHistoryListView.getItems().add(chat);
			// TODO: check why sharknet suddenly returns a triple size chatlist when the following lines are enables...
			//sharkNetModel.getChats().add(chat);
			activeChat = chat;
			//loadChatHistory();

		}

		addChatContacts = false;
		newChat = false;
	}
}
