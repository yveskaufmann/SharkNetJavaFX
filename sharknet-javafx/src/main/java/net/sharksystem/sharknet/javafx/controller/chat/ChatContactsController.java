package net.sharksystem.sharknet.javafx.controller.chat;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Chat;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import static net.sharksystem.sharknet.javafx.i18n.I18N.getString;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
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
	private JFXButton buttonAdd;
	@FXML
	private JFXButton buttonRemove;
	@FXML
	private JFXButton buttonOk;

	@Inject
	private SharkNet sharkNet;

	// list containing all contacts
	private List<Contact> allContacts;
	// list containing only the contacts you want to add
	private List<Contact> addedContacts;
	private List<ChatListener> listeners;
	private Chat chat;

	private Stage stage;

	public ChatContactsController(Chat chat) {
		super(App.class.getResource("views/chat/chatAddContacts.fxml"));
		addedContacts = new ArrayList<>();
		listeners = new ArrayList<>();
		this.chat = chat;
		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle(getString("chat.contacts.add.title"));
		stage.setScene(new Scene(root, 494, 414));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		InputStream in = App.class.getResourceAsStream("images/shark-icon256x256.png");
		if (in != null) {
			stage.getIcons().add(new Image(in));
		}
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
		listViewAllContacts.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					onAddContact(listViewAllContacts.getSelectionModel().getSelectedIndex());
				}

			}
		});
		listViewAddContacts.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					onRemoveContact(listViewAddContacts.getSelectionModel().getSelectedIndex());
				}

			}
		});
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
	 * load all contacts from sharknet
	 * own contact will be removed
	 */
	private void loadContacts() {
		allContacts = sharkNet.getContacts();
		/*
		// remove own contact
		Iterator<Contact> contactIterator = allContacts.iterator();
		while (contactIterator.hasNext()) {
			Contact contact = contactIterator.next();
			if (contact.isEqual(sharkNet.getMyProfile().getContact())) {
				contactIterator.remove();
			}
		}
		*/
		// remove contacts from allContacts list, which are already in chat
		// check if chat already exist, if not -> new chat
		if (chat != null) {
			for (Contact contact : chat.getContacts()) {
				addedContacts.add(contact);
				listViewAddContacts.getItems().add(contact.getNickname());
				Iterator<Contact> it = allContacts.iterator();
				while (it.hasNext()) {
					Contact tmpContact = it.next();
					if (contact.isEqual(tmpContact)) {
						it.remove();
					}
				}
			}
		}
		// add contacts (own contact excluded) to listview
		for (Contact contact : allContacts) {
			listViewAllContacts.getItems().add(contact.getNickname());
		}
	}

	/**
	 * notify listener about changed contactlist
	 */
	private void onOKClick() {
		if (addedContacts.size() > 0) {
			for (ChatListener c : listeners) {
				c.onContactListChanged(addedContacts);
			}
			stage.close();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(getString("chat.contacts.remove.error.title"));
			alert.setContentText(getString("chat.contacts.remove.error.msg"));
			alert.setHeaderText("");
			alert.showAndWait();
		}

	}

	/**
	 * add listener for contactlist
	 * @param c listener
	 */
	public void addListener(ChatListener c) {
		listeners.add(c);
	}
}
