package net.sharksystem.sharknet.javafx.controller.chat;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benni on 01.06.2016.
 */

public class ChatContactsController extends AbstractController {

	@FXML
	private ListView listViewAllContacts;
	@FXML
	private ListView listViewAddContacts;
	@FXML
	private Button buttonAdd;
	@FXML
	private Button buttonRemove;
	@FXML
	private Button buttonOk;

	@Inject
	private SharkNet sharkNet;

	// list containing all contacts
	private List<Contact> allContacts;
	// list containing only the contacts you want to add
	private List<Contact> addedContacts;
	private ChatListener listener;

	private Stage stage;

	public ChatContactsController() {
		super(App.class.getResource("views/chat/chatAddContacts.fxml"));
		addedContacts = new ArrayList<>();
		listener = null;

		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle("Choose the contacts you want to add");
		stage.setScene(new Scene(root, 494, 414));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();
	}

	@Override
	protected void onFxmlLoaded() {
		// mouseclick events for all buttons
		buttonAdd.setOnMouseClicked(event -> {
			onAddContact(listViewAllContacts.getSelectionModel().getSelectedIndex());
			event.consume();
		});

		buttonRemove.setOnMouseClicked(event -> {
			onRemoveContact(listViewAddContacts.getSelectionModel().getSelectedIndex());
			event.consume();
		});

		buttonOk.setOnMouseClicked(event -> {
			onOKClick();
			event.consume();
		});

		// initial load of contacts
		loadContacts();
	}

	/**
	 * Add contact to list
	 * triggered by ">>" Button
	 * @param selectedIndex listindex
	 */
	private void onAddContact(int selectedIndex) {
		System.out.println("onAddContact");
		// if an item is selected
		if (selectedIndex != -1) {
			// add selected item to addcontact listview
			listViewAddContacts.getItems().add(allContacts.get(selectedIndex).getNickname());
			// add selected item to addedcontact list
			addedContacts.add(allContacts.get(selectedIndex));
			// remove selected item from all contact listview
			listViewAllContacts.getItems().remove(selectedIndex);
			// remove selected item from allcontacts list
			allContacts.remove(selectedIndex);
		}
	}

	/**
	 * remove contact from list
	 * triggered by "<<" button
	 * @param selectedIndex listindex
	 */
	private void onRemoveContact(int selectedIndex) {
		System.out.println("onRemoveContact");
		// analog to onAddContact, just inverted...
		if (selectedIndex != -1) {
			listViewAllContacts.getItems().add(addedContacts.get(selectedIndex).getNickname());
			allContacts.add(addedContacts.get(selectedIndex));
			listViewAddContacts.getItems().remove(selectedIndex);
			addedContacts.remove(selectedIndex);
		}
	}

	/**
	 * load all contacts from sharknet, no check if contact is equal to own profile
	 */
	private void loadContacts() {
		allContacts = sharkNet.getContacts();

		for (int i = 0; i < allContacts.size(); i++) {
			listViewAllContacts.getItems().add(allContacts.get(i).getNickname());
		}
	}

	/**
	 * notify listener about changed contactlist
	 */
	private void onOKClick() {
		if (listener != null) {
			listener.onContactListChanged(addedContacts);
		}
		stage.close();
	}

	/**
	 * set listener for contactlist
	 * @param c listener
	 */
	public void setContactListListener(ChatListener c) {
		listener = c;
	}
}
