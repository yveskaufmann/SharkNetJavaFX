package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.scene.control.ListCell;
import net.sharksystem.sharknet.api.Chat;

/**
 * Created by Benni on 31.05.2016.
 */
public class ChatHistoryListCell extends ListCell<Chat> {

	private ChatHistoryEntryController entryController;

	public ChatHistoryListCell() {
		super();
		getStyleClass().add("inbox-item");
	}

	@Override
	protected void updateItem(Chat item, boolean empty) {
		super.updateItem(item, empty);

		if (item == null || empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (entryController == null) {
				entryController = new ChatHistoryEntryController(this);
				entryController.setHistoryListener(ChatController.getInstance());
			}
			setGraphic(entryController.getRoot());
			entryController.setChat(item);
		}
	}


}
