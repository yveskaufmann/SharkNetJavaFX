package net.sharksystem.sharknet.javafx.controller.chat;

import com.google.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.api.Chat;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Message;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCellController;
import net.sharksystem.sharknet.javafx.services.ImageManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benni on 31.05.2016.
 *
 * responsible for custom listview entry @chathistorylistview
 */
public class ChatHistoryEntryController extends MediaListCellController<Chat> {

	@Inject
	private SharkNet sharkNetModel;

	@Inject
	private ImageManager imageManager;

	@FXML
	private Text chatContacts;
	@FXML
	private Text chatLastMessage;
	@FXML
	private Text chatTitle;
	@FXML
	private Label chatContent;
	@FXML
	private ImageView imageViewDelete;
	@FXML
	private ImageView imageViewContactProfile;
	@FXML
	private Label labelNewMsgCount;

	private ObjectProperty<Chat> chatProp;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy H:mm");

	public ChatHistoryEntryController(MediaListCell<Chat> chatHistoryListCell) {
		super(App.class.getResource("views/chat/chatHistoryEntry.fxml"), chatHistoryListCell);
	}

	@Override
	protected void onFxmlLoaded() {
		imageViewDelete.setOnMouseClicked(event -> {
			if (event.getSource() instanceof ImageView) {
				ImageView view = (ImageView) event.getSource();
				if (view.getUserData() instanceof Chat) {
					Chat chat = (Chat) view.getUserData();
					chatDeleted(chat);
				}
			}
			event.consume();
		});
		imageViewDelete.setPickOnBounds(true);
		imageViewDelete.setOpacity(0.5);
		imageViewDelete.setOnMouseEntered(event -> {
			imageViewDelete.setOpacity(1.0);
		});
		imageViewDelete.setOnMouseExited(event -> {
			imageViewDelete.setOpacity(0.5);
		});
	}

	@Override
	protected void onItemChanged(Chat chat) {

		if (chat == null) {
			return;
		}
		List<String> contactNames = new ArrayList<>();
		labelNewMsgCount.setVisible(false);
		for (Contact contact : chat.getContacts()) {
			contactNames.add(contact.getNickname());
		}
		// check if some of the msgs are unread
		int newMsgCount = 0;
		for (Message msg : chat.getMessages(false)) {
			if (!msg.isRead()) {
				newMsgCount++;
			}
		}
		// if we found unread msgs, enable newmsg label
		if (newMsgCount > 0) {
			labelNewMsgCount.setText(String.valueOf(newMsgCount));
			labelNewMsgCount.setVisible(true);
		}
		String senders = String.join(", ", contactNames);
		// set sender label
		chatContacts.setText(senders);
		if (chat.getMessages(false) != null && chat.getMessages(false).size() > 0) {
			java.sql.Timestamp timestamp = chat.getMessages(false).get(chat.getMessages(false).size()-1).getTimestamp();
			// timestamp of last message
			chatLastMessage.setText(dateFormat.format(timestamp));
			// chat preview (last message)
			// remove any emojis...
			if (chat.getMessages(false).get(chat.getMessages(false).size()-1).getContent().getMessage().matches(".*[:emojione-].*[:].*")) {
				String tmp = chat.getMessages(false).get(chat.getMessages(false).size() - 1).getContent().getMessage();
				tmp = tmp.replaceAll(":emojione-.*[:]", "");
				chatContent.setText(tmp);
			}
			// if msg doesnt contain any emojis, just display the msg
			else {
				chatContent.setText(chat.getMessages(false).get(chat.getMessages(false).size()-1).getContent().getMessage());
			}
		}
		// set chat title
		chatTitle.setText(chat.getTitle());
		// set chat picture, if no chat picture is set, just use the picture from chat contact
		for (Contact contact : chat.getContacts()) {
			if (!contact.isEqual(sharkNetModel.getMyProfile().getContact())) {
				imageManager.readImageFrom(contact.getPicture()).ifPresent(imageViewContactProfile::setImage);
				break;
			}
		}
		imageManager.readImageFrom(chat.getPicture()).ifPresent(imageViewContactProfile::setImage);
		imageViewDelete.setUserData(chat);
	}

	private void chatDeleted(Chat c) {
		ChatController controller = ChatController.getInstance();
		controller.onChatDeleted(c);
	}
}
