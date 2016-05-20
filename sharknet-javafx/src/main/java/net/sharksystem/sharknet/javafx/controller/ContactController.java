package net.sharksystem.sharknet.javafx.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import net.sharksystem.sharknet.api.ImplContact;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.AbstractController;
import javafx.fxml.FXML;




@Controller( title = "%sidebar.contacts")
public class ContactController extends AbstractController {

	private AppController appController;
	private ImplContact contact;

	public ContactController(AppController appController) {
		super(App.class.getResource("views/contactsView.fxml"));
		this.appController = appController;
		this.contact = new ImplContact("Max","0000","asdf");
	}


	@FXML
	private ListView<String> contactList;

	@FXML
	private ListView<String> groupList;

	@FXML
	private ListView<String> blackList;


	@FXML
	private void onNewContactButtonClick() {
		System.out.println("Neuen Kontakt erstellen");
	}

	@FXML
	private void onContactDeleteButtonClick() {
		System.out.println("Neuen Kontakt erstellen");
	}

	@FXML
	private void onContactGroupButtonClick() {
		System.out.println("Neuen Kontakt erstellen");
	}

	@FXML
	private void onContactBlockButtonClick() {
		System.out.println("Neuen Kontakt erstellen");
	}



	@Override
	protected void onFxmlLoaded() {

		ObservableList<String> contactsData = FXCollections.observableArrayList("Sarah","Peter", "Max");
		contactList.setItems(contactsData);

		ObservableList<String> groupsData = FXCollections.observableArrayList("Gruppe1","Gruppe2", "Gruppe3");
		groupList.setItems(groupsData);

		ObservableList<String> blackListData = FXCollections.observableArrayList("Blockiert1","Blockiert2", "Blockiert3");
		blackList.setItems(blackListData);
	}
}
