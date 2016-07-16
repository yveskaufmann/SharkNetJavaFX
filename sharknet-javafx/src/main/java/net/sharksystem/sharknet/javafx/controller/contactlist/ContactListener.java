package net.sharksystem.sharknet.javafx.controller.contactlist;

import net.sharksystem.sharknet.api.Contact;

import java.util.List;

/**
 * Created by ich on 08.07.16.
 */
public interface ContactListener {
	void onContactListChanged();
	void onContactDeleted(Contact c);
}
