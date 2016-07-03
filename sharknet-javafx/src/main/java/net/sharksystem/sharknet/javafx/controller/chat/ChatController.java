package net.sharksystem.sharknet.javafx.controller.chat;


import com.google.inject.Inject;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller( title = "%sidebar.chat")
public class ChatController extends AbstractController implements ChatListener, GetEvents {

	private  static final Logger Log = LoggerFactory.getLogger(ChatController.class);

	@Inject
	private SharkNet sharkNetModel;

	@Inject
	private ImageManager imageManager;

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

	private FrontController frontController;

	public ChatController() {
		super(App.class.getResource("views/chat/chatView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);
		activeChat = null;
		chatControllerInstance = this;
		status = Status.NONE;
		// enable sending messages with ENTER, works just if textFieldMessage is focused
		this.getRoot().addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if (ev.getCode() == KeyCode.ENTER) {
				onSendClick();
			}
			ev.consume();
		});
		// removing messages with del
		this.getRoot().addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if (ev.getCode() == KeyCode.DELETE) {
				onMessageDelete();
			}
			ev.consume();
		});
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
			/*Message m = new ImplMessage(new ImplContent("Das ist eine neue Nachricht. Bla blub keks tralalalalalala wer wie wo was der die das bla blub keks"), sharkNetModel.getContacts(), sharkNetModel.getMyProfile().getContact(), sharkNetModel.getMyProfile());
			receivedMessage(m);*/
			event.consume();
		});
		// set listener for chathistorylistview items
		chatHistoryListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		chatHistoryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onChatSelected(chatHistoryListView.getSelectionModel().getSelectedItem());
		});
		// scroll to last chat message in chatwindow, everytime a new message gets added
		chatWindowListView.getItems().addListener(new ListChangeListener<Message>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Message> c) {
				chatWindowListView.scrollTo(c.getList().size()-1);

			}
		});

		// load all chats into chathistorylistview
		loadChatHistory();
		// load first chat from history if there is one
		if (chatHistoryListView.getItems().size() > 0) {
			activeChat = chatHistoryListView.getItems().get(0);
			loadChat(activeChat);
		}
	}

	private void onMessageDelete() {
		if (chatWindowListView.getSelectionModel().getSelectedItem() != null) {
			// delete msg dialog
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Delete Message");
			alert.setHeaderText("Delete this message?");

			ButtonType buttonDel = new ButtonType("Delete");
			ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(buttonDel, buttonCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == buttonDel){
				Message msg = chatWindowListView.getSelectionModel().getSelectedItem();
				msg.deleteMessage();
				loadChat(activeChat);
				loadChatHistory();

			}
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
		// ToDo: add check for chat admin
		System.out.println("onAddClick");
		if (activeChat != null) {
			// make sure just chatadmin can remove / add contacts
			if (activeChat.getAdmin().isEqual(sharkNetModel.getMyProfile().getContact())) {
				status = Status.ADDCONTACT;
				// open add contacts window
				ChatContactsController c = new ChatContactsController(activeChat);
				c.addListener(this);
			}
			else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error editing Chat");
				alert.setContentText("You have to be admin in order to do this!");
				alert.setHeaderText("");
				alert.showAndWait();
				return;
			}
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

	@Override
	public void onChatDeleted(Chat chat) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Delete Chat");
		alert.setHeaderText("Delete this Chat?");

		ButtonType buttonDel = new ButtonType("Delete");
		ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonDel, buttonCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == buttonDel){
			// load all chats into chathistorylistview
			chat.delete();
			loadChatHistory();
			// load first chat from history if there is one
			if (chatHistoryListView.getItems().size() > 0) {
				activeChat = chatHistoryListView.getItems().get(0);
				loadChat(activeChat);
			}
		}
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
				chatHistoryListView.refresh();
				// clear message input
				textFieldMessage.setText("");

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
		if (c != null) {
			System.out.println("loadchat");
			for (Message m : c.getMessages(false)) {
			System.out.println(m.getContent().getMessage());
			}

			fillChatArea(c);
			fillContactLabel(c.getContacts());
			// try to set chat picture
			if (c.getPicture() != null) {
				imageManager.readImageFrom(c.getPicture()).ifPresent(imageViewContactProfile::setImage);
			}
		}
	}

	/**
	 * loading all chats into listview
	 */
	private void loadChatHistory() {
		// TODO: seperate chats in today, yesterday, earlier...
		// load chats
		List<Chat> chatList = sharkNetModel.getChats();
		Log.debug("reload chat history");
		// remove old chats and add new chats to listview
		chatHistoryListView.getItems().setAll(chatList);
	}

	/**
	 * triggered when a chat is selected in the chathistorylistview
	 * @param c
	 */
	private void onChatSelected(Chat c) {
		if (c != null) {
			activeChat = c;
			loadChat(activeChat);
		}
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
		// if contacts get added...
		if (status == Status.ADDCONTACT) {
			// check if active contact is still in the new list or did he get removed?
			for (Contact activeChatContact : activeChat.getContacts()) {
				boolean found = false;
				// loop through new contact list and check for equality
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
			loadChatHistory();

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
		//loadChat(m.getChat());
		NewMessageController controller = new NewMessageController(m);
		// ToDo: update history listview
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
