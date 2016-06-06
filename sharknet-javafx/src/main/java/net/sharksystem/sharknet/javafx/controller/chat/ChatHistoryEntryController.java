package net.sharksystem.sharknet.javafx.controller.chat;

import com.google.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.api.Chat;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCellController;
import net.sharksystem.sharknet.javafx.services.ImageManager;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by Benni on 31.05.2016.
 */
public class ChatHistoryEntryController extends MediaListCellController<Chat> {

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

		if (chat.getContacts().size() > 1) {
			for (int i = 1; i < chat.getContacts().size(); i++) {
				senders += " , " + chat.getContacts().get(i).getNickname();
			}
		}

		chatContacts.setText(senders);
		if (chat.getMessages() != null && chat.getMessages().size() > 0) {
			java.sql.Timestamp timestamp = chat.getMessages().get(chat.getMessages().size()-1).getTimestamp();
			chatLastMessage.setText(dateFormat.format(timestamp));
			chatContent.setText(chat.getMessages().get(chat.getMessages().size()-1).getContent().getMessage());
		}

		chatTitle.setText(chat.getTitle());

		try {
			// TODO: image reloading must be avoided
			imageManager.readImageFromSync(chat.getPicture()).ifPresent(imageViewContactProfile::setImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
