package net.sharksystem.sharknet.javafx.controller.settings;

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


public class ChooseRoutingContactsController extends AbstractController {

	//TODO Could not obtain root node??!?!?


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

	public ChooseRoutingContactsController() {
		super(App.class.getResource("views/routingContactsView.fxml"));

		allowedContacts = new ArrayList<>();
		deniedContacts = new ArrayList<>();
		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle("Für Routing zugelassene Kontakte");
		stage.setScene(new Scene(root, 800, 600));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();
	}

	@Override
	protected void onFxmlLoaded() {

		loadContacts();

		allowButton.setOnMouseClicked(event -> {
			onAllowContact(allowedContactsListView.getSelectionModel().getSelectedIndex());
			event.consume();
		});

		denyButton.setOnMouseClicked(event -> {
			onDenyContact(deniedContactsListView.getSelectionModel().getSelectedIndex());
			event.consume();
		});

		okButton.setOnMouseClicked(event -> {
			event.consume();
		});
	}


	private void onAllowContact(int selectedIndex){
		if (selectedIndex >= 0) {
			allowedContactsListView.getItems().add(deniedContacts.get(selectedIndex).getNickname());
			allowedContacts.add(deniedContacts.get(selectedIndex));
			deniedContactsListView.getItems().remove(selectedIndex);
			deniedContacts.remove(selectedIndex);
		}
	}

	private void onDenyContact(int selectedIndex) {
		if (selectedIndex >= 0) {
			deniedContactsListView.getItems().add(allowedContacts.get(selectedIndex).getNickname());
			deniedContacts.add(allowedContacts.get(selectedIndex));
			allowedContactsListView.getItems().remove(selectedIndex);
			allowedContacts.remove(selectedIndex);
		}
	}

	private void loadContacts() {
		allContacts = sharkNetModel.getContacts();
		for (Contact c : allContacts) {
			System.out.println(c.getNickname());
			allowedContacts.add(c);
			allowedContactsListView.getItems().add(c.getNickname());
		}
	}
}

