package net.sharksystem.sharknet.javafx.controller.inbox;


import net.sharksystem.sharknet.api.Feed;
import net.sharksystem.sharknet.javafx.controlls.MediaListView;

public class InboxList extends MediaListView<Feed> {
	public InboxList() {
		super();
		setCellFactory(param -> new InboxListCell());
	}
}
