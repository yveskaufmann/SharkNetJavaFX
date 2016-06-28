package net.sharksystem.sharknet.javafx.controller.chat;

import com.google.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.api.Chat;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCellController;
import net.sharksystem.sharknet.javafx.services.ImageManager;

import java.io.IOException;
import java.text.SimpleDateFormat;

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
	private Text chatContent;

	@FXML
	private ImageView imageViewContactProfile;

	private ObjectProperty<Chat> chatProp;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat();

	public ChatHistoryEntryController(MediaListCell<Chat> chatHistoryListCell) {
		super(App.class.getResource("views/chat/chatHistoryEntry.fxml"), chatHistoryListCell);
	}

	@Override
	protected void onFxmlLoaded() {

	}

	@Override
	protected void onItemChanged(Chat chat) {
		String senders = chat.getContacts().get(0).getNickname();

		if (chat == null) {
			return;
		}

		// extracting senders
		if (chat.getContacts().size() > 1) {
			for (int i = 1; i < chat.getContacts().size(); i++) {
				senders += " , " + chat.getContacts().get(i).getNickname();
			}
		}
		// set sender label
		chatContacts.setText(senders);
		if (chat.getMessages(false) != null && chat.getMessages(false).size() > 0) {
			java.sql.Timestamp timestamp = chat.getMessages(false).get(chat.getMessages(false).size()-1).getTimestamp();
			// timestamp of last message
			chatLastMessage.setText(dateFormat.format(timestamp));
			// chat preview (last message)
			chatContent.setText(chat.getMessages(false).get(chat.getMessages(false).size()-1).getContent().getMessage());
		}
		// set chat title
		chatTitle.setText(chat.getTitle());
		// set chat picture
		imageManager.readImageFrom(chat.getPicture()).ifPresent(imageViewContactProfile::setImage);

	}


}
