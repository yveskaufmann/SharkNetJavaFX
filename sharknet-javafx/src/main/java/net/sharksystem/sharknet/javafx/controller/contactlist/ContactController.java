package net.sharksystem.sharknet.javafx.controller.contactlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller(title = "%sidebar.contacts")
public class ContactController extends AbstractController implements ContactListener {

	private ObservableList<Contact> contacts = FXCollections.observableArrayList();
	private ObservableList<Contact> blockedContacts = FXCollections.observableArrayList();
	private FilteredList<Contact> filteredContactsData;
	private FilteredList<Contact> filteredBlacklistData;
	private List<ContactListener> contactListeners;


	public ContactController() {
		super(App.class.getResource("views/contactlist/contactsView.fxml"));
		this.appController = Controllers.getInstance().get(FrontController.class);
		contactListeners = new ArrayList<>();
	}


	private FrontController appController;
	@Inject
	private SharkNet sharkNetModel;
	private ImageManager imageManager;
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
			alert.setHeaderText(blackListView.getSelectionModel().getSelectedItem().getNickname() + " entblocken? ");

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
	public void loadEntries() {
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
		contactListView.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				if (contactListView.getSelectionModel().getSelectedItem() != null) {
					ShowContactController s = new ShowContactController(contactListView.getSelectionModel().getSelectedItem());
					s.addListener(this);
					contactListView.getSelectionModel().clearSelection();
				}
			}
		});

		// Reaktion auf Klick auf Eintag in der Blocklist
		blackListView.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				if (blackListView.getSelectionModel().getSelectedItem() != null) {
					ShowContactController s = new ShowContactController(blackListView.getSelectionModel().getSelectedItem());
					s.addListener(this);
					contactListView.getSelectionModel().clearSelection();
				}
			}
		});
	}


	public void onContactListChanged() {
		loadEntries();
	}

	public void onContactDeleted(Contact c) {
		sharkNetModel.getMyProfile().getBlacklist().remove(c);
		c.delete();
		loadEntries();
	}

}
