package net.sharksystem.sharknet.javafx.controller.settings;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.ImplContact;
import net.sharksystem.sharknet.api.Setting;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import java.util.ArrayList;
import java.util.List;


public class ChooseRoutingContactsController extends AbstractController {

	@FXML
	private ListView allowedContactsListView;
	@FXML
	private ListView deniedContactsListView;
	@FXML
	private Button allowButton;
	@FXML
	private Button denyButton;
	@FXML
	private Button okButton;

	@Inject
	private SharkNet sharkNetModel;

	private List<Contact> allContacts;
	private List<Contact> allowedContacts;
	private List<Contact> deniedContacts;
	private Stage stage;
	private Setting settings;

	public ChooseRoutingContactsController() {
		super(App.class.getResource("views/routingContactsView.fxml"));
		allowedContacts = new ArrayList<>();
		deniedContacts = new ArrayList<>();
		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle("Fürs Routing zugelassene Kontakte");
		stage.setScene(new Scene(root, 600, 400));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();
	}

	@Override
	protected void onFxmlLoaded() {
		this.settings = sharkNetModel.getMyProfile().getSettings();

		loadContacts();

		allowButton.setOnMouseClicked(event -> {
			onAllowContact(deniedContactsListView.getSelectionModel().getSelectedIndex());
			event.consume();
		});

		denyButton.setOnMouseClicked(event -> {
			onDenyContact(allowedContactsListView.getSelectionModel().getSelectedIndex());
			event.consume();
		});

		okButton.setOnMouseClicked(event -> {
			event.consume();
			stage.close();
		});
	}


	private void onAllowContact(int selectedIndex){
		if (selectedIndex >= 0) {
			System.out.println("onAllowContact INDEX: "+ selectedIndex);
			allowedContactsListView.getItems().add(deniedContacts.get(selectedIndex).getNickname());
			allowedContacts.add(deniedContacts.get(selectedIndex));

			deniedContactsListView.getItems().remove(selectedIndex);
			deniedContacts.remove(selectedIndex);
		}
	}

	private void onDenyContact(int selectedIndex) {
		if (selectedIndex >= 0) {
			System.out.println("DENY: " + selectedIndex);
			deniedContacts.add(allowedContacts.get(selectedIndex));
			deniedContactsListView.getItems().add(allowedContacts.get(selectedIndex).getNickname());

			allowedContactsListView.getItems().remove(selectedIndex);
			allowedContacts.remove(selectedIndex);
		}
	}

	private void loadContacts() {
		allContacts = sharkNetModel.getContacts();
		allContacts.addAll(sharkNetModel.getMyProfile().getBlacklist().getList());
		allowedContacts = settings.getRoutingContacts();

		// Daten von diesen Kontakten weiterleiten
		for (Contact c : allContacts) {

			if (allowedContacts.contains(c)) {
				allowedContactsListView.getItems().add(c.getNickname());
			}
			else{
				deniedContacts.add(c);
				deniedContactsListView.getItems().add(c.getNickname());
			}

		}
	}
}

