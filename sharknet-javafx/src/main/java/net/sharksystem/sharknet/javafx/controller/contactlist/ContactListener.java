package net.sharksystem.sharknet.javafx.controller.contactlist;

import net.sharksystem.sharknet.api.Contact;

/******************************************************************************
 *
 * Listener-Interface zum aktualisieren der Kontaktliste bei verändern/löschen
 * von Kontakten.
 *
 ******************************************************************************/

public interface ContactListener {
	void onContactListChanged();
	void onContactDeleted(Contact c);
}
