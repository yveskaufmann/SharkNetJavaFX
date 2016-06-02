package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.api.Chat;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCellController;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListView;

import java.text.SimpleDateFormat;

/**
 * Created by Benni on 31.05.2016.
 */
public class ChatHistoryEntryController extends MediaListCellController<Chat> {

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
		java.sql.Timestamp timestamp = chat.getMessages().get(chat.getMessages().size()-1).getTimestamp();
		chatLastMessage.setText(dateFormat.format(timestamp));
		chatTitle.setText(chat.getTitle());
		chatContent.setText(chat.getMessages().get(chat.getMessages().size()-1).getContent());

		//TODO: Picture
	}

	/**
	 *
	 * TODO: NOTE: this is not really necessary the list-view and the list-cell does
	 * this already. The list view provides a {@link MediaListView#getSelectionModel()} which
	 * enables you to observe the selected item of the view.
	 *
	 * For example your chatController can invoke in {@link ChatController#onFxmlLoaded()}
	 * the following snippet to achieve the same:
	 *
	 * <pre>
	 * chatHistoryListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	 * chatHistoryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	 *     loadChatHistory();
	 * });
	 * chatHistoryListView.getSelectionModel().selectFirst();
	 * </pre>
	 *
	 * After that you doesn't need the ChatHistoryListener for this purpose of selections.
	 */

}
