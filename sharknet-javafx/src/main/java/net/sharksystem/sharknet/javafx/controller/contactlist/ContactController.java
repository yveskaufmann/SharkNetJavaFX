package net.sharksystem.sharknet.javafx.controller.contactlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import javax.inject.Inject;
import java.util.Optional;

/******************************************************************************
 *
 * Dieser Controller kümmert sich um Kontaktliste und Blacklist.
 * Für die einzelnen Einträge ist der ContactListEntryControler verantwortlich.
 * Zugehörige View: contactsView.fxml
 *
 ******************************************************************************/


@Controller(title = "%sidebar.contacts")
public class ContactController extends AbstractController implements ContactListener {

	private ObservableList<Contact> contacts = FXCollections.observableArrayList();
	private ObservableList<Contact> blockedContacts = FXCollections.observableArrayList();
	private FilteredList<Contact> filteredContactsData;
	private FilteredList<Contact> filteredBlacklistData;

	public ContactController() {
		super(App.class.getResource("views/contactlist/contactsView.fxml"));
		this.appController = Controllers.getInstance().get(FrontController.class);
	}

	private FrontController appController;
	@Inject
	private SharkNet sharkNetModel;
	@FXML
	private ContactList contactListView;
	@FXML
	private ContactList blackListView;
	@FXML
	private TextField contactsSearchTextfield;
	@FXML
	private TextField blacklistSearchTextfield;
	@FXML
	private Button contactDeleteButton;

	// Klick auf Neuer Kontakt-Button
	@FXML
	private void onNewContactButtonClick() {
		ContactNewController c = new ContactNewController();
		c.addListener(this);
	}

	// Klick auf Kontakt-löschen-Button
	@FXML
	private void onContactDeleteButtonClick() {
		int indexContactList = contactListView.getSelectionModel().getSelectedIndex();
		if (indexContactList >= 0) {
			// Löschdialog
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Kontakt löschen");
			alert.setHeaderText("Soll " + contactListView.getSelectionModel().getSelectedItem().getNickname() + " wirklich gelöscht werden?");

			ButtonType loeschenOKButton = new ButtonType("Löschen");
			ButtonType abbruchButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(loeschenOKButton, abbruchButton);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == loeschenOKButton) {
				onContactDeleted(sharkNetModel.getContacts().get(indexContactList));
			}
		}
	}

	// Klick auf Kontakt-blockieren-Button
	@FXML
	private void onContactBlockButtonClick() {
		int selectedIndex = contactListView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {

			// Blockieren-Dialog
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Kontakt blockieren");
			alert.setHeaderText(contactListView.getSelectionModel().getSelectedItem().getNickname() + " blockieren? ");
			ButtonType blockierenButton = new ButtonType("Blockieren");
			ButtonType abbruchButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(blockierenButton, abbruchButton);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == blockierenButton) {
				Contact c = sharkNetModel.getContacts().get(selectedIndex);
				sharkNetModel.getMyProfile().getBlacklist().add(c);
				sharkNetModel.getContacts().remove(c);
				loadEntries();
			}
		}
	}

	// Klick auf Kontakt-entsperren-Button
	@FXML
	private void onContactUnblockButtonClick() {
		int selectedIndex = blackListView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Blockierung aufhaben?");
			alert.setHeaderText("Blockierung für " + blackListView.getSelectionModel().getSelectedItem().getNickname() + " aufheben? ");

			ButtonType entblockenButton = new ButtonType("Entblocken");
			ButtonType abbruchButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(entblockenButton, abbruchButton);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == entblockenButton) {
				Contact c = sharkNetModel.getMyProfile().getBlacklist().getList().get(selectedIndex);
				sharkNetModel.getContacts().add(c);
				sharkNetModel.getMyProfile().getBlacklist().remove(c);

				onContactListChanged();
			}
		}
	}


	// Kontakt- und Blacklist laden
	private void loadEntries() {
		contacts.clear();
		blockedContacts.clear();

		// Kontaktliste laden
		if (sharkNetModel.getContacts() != null) {
			for (Contact c : sharkNetModel.getContacts()) {
				if (sharkNetModel.getMyProfile().getBlacklist().getList() == null) {
					contacts.add(c);
				} else if (!sharkNetModel.getMyProfile().getBlacklist().getList().contains(c)) {
					contacts.add(c);
				}
			}
		}
		// Blacklist laden
		if (sharkNetModel.getMyProfile().getBlacklist().getList() != null) {
			for (Contact c : sharkNetModel.getMyProfile().getBlacklist().getList()) {
				blockedContacts.add(c);
			}
		}
		
		filteredContactsData = new FilteredList<>(contacts, s -> true);
		filteredBlacklistData = new FilteredList<>(blockedContacts, s -> true);
		contactListView.getItems().setAll(filteredContactsData);
		blackListView.getItems().setAll(filteredBlacklistData);


		// Auf Eingaben in die Suchfelder reagieren
		contactsSearchTextfield.textProperty().addListener(observable -> {
			String filter = contactsSearchTextfield.getText();
			if (filter == null || filter.length() == 0) {
				filteredContactsData.setPredicate(s -> true);
				contactListView.getItems().setAll(filteredContactsData);
			} else {
				filteredContactsData.setPredicate(s -> s.getNickname().contains(filter));
				contactListView.getItems().setAll(filteredContactsData);
			}
		});
		blacklistSearchTextfield.textProperty().addListener(observable -> {
			String filter = blacklistSearchTextfield.getText();
			if (filter == null || filter.length() == 0) {
				filteredBlacklistData.setPredicate(s -> true);
				blackListView.getItems().setAll(filteredBlacklistData);

			} else {
				filteredBlacklistData.setPredicate(s -> s.getNickname().contains(filter));
				blackListView.getItems().setAll(filteredBlacklistData);
			}
		});
	}

	@Override
	public void onResume() {
		// Will be triggered before this view becomes visible,
		// which ensures that the data is reloaded when the user
		// re/opens the contact list.
		loadEntries();
	}

	@Override
	protected void onFxmlLoaded() {
		// Reaktion auf Klick auf Eintag in der Kontaktliste
		contactListView.setOnMouseClicked(this::onContactClicked);
		blackListView.setOnMouseClicked(this::onContactClicked);
	}

	private void onContactClicked(MouseEvent e) {

		if (e.getButton() == MouseButton.PRIMARY) {

			final Object source = e.getSource();
			if (source instanceof ContactList) {
				final ContactList contactList = (ContactList) source;
				Contact selectedContact = contactList.getSelectionModel().getSelectedItem();
				if (selectedContact != null) {
					showContact(selectedContact, null);
					contactList.getSelectionModel().clearSelection();
				}
			}
		}
	}

	/******************************************************************************
	 *
	 * Public Api
	 *
	 ******************************************************************************/

	public void showContact(Contact contact, Runnable onContactsChanged) {
		ShowContactController s = new ShowContactController(contact);
		s.addListener(this);
		s.addListener(new ContactListener() {
			@Override
			public void onContactListChanged() {
				if (onContactsChanged != null) onContactsChanged.run();
			}

			@Override
			public void onContactDeleted(Contact c) {
				if (onContactsChanged != null) onContactsChanged.run();
			}
		});
	}

	// Listener-Implementierung
	public void onContactListChanged() {
		loadEntries();
	}
	public void onContactDeleted(Contact c) {
		sharkNetModel.getMyProfile().getBlacklist().remove(c);
		c.delete();
		loadEntries();
	}
}
