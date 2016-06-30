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
import java.util.ArrayList;
import java.util.List;


@Controller( title = "%sidebar.chat")
public class ChatController extends AbstractController implements ChatListener, GetEvents {

	@Inject
	private SharkNet sharkNetModel;

	@Inject
	private ImageManager imageManager;

	private FrontController frontController;

	public static ChatController chatControllerInstance;
	// the currently active chat object
	private Chat activeChat;

	// used so we can use the same window / class for adding contacts and new chats
	private Status status;

	private enum Status {
		NONE, ADDCONTACT, NEWCHAT
	}


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
		status = Status.NONE;
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
		// load first chat from history if there is one
		if (chatHistoryListView.getItems().size() > 0) {
			activeChat = chatHistoryListView.getItems().get(0);
			loadChat(activeChat);
		}

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
					// extract extension
					String extension = "";
					int i = file.getPath().lastIndexOf('.');
					if (i > 0) {
						extension = file.getPath().substring(i + 1);
					}
					// create attachment object
					// ToDo: set mimetype
					Content attachment = new ImplContent(fileAttachment, extension, file.getName());
					sendAttachment(attachment);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {

				}
			}
		}

	}

	private void sendAttachment(Content attachment) {
		// if user wants to send attachment
		if (activeChat != null && attachment != null) {
			// append filename to chat message
			attachment.setMessage(textFieldMessage.getText() + "<" + attachment.getFileName() + ">");
			// send message and attachment
			activeChat.sendMessage(attachment);
		}
		// reload chat area, so new message is shown
		loadChat(activeChat);
	}

	/**
	 * add contacts to active chat conversation
	 */
	private void onAddClick() {
		System.out.println("onAddClick");
		if (activeChat != null) {
			status = Status.ADDCONTACT;
			// open add contacts window
			ChatContactsController c = new ChatContactsController(activeChat);
			c.addListener(this);
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
			voteController.addListener(this);
		}
	}

	@Override
	public void onVoteAdded(Content vote) {
		sendVote(vote);
	}

	private void sendVote(Content vote) {
		if (activeChat != null && vote != null) {
			activeChat.sendMessage(vote);
			loadChat(activeChat);
		}
	}

	/**
	 * sending chat message
	 */
	private void onSendClick() {

		if (activeChat != null) {
			if (textFieldMessage.getText().length() > 0){
				// just send chat message
				activeChat.sendMessage(new ImplContent(null, "", "", textFieldMessage.getText()));
				loadChat(activeChat);
			}
		}
	}

	/**
	 * starting a new chat
	 */
	private void onNewChatClick() {
		System.out.println("onNewChatClick");
		// set flag
		status = Status.NEWCHAT;
		// open new chat window
		ChatContactsController c = new ChatContactsController(null);
		c.addListener(this);
	}

	private void loadChat(Chat c) {
		fillChatArea(c);
		fillContactLabel(c.getContacts());
		// try to set chat picture
		if (c.getPicture() != null) {
			imageManager.readImageFrom(c.getPicture()).ifPresent(imageViewContactProfile::setImage);
		}
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
		for (Chat chat : chatList) {
			chatHistoryListView.getItems().add(chat);
		}
	}

	/**
	 * triggered when a chat is selected in the chathistorylistview
	 * @param c
	 */
	private void onChatSelected(Chat c) {
		activeChat = c;
		System.out.println("count msg:" +activeChat.getMessages(true).size());
		loadChat(activeChat);
	}

	/**
	 * fill contact label with all chat contacts, except yourself...
	 */
	private void fillContactLabel(List<Contact> contactList) {
		List<String> contactNames = new ArrayList<>();
		for (Contact contact : contactList) {
			contactNames.add(contact.getNickname());
		}

		String contacts = String.join(", ", contactNames);
		// set label text
		System.out.println("contacts: " + contacts);
		labelChatRecipients.setText("");
		labelChatRecipients.setText(contacts);
	}

	/**
	 * fill chat listview
	 * @param c chat
	 */
	private void fillChatArea(Chat c) {
		// clear old chat messages
		//chatWindowListView.getItems().removeAll(chatWindowListView.getItems());
		chatWindowListView.getItems().clear();
		// if chat contains messages
		if (c.getMessages(false) != null) {
			for (Message message : c.getMessages(false)) {
				chatWindowListView.getItems().add(message);
			}
		}
	}

	/**
	 * triggered when the add contacts window / new chat window is closed
	 */
	@Override
	public void onContactListChanged(List<Contact> c) {
		System.out.println("oncontactslistchanged");
		for (Contact contact : c) {
			System.out.println(contact.getNickname());
		}
		// if contacts get added...
		if (status == Status.ADDCONTACT) {
			// check if active contact is still in the new list or did he get removed?
			for (Contact activeChatContact : activeChat.getContacts()) {
				boolean found = false;
				// through new contact list and check for equality
				for (Contact contact : c) {
					if (activeChatContact.isEqual(contact)) {
						found = true;
						break;
					}
				}
				// if contact got removed
				if (!found) {
					// ToDo: add api usage when aviable
					System.out.println("contact removed");
					//activechat.remove(activechatcontact)
				}
			}
			List<Contact> tmpContactList = new ArrayList<>();
			// now the check vice versa... check if the contacts from the new list are already in activechat added
			for (Contact contact : c) {
				boolean found = false;
				for (Contact activeChatContact : activeChat.getContacts()) {
					if (contact.isEqual(activeChatContact)) {
						found = true;
						break;
					}
				}
				if (!found) {
					tmpContactList.add(contact);
				}
			}
			// if we found a new contact for this chat
			if (tmpContactList.size() > 0) {
				System.out.println("found new contacts");
				activeChat.addContact(tmpContactList);
			}

			fillContactLabel(c);


		// start new chat
		} else if (status == Status.NEWCHAT) {
			// create new chat
			Chat chat = sharkNetModel.newChat(c);
			// add chat to history listview
			loadChatHistory();
			// set active chat
			activeChat = chat;
			// reload chat area
			//fillChatArea(activeChat);
			//fillContactLabel();
			loadChat(activeChat);
		}
		// reset flags
		status = Status.NONE;
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
		e.addListener(this);
	}

	@Override
	public void receivedMessage(Message m) {
		// ToDo: handling
		//loadChat();
	}

	@Override
	public void receivedFeed(Feed f) {
		// do nothing
	}

	@Override
	public void receivedComment(Comment c) {
		// do nothing
	}

}
