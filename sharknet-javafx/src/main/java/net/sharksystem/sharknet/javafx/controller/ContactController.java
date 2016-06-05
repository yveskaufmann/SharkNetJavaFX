package net.sharksystem.sharknet.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.List;


@Controller(title = "%sidebar.contacts")
public class ContactController extends AbstractController{

	private FrontController appController;

	private ImplSharkNet implSharkNet;
	//private ImplContact testKontakt3 = new ImplContact("Jan", "", "");

	public ContactController() {
		super(App.class.getResource("views/contactsView.fxml"));
		this.appController = Controllers.getInstance().get(FrontController.class);

		implSharkNet = new ImplSharkNet();
		implSharkNet.fillWithDummyData();
	}


	@FXML
	private ListView<String> contactList;
	@FXML
	private ListView<String> groupList;
	@FXML
	private ListView<String> blackList;
	@FXML
	private TextField contactsSearchTextfield;
	@FXML
	private TextField groupsSearchTextfield;
	@FXML
	private TextField blacklistSearchTextfield;


	@FXML
	private void onNewContactButtonClick() {
		System.out.println("Neuen Kontakt erstellen");
		//appController.getChildren().clear();
		//appController.getChildren().add(FXMLLoader.load(getClass().getResource("Content2.fxml"));
	}

	@FXML
	private void onContactDeleteButtonClick() {
		System.out.println("Kontakt löschen:");
		System.out.println(contactList.getSelectionModel().getSelectedItem());
		//testKontakt.deleteContact();
	}

	@FXML
	private void onContactGroupButtonClick() {
		System.out.println("Kontakt zu Gruppe:");
		System.out.println(contactList.getSelectionModel().getSelectedItem());
	}

	@FXML
	private void onContactBlockButtonClick() {
		System.out.println("Kontakt blockieren:");
		System.out.println(contactList.getSelectionModel().getSelectedItem());
	}

	@FXML
	private void onContactUnblockButtonClick(){
		System.out.println("Kontakt wieder entsperrt:");
		System.out.println(blackList.getSelectionModel().getSelectedItem());
	}


	//
	Image getImageForContact(String contactName){

		return null;
	}
	//


	@Override
	protected void onFxmlLoaded() {

		ObservableList<String> contactsData = FXCollections.observableArrayList();
		ObservableList<String> blackListData = FXCollections.observableArrayList();
		ObservableList<String> groupsData = FXCollections.observableArrayList("Gruppe1","Gruppe2", "Gruppe3");
		FilteredList<String> filteredContacts = new FilteredList<>(contactsData, p -> true);
		FilteredList<String> filteredGroups = new FilteredList<>(groupsData, p -> true);
		FilteredList<String> filteredBlocked = new FilteredList<>(blackListData, p -> true);

		//TEST Suchfunktion
		// Kontaktliste filtern
		contactsSearchTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredContacts.setPredicate(nickname -> {
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

		// Gruppen filtern
		groupsSearchTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredGroups.setPredicate(nickname -> {
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


		//Mit Testwerten füllen
		List<Contact> allContacts = implSharkNet.getContacts();
		//List<ImplContact> allContacts = new ArrayList<ImplContact>();
		List<ImplContact> allBlockedContacts = new ArrayList<ImplContact>();
		//List<ImplGroup> allGroups = new ArrayList<ImplGroup>();


		//allBlockedContacts.add(testKontakt3);



		// Kontaktlisten füllen
		for (Contact c : allContacts) {
			contactsData.add(c.getNickname());
		}

		/* Kontaktlisten füllen
		for (Contact c : allContacts) {
			System.out.println(c.getNickname());
			contactsData.add(c.getNickname());
		}
		*/

		for (ImplContact ic : allBlockedContacts) {
			blackListData.add(ic.getNickname());
		}


		contactList.setItems(filteredContacts);
		groupList.setItems(filteredGroups);
		blackList.setItems(filteredBlocked);
		//contactList.setItems(contactsData);
		//blackList.setItems(blackListData);
		//groupList.setItems(groupsData);

		contactList.setCellFactory(listView -> new ListCell<String>() {
			private ImageView imageView = new ImageView();
			@Override
			public void updateItem(String contactName, boolean empty) {
				super.updateItem(contactName, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					//Image image = getImageForContact(contactName);
					Image profilePicture = new Image(App.class.getResource("images/profile.png").toExternalForm());
					imageView.setImage(profilePicture);
					setText(contactName);
					setGraphic(imageView);
				}
			}
		});


		// TODO Gruppenbild
		/*
		groupList.setCellFactory(listView -> new ListCell<String>() {
			private ImageView imageView = new ImageView();
			@Override
			public void updateItem(String contactName, boolean empty) {
				super.updateItem(contactName, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					//Image image = getImageForContact(contactName);
					Image profilePicture = new Image(App.class.getResource("images/profile.png").toExternalForm());
					imageView.setImage(profilePicture);
					setText(contactName);
					setGraphic(imageView);
				}
			}
		});
		*/

		blackList.setCellFactory(listView -> new ListCell<String>() {
			private ImageView imageView = new ImageView();
			@Override
			public void updateItem(String contactName, boolean empty) {
				super.updateItem(contactName, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					//Image image = getImageForContact(contactName);
					Image profilePicture = new Image(App.class.getResource("images/profile.png").toExternalForm());
					imageView.setImage(profilePicture);
					setText(contactName);
					setGraphic(imageView);
				}
			}
		});
	}
}
