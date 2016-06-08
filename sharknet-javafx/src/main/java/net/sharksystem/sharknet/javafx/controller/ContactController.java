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
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Controller(title = "%sidebar.contacts")
public class ContactController extends AbstractController{

	private FrontController appController;
	@Inject
	private SharkNet sharkNetModel;

	private ImageManager imageManager;

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





		/*
		System.out.println("sharkNetModel.getMyProfile().getContact():");
		System.out.println(sharkNetModel.getMyProfile().getContact().getNickname());
		//sharkNetModel.getContacts()

		System.out.println("Blacklist:");
		System.out.println(sharkNetModel.getMyProfile().getBlacklist().getList());
		*/

		List<Contact> allContacts = sharkNetModel.getContacts();
		List<Contact> allBlockedContacts = sharkNetModel.getMyProfile().getBlacklist().getList();
		//List<ImplContact> allBlockedContacts = new ArrayList<ImplContact>();





		// Kontaktlisten füllen
		for (Contact c : allContacts) {
			contactsData.add(c.getNickname());
		}
		for (Contact c : allBlockedContacts) {
			blackListData.add(c.getNickname());
		}


		contactList.setItems(filteredContacts);
		blackList.setItems(filteredBlocked);
		//contactList.setItems(contactsData);
		//blackList.setItems(blackListData);

		contactList.setCellFactory(listView -> new ListCell<String>() {

			private ImageView imageView = new ImageView();
			//private RoundImageView roundImageView = new RoundImageView();
			@Override
			public void updateItem(String contactName, boolean empty) {
				super.updateItem(contactName, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					//Image image = getImageForContact(contactName);
					//Image profilePicture = new Image(App.class.getResource("images/profile.png").toExternalForm());

					/*
					try{
						//imageManager.readImageFromSync(sharkNetModel.getContacts().get(1).getPicture()).ifPresent(imageView::setImage);
						imageManager.readImageFromSync(sharkNetModel.getContacts().get(1).getPicture().getFile());
					}catch (IOException e){e.printStackTrace();}
					*/
					//imageManager.readImageFromSync(sharkNetModel.getContacts().get(1).getPicture().getFile());
					InputStream inputStream = sharkNetModel.getContacts().get(1).getPicture().getFile();
					Image image = new Image(inputStream);
					imageView.setImage(image);

					//imageView.setImage(profilePicture);
					//roundImageView.setImage(profilePicture);
					setText(contactName);
					setGraphic(imageView);
					//setGraphic(roundImageView);
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
