package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.ImplSharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

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

	private ImplSharkNet sharkNet;
	private List<Contact> allContacts;
	private List<Contact> addedContacts;
	private ChatContactsListener listener;

	private Stage stage;

	public ChatContactsController() {
		super(App.class.getResource("views/chat/chatAddContacts.fxml"));

		sharkNet = new ImplSharkNet();
		sharkNet.fillWithDummyData();

		addedContacts = new ArrayList<>();
		listener = null;

		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle("Choose the contacts you want to add");
		stage.setScene(new Scene(root, 494, 414));
		stage.getScene().getStylesheets().add(App.class.getResource("style.css").toExternalForm());
		stage.show();


	}

	@Override
	protected void onFxmlLoaded() {
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


		loadContacts();
	}

	private void onAddContact(int selectedIndex) {
		System.out.println("onAddContact");
		if (selectedIndex != -1) {
			listViewAddContacts.getItems().add(allContacts.get(selectedIndex).getNickname());
			addedContacts.add(allContacts.get(selectedIndex));

			listViewAllContacts.getItems().remove(selectedIndex);
			allContacts.remove(selectedIndex);
		}
	}

	private void onRemoveContact(int selectedIndex) {
		System.out.println("onRemoveContact");
		if (selectedIndex != -1) {
			listViewAllContacts.getItems().add(addedContacts.get(selectedIndex).getNickname());
			allContacts.add(addedContacts.get(selectedIndex));

			listViewAddContacts.getItems().remove(selectedIndex);
			addedContacts.remove(selectedIndex);
		}
	}

	private void loadContacts() {
		allContacts = sharkNet.getContacts();

		for (int i = 0; i < allContacts.size(); i++) {
			listViewAllContacts.getItems().add(allContacts.get(i).getNickname());
		}
	}

	private void onOKClick() {
		if (listener != null) {
			listener.onContactListChanged(addedContacts);
		}
		stage.close();
	}

	public void setContactListListener(ChatContactsListener c) {
		listener = c;
	}
}
