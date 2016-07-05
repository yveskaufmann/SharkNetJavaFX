package net.sharksystem.sharknet.javafx.controller.inbox;


import net.sharksystem.sharknet.api.Feed;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListView;

public class InboxList extends MediaListView<Feed> {

	public InboxList() {
		super();
		setCellFactory(param -> new MediaListCell<Feed>(InboxEntryController.class));
	}
}
