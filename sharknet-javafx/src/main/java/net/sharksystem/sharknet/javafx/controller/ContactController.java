package net.sharksystem.sharknet.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.ImplContact;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


@Controller(title = "%sidebar.contacts")
public class ContactController extends AbstractController{

	private FrontController appController;
	@Inject
	private SharkNet sharkNetModel;

	//private ImplContact testKontakt3 = new ImplContact("Jan", "", "");

	public ContactController() {
		super(App.class.getResource("views/contactsView.fxml"));
		this.appController = Controllers.getInstance().get(FrontController.class);


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

		ContactNewController c = new ContactNewController();

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
	private void onContactBlockButtonClick() {
		System.out.println("Kontakt blockieren:");
		System.out.println(contactList.getSelectionModel().getSelectedItem());
	}

	@FXML
	private void onContactUnblockButtonClick(){
		System.out.println("Kontakt wieder entsperrt:");
		System.out.println(blackList.getSelectionModel().getSelectedItem());
	}



	@Override
	protected void onFxmlLoaded() {

		ObservableList<String> contactsData = FXCollections.observableArrayList();
		ObservableList<String> blackListData = FXCollections.observableArrayList();
		FilteredList<String> filteredContacts = new FilteredList<>(contactsData, p -> true);
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


		List<Contact> allContacts = sharkNetModel.getContacts();
		List<ImplContact> allBlockedContacts = new ArrayList<ImplContact>();


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
		blackList.setItems(filteredBlocked);
		//contactList.setItems(contactsData);
		//blackList.setItems(blackListData);

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
