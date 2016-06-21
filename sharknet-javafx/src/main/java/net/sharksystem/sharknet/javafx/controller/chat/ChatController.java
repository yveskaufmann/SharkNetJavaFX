package net.sharksystem.sharknet.javafx.controller.chat;


import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;

import java.io.*;
import java.util.List;


@Controller( title = "%sidebar.chat")
public class ChatController extends AbstractController implements ChatListener {

	@Inject
	private SharkNet sharkNetModel;

	@Inject
	private ImageManager imageManager;

	private FrontController frontController;


	public static ChatController chatControllerInstance;
	private Chat activeChat;
	private Content attachment;

	// used so we can use the same window / class for adding contacts and new chats
	private boolean newChat;
	private boolean addChatContacts;

	// if true, the next message will contain the fileAttachment
	private boolean sendAttachment;


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
	private ImageView imageViewEmoji;
	@FXML
	private TextArea textAreaChat;
	@FXML
	private ChatHistoryList chatHistoryListView;
	@FXML
	private Button buttonSend;
	@FXML
	private Button buttonNewChat;
	@FXML
	private ChatWindowList chatWindowListView;
	@FXML
	private Label labelChatRecipients;

	public ChatController() {
		super(App.class.getResource("views/chat/chatView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);
		activeChat = null;
		chatControllerInstance = this;
		newChat = false;
		addChatContacts = false;
		sendAttachment = false;
		attachment = null;
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

		imageViewEmoji.setOnMouseClicked(event -> {
			onEmojiClick();
			event.consume();
		});

		buttonSend.setOnMouseClicked(event -> {
			onSendClick();
			event.consume();
		});

		buttonNewChat.setOnMouseClicked(event -> {
			onNewChatClick();
			event.consume();
		});

		chatHistoryListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		chatHistoryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onChatSelected(chatHistoryListView.getSelectionModel().getSelectedItem());
		});
		loadChatHistory();


	}

	private void onAttachmentClick() {
		// TODO: display attachment in chat somehow..
		System.out.println("onAttachmentClick");
		if (activeChat != null) {
			FileChooser fileChooser = new FileChooser();
			Stage stage = new Stage();
			fileChooser.setTitle("Select Attachment");
			File file = fileChooser.showOpenDialog(stage);
			if (file != null) {
				try {
					InputStream fileAttachment = new FileInputStream(file.getPath());
					fileAttachment.close();
					sendAttachment = true;
					String extension = "";
					int i = file.getPath().lastIndexOf('.');
					if (i > 0) {
						extension = file.getPath().substring(i + 1);
					}
					attachment = new ImplContent(fileAttachment, extension, file.getName());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {

				}
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
		VoteController voteController = new VoteController();

	}


	private void onSendClick() {

		if (activeChat != null) {
			if (sendAttachment && attachment != null) {
				attachment.setMessage(textFieldMessage.getText() + "<" + attachment.getFileName() + ">");
				activeChat.sendMessage(attachment);
			} else {
				activeChat.sendMessage(new ImplContent(null, "", "", textFieldMessage.getText()));
			}

				//TODO: saving... seems not to work
			//activeChat.save();
			fillChatArea(activeChat);
			sendAttachment = false;
			attachment = null;
		}

	}

	@FXML
	private void onNewChatClick() {
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

	private void onChatSelected(Chat c) {
		fillChatArea(c);
		activeChat = c;

		if (activeChat.getPicture() != null) {
			imageManager.readImageFrom(activeChat.getPicture()).ifPresent(imageViewContactProfile::setImage);
		}

		String contactNames = "";

		for (int i = 0; i < activeChat.getContacts().size(); i++) {
			if (!activeChat.getContacts().get(i).isEqual(sharkNetModel.getMyProfile().getContact())) {
				contactNames += activeChat.getContacts().get(i).getNickname();
				if (i < activeChat.getContacts().size()-1 && !activeChat.getContacts().get(i+1).isEqual(sharkNetModel.getMyProfile().getContact())) {
					contactNames += " , ";
				}
			}
		}

		labelChatRecipients.setText(contactNames);
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

			//Chat chat = new ImplChat(c, sharkNetModel.getMyProfile().getContact(), sharkNetModel.getMyProfile());
			Chat chat = sharkNetModel.newChat(c);
			chatHistoryListView.getItems().add(chat);

			// TODO: check why sharknet suddenly returns a triple size chatlist when the following lines are enables...
			//sharkNetModel.getChats().add(chat);
			//chat.save();
			activeChat = chat;
			fillChatArea(activeChat);
			loadChatHistory();

		}

		addChatContacts = false;
		newChat = false;
	}

	@Override
	public void onEmojiChoose(String emojiClass) {
		textFieldMessage.appendText(" :" + emojiClass + ": ");
	}

	private void onEmojiClick() {
		System.out.println("onEmojiClick");
		EmojiController e = new EmojiController();
		e.setListener(this);
	}
}
