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
import net.sharksystem.sharknet.javafx.controller.contactlist.ShowContactController;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;

import java.io.*;
import java.nio.file.Files;
import java.util.List;


@Controller( title = "%sidebar.chat")
public class ChatController extends AbstractController implements ChatListener {

	@Inject
	private SharkNet sharkNetModel;

	@Inject
	private ImageManager imageManager;

	private FrontController frontController;

	public static ChatController chatControllerInstance;
	// the currently active chat object
	private Chat activeChat;
	// attachment object
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
		// set onMouseClick for emoji ImageView
		imageViewEmoji.setOnMouseClicked(event -> {
			onEmojiClick();
			event.consume();
		});
		// set onMouseClick for send Button
		buttonSend.setOnMouseClicked(event -> {
			onSendClick();
			event.consume();
		});
		// set onMouseCLick for newchat Button
		buttonNewChat.setOnMouseClicked(event -> {
			onNewChatClick();
			event.consume();
		});
		// set listener for chathistorylistview items
		chatHistoryListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		chatHistoryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onChatSelected(chatHistoryListView.getSelectionModel().getSelectedItem());
		});
		// load all chats into chathistorylistview
		loadChatHistory();
	}

	/**
	 * sending attachment... open filedialog
	 */
	private void onAttachmentClick() {
		System.out.println("onAttachmentClick");
		if (activeChat != null) {
			// setup and open filechooser
			FileChooser fileChooser = new FileChooser();
			Stage stage = new Stage();
			fileChooser.setTitle("Select Attachment");
			File file = fileChooser.showOpenDialog(stage);
			// if a file is selected
			if (file != null) {
				try {
					// load file into stream
					InputStream fileAttachment = new FileInputStream(file.getPath());
					// determine mimeType
					String mimeType = Files.probeContentType(file.toPath());
					fileAttachment.close();
					// set flag for attachment
					sendAttachment = true;
					// extract extension
					String extension = "";
					int i = file.getPath().lastIndexOf('.');
					if (i > 0) {
						extension = file.getPath().substring(i + 1);
					}
					// create attachment object
					// ToDo: set mimetype
					attachment = new ImplContent(fileAttachment, extension, file.getName());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {

				}
			}
		}

	}

	/**
	 * add contacts to active chat conversation
	 */
	private void onAddClick() {
		System.out.println("onAddClick");
		if (activeChat != null) {
			addChatContacts = true;
			// open add contacts window
			ChatContactsController c = new ChatContactsController();
			c.setContactListListener(this);
		}
	}

	private void onContactProfileClick() {
		System.out.println("onContactProfileClick");
		//ToDo: what to do with group chat?
		if (activeChat != null && activeChat.getContacts().size() == 1) {
			ShowContactController sc = new ShowContactController(activeChat.getContacts().get(0));
		}
	}

	private void onVoteClick() {
		System.out.println("onVoteClick");
		if (activeChat != null) {
			// open vote window
			VoteController voteController = new VoteController();
		}
	}

	/**
	 * sending chat message
	 */
	private void onSendClick() {

		if (activeChat != null) {
			// if user wants to send attachment
			if (sendAttachment && attachment != null) {
				// append filename to chat message
				attachment.setMessage(textFieldMessage.getText() + "<" + attachment.getFileName() + ">");
				// send message and attachment
				activeChat.sendMessage(attachment);
			} else {
				// just send chat message
				activeChat.sendMessage(new ImplContent(null, "", "", textFieldMessage.getText()));
			}
			// reload chat area, so new message is shown
			fillChatArea(activeChat);
			// reset flags
			sendAttachment = false;
			attachment = null;
		}
	}

	/**
	 * starting a new chat
	 */
	private void onNewChatClick() {
		System.out.println("onNewChatClick");
		// set flag
		newChat = true;
		// open new chat window
		ChatContactsController c = new ChatContactsController();
		c.setContactListListener(this);
	}

	/**
	 * loading all chats into listview
	 */
	private void loadChatHistory() {
		// TODO: seperate chats in today, yesterday, earlier...
		// clear old chats
		chatHistoryListView.getItems().clear();
		// load chats
		List<Chat> chatList = sharkNetModel.getChats();
		// add chats to listview
		for (int i = 0; i < chatList.size(); i++) {
			chatHistoryListView.getItems().add(chatList.get(i));
		}
	}

	/**
	 * triggered when a chat is selected in the chathistorylistview
	 * @param c
	 */
	private void onChatSelected(Chat c) {
		fillChatArea(c);
		activeChat = c;
		// try to set chat picture
		if (activeChat.getPicture() != null) {
			imageManager.readImageFrom(activeChat.getPicture()).ifPresent(imageViewContactProfile::setImage);
		}
		fillContactLabel();
	}

	/**
	 * fill contact label with all chat contacts, except yourself...
	 */
	private void fillContactLabel() {
		String contactNames = "";
		// loop through chat contacts
		for (int i = 0; i < activeChat.getContacts().size(); i++) {
			// if contact != you
			if (!activeChat.getContacts().get(i).isEqual(sharkNetModel.getMyProfile().getContact())) {
				contactNames += activeChat.getContacts().get(i).getNickname();
				if (i < activeChat.getContacts().size()-1 && !activeChat.getContacts().get(i+1).isEqual(sharkNetModel.getMyProfile().getContact())) {
					contactNames += " , ";
				}
			}
		}
		// set label text
		labelChatRecipients.setText(contactNames);
	}

	/**
	 * fill chat listview
	 * @param c chat
	 */
	private void fillChatArea(Chat c) {
		// clear old chat messages
		chatWindowListView.getItems().clear();
		// if chat contains messages
		if (c.getMessages(false) != null) {
			for (int i = 0; i < c.getMessages(false).size(); i++) {
				// add message to listview
				chatWindowListView.getItems().add(c.getMessages(false).get(i));
			}
		}
	}

	/**
	 * triggered when the add contacts window / new chat window is closed
	 */
	@Override
	public void onContactListChanged(List<Contact> c) {
		System.out.println("oncontactslistchanged");
		// if contacts get added...
		if (addChatContacts) {
			if (c.size() > 0) {
				//ToDo: change to api usage
				activeChat.getContacts().addAll(c);
			}
		// start new chat
		} else if (newChat) {
			// create new chat
			Chat chat = sharkNetModel.newChat(c);
			// add chat to history listview
			loadChatHistory();
			// set active chat
			activeChat = chat;
			// reload chat area
			fillChatArea(activeChat);
			fillContactLabel();
		}
		// reset flags
		addChatContacts = false;
		newChat = false;
	}

	/**
	 * triggered by emoji window click
	 * @param emojiClass chosen emoji
	 */
	@Override
	public void onEmojiChoose(String emojiClass) {
		// insert emoji placiholder in chat input
		textFieldMessage.appendText(" :" + emojiClass + ": ");
	}

	/**
	 * triggered by emoji imageview click
	 */
	private void onEmojiClick() {
		System.out.println("onEmojiClick");
		// open emoji window
		EmojiController e = new EmojiController();
		e.setListener(this);
	}
}
