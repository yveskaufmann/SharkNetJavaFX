package net.sharksystem.sharknet.javafx.controller.contactlist;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.ImplContact;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class ContactNewController extends AbstractController {

	@FXML
	private TextField nameInputTextField;
	@FXML
	private TextField mailInputTextField;
	@FXML
	private Button saveButton;
	@FXML
	private Button backButton;
	@FXML
	private Button newContactScanQRButton;
	@FXML
	private Label nfcInfoLabel;

	@Inject
	private SharkNet sharkNetModel;

	private List<Contact> allContacts;
	private List<ContactListener> contactListeners;
	private Stage stage;
	private String uid;
	private String publickey;
	//private String email;
	private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


	public ContactNewController(){
		super(App.class.getResource("views/contactlist/newContactView.fxml"));
		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle("Neuen Kontakt erstellen");
		stage.setScene(new Scene(root, 600, 300));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();
		contactListeners = new ArrayList<>();
	}

	// Erstellt einen neuen Kontakt mit dem von getQRCodeFromCamera() bereitgestellten publickey
	private void scanQRCode(){
		publickey = getQRCodeFromCamera();
		ImplContact newContact = new ImplContact("Neuer Kontakt", uid, publickey, sharkNetModel.getMyProfile());
		stage.close();
		ShowContactController s = new ShowContactController(newContact);

		for (ContactListener cl : contactListeners) {
			cl.onContactListChanged();
		}
	}

	// Diese Methode soll später den von der Kamera erhaltenen QR-Code in einen String umwandeln
	private String getQRCodeFromCamera(){
		//DUMMY
		String qrcode= "<Dummy-Key aus QR-Code>";
		return qrcode;
	}


	// Listener
	public void addListener(ContactListener cl) {
		contactListeners.add(cl);
	}

	@Override
	protected void onFxmlLoaded() {

		// Klick auf Kontakt Speichern
		saveButton.setOnMouseClicked(event -> {
			if(nameInputTextField.getText().length() > 0) {
				Contact newContact = new ImplContact(nameInputTextField.getText(), uid, "<ausstehend>", sharkNetModel.getMyProfile());
				if ((mailInputTextField.getText() != null) && (mailInputTextField.getText() != "")) {
					if (mailInputTextField.getText().matches(EMAIL_PATTERN)) {
						newContact.setEmail(mailInputTextField.getText());
					} else {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Ungültige E-Mail-Adresse");
						alert.setContentText("Die eingegebene E-Mail-Adresse ist ungültig.");
						alert.setHeaderText("");
						alert.showAndWait();
						newContact.delete();
						return;
					}
				}
				sharkNetModel.getContacts().add(newContact);

				for (ContactListener cl : contactListeners) {
					cl.onContactListChanged();
				}
				stage.close();
				event.consume();
			}
		});

		backButton.setOnMouseClicked(event -> {
			stage.close();
			event.consume();
		});

		newContactScanQRButton.setOnMouseClicked(event -> {
			scanQRCode();
		});
	}
}
