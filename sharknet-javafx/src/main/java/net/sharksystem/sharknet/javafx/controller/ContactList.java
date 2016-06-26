package net.sharksystem.sharknet.javafx.controller;


import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Feed;
import net.sharksystem.sharknet.javafx.controller.inbox.InboxEntryController;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListView;

public class ContactList extends MediaListView<Contact> {

	public ContactList() {
		super();
		//setStyle("-fx-background-color:red");
		setCellFactory(param -> new MediaListCell<Contact>(ContactListEntryController.class));
	}
}
