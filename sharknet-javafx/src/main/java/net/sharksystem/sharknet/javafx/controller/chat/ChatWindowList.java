package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.scene.control.ListView;
import net.sharksystem.sharknet.api.Message;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListView;

/**
 * Created by Benni on 04.06.2016.
 */
public class ChatWindowList extends MediaListView<Message> {

	public ChatWindowList() {
		super();
		setCellFactory(param -> new MediaListCell<Message>(ChatWindowListController.class));
	}
}
