package net.sharksystem.sharknet.javafx.controller.chat;

import net.sharksystem.sharknet.api.Chat;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListView;

/**
 * Created by Benni on 31.05.2016.
 */
public class ChatHistoryList extends MediaListView<Chat> {

	public ChatHistoryList() {
		super();
		setCellFactory(param -> new MediaListCell<Chat>(ChatHistoryEntryController.class));
	}
}
