package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.api.Chat;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

import java.text.SimpleDateFormat;

/**
 * Created by Benni on 31.05.2016.
 */
public class ChatHistoryEntryController extends AbstractController {

	@FXML
	private Text chatContacts;
	@FXML
	private Text chatLastMessage;
	@FXML
	private Text chatTitle;
	@FXML
	private Text chatContent;

	private ObjectProperty<Chat> chatProp;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat();
	private ChatHistoryListCell listCell;
	private ChatHistoryListener historyListener;

	public ChatHistoryEntryController(ChatHistoryListCell chatHistoryListCell) {
		super(App.class.getResource("views/chat/chatHistoryEntry.fxml"));
		listCell = chatHistoryListCell;
		historyListener = null;
	}

	public void setHistoryListener(ChatHistoryListener listener) {
		historyListener = listener;
	}

	@Override
	protected void onFxmlLoaded() {

		listCell.setOnMouseClicked(event -> {
			onCellClick();
			event.consume();
		});
	}

	public ObjectProperty<Chat> chatProperty() {
		if (chatProp == null) {
			chatProp = new SimpleObjectProperty<Chat>(this, "chat") {
				@Override
				protected void invalidated() {
					updateView();
				}
			};
		}
		return chatProp;
	}

	private void updateView() {
		Chat chat = chatProperty().get();
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
		java.sql.Timestamp timestamp = chat.getMessages().get(chat.getMessages().size()-1).getTimestamp();
		chatLastMessage.setText(dateFormat.format(timestamp));
		chatTitle.setText(chat.getTitle());
		chatContent.setText(chat.getMessages().get(chat.getMessages().size()-1).getContent());

		//TODO: Picture

	}

	public void setChat(Chat chat) {
		chatProperty().set(chat);
	}

	private void onCellClick() {
		if (historyListener != null) {
			historyListener.onChatSelected(chatProp.get());
		}
	}
}
