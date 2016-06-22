package net.sharksystem.sharknet.javafx.controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import javax.inject.Inject;
import java.util.Optional;

@Controller(title = "%sidebar.contacts")
public class ContactController extends AbstractController{

	public ContactController() {
		super(App.class.getResource("views/contactsView.fxml"));
		this.appController = Controllers.getInstance().get(FrontController.class);
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

	//List<Contact> allContacts;
	//List<Contact> allBlockedContacts;
	//ObservableList<String> contactsData;
	//ObservableList<String> blackListData;
	//FilteredList<String> filteredContacts;
	//FilteredList<String> filteredBlocked;

	@FXML
	private void onNewContactButtonClick() {
		System.out.println("Neuen Kontakt erstellen");
		ContactNewController c = new ContactNewController();
	}


	@FXML
	private void onContactDeleteButtonClick() {
		int index = contactListView.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			// Löschdialog
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Kontakt löschen");
			alert.setHeaderText("Soll " + contactListView.getSelectionModel().getSelectedItem().getNickname() + " wirklich gelöscht werden?");

			ButtonType loeschenOKButton = new ButtonType("Löschen");
			ButtonType abbruchButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(loeschenOKButton, abbruchButton);

			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == loeschenOKButton){
				System.out.println("Kontakt löschen:");
				System.out.println(contactListView.getSelectionModel().getSelectedItem().getNickname());
				System.out.println("Index: " + index);
				sharkNetModel.getContacts().get(index).delete();
				loadEntries();
			}
		}
	}



	@FXML
	private void onContactBlockButtonClick() {
		int selectedIndex = contactListView.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0) {
			System.out.println("Index: " + selectedIndex);

			// Blockieren-Dialog
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Kontakt blockieren");
			alert.setHeaderText(contactListView.getSelectionModel().getSelectedItem().getNickname() + " blockieren? ");

			ButtonType blockierenButton = new ButtonType("Blockieren");
			ButtonType abbruchButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(blockierenButton, abbruchButton);

			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == blockierenButton){
				System.out.println("Kontakt blockieren:");
				System.out.println(contactListView.getSelectionModel().getSelectedItem().getNickname());
				sharkNetModel.getMyProfile().getBlacklist().add(sharkNetModel.getContacts().get(selectedIndex));
				sharkNetModel.getContacts().get(selectedIndex).delete();
				loadEntries();
			}
		}
	}

	@FXML
	private void onContactUnblockButtonClick(){
		int selectedIndex = blackListView.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Blockierung aufhaben?");
			alert.setHeaderText(blackListView.getSelectionModel().getSelectedItem().getNickname() + " entblocken? ");

			ButtonType entblockenButton = new ButtonType("Entblocken");
			ButtonType abbruchButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(entblockenButton, abbruchButton);

			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == entblockenButton){
				System.out.println("Kontakt entblocken:");
				System.out.println(blackListView.getSelectionModel().getSelectedItem().getNickname());
				System.out.println("Index: " + selectedIndex);

				Contact tempContact = sharkNetModel.getMyProfile().getBlacklist().getList().get(selectedIndex);
				sharkNetModel.getContacts().add(tempContact);

				//System.out.println("tempcontact =  " + tempContact.getNickname());

				/*if(sharkNetModel.getContacts().contains(tempContact)){
					System.out.println("tempcontact in contacts angekommen");
				}*/

				sharkNetModel.getMyProfile().getBlacklist().remove(sharkNetModel.getMyProfile().getBlacklist().getList().get(selectedIndex));
				loadEntries();
			}
		}
	}



	public void loadEntries() {
		contactListView.getItems().clear();
		blackListView.getItems().clear();

		for(Contact contact : sharkNetModel.getContacts()) {
			if(!sharkNetModel.getMyProfile().getBlacklist().getList().contains(contact)){
				contactListView.getItems().add(contact);
				System.out.println(contact.getNickname());
			}
		}
		for(Contact contact : sharkNetModel.getMyProfile().getBlacklist().getList()) {
			blackListView.getItems().add(contact);
		}

	}




	/*
	private void loadContactAndBlacklist(){
		allContacts = sharkNetModel.getContacts();
		allBlockedContacts = sharkNetModel.getMyProfile().getBlacklist().getList();

		contactsData = FXCollections.observableArrayList();
		blackListData = FXCollections.observableArrayList();
		filteredContacts = new FilteredList<>(contactsData, p -> true);
		filteredBlocked = new FilteredList<>(blackListData, p -> true);

		contactListView.setItems(filteredContacts);
		blackListView.setItems(filteredBlocked);


		// Blacklist filtern

		blacklistSearchTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredBlocked.setPredicate(nickname -> {
				// If filter text is empty, display all entries.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (nickname.toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches first name.
				}
				return false; // Does not match.
			});
		});

	}*/

	@Override
	protected void onFxmlLoaded() {
		loadEntries();



	}
}
