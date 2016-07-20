package net.sharksystem.sharknet.javafx.controller.contactlist;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/******************************************************************************
 *
 * Dieser Controller kümmert sich das Anzeigen und Bearbeiten eines Kontaktes.
 * Zugehörige View: showContactView.fxml.
 *
 ******************************************************************************/

public class ShowContactController extends AbstractController {

	@FXML
	private TextField nicknameTextField;
	@FXML
	private TextField emailTextField;
	@FXML
	private TextField telephoneTextField;
	@FXML
	private TextArea infoTextField;
	@FXML
	private TextArea publicKeyTextField;
	@FXML
	private Button editButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button closeWindowButton;
	@FXML
	private Button deletePublicKeyButton;
	@FXML
	private Button deleteContactButton;
	@FXML
	private RoundImageView profilePictureImageView;
	@FXML
	private Label nameLabel;
	@FXML
	private Label editLabel;
	@FXML
	private ListView interestsListView;

	@Inject
	private ImageManager imageManager;

	@Inject
	private SharkNet sharkNetModel;

	private Contact contact;
	private Stage stage;
	private List<ContactListener> contactListeners;
	private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


	public ShowContactController(Contact c){
		super(App.class.getResource("views/contactlist/showContactView.fxml"));
		this.contact = c;
		stage = new Stage();
		stage.setTitle("");
		Parent root = super.getRoot();
		stage.setScene(new Scene(root, 800, 600));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();
		editLabel.setVisible(false);
		contactListeners = new ArrayList<>();
	}

	// listener
	public void addListener(ContactListener cl) {
		contactListeners.add(cl);
	}

	@Override
	protected void onFxmlLoaded() {

		imageManager.readImageFrom(contact.getPicture()).ifPresent(profilePictureImageView::setImage);

		// Interessen in Ansicht laden
		if(contact.getInterests().getAllTopics().isEmpty()){
			interestsListView.getItems().add("Keine Interessen vorhanden.");
		}
		for (SemanticTag s : contact.getInterests().getAllTopics()) {
			interestsListView.getItems().add(s.getName());
		}

		nicknameTextField.setEditable(false);
		emailTextField.setEditable(false);
		telephoneTextField.setEditable(false);
		infoTextField.setEditable(false);
		publicKeyTextField.setEditable(false);

		nicknameTextField.setText(contact.getNickname());
		nameLabel.setText(contact.getName());
		emailTextField.setText(contact.getEmail());
		infoTextField.setText(contact.getNote());
		publicKeyTextField.setText(contact.getPublicKey());

		if(!contact.getTelephonnumber().isEmpty()){
			telephoneTextField.setText("" + contact.getTelephonnumber().get(0));
		}



		editButton.setOnMouseClicked(event -> {
			nicknameTextField.setEditable(true);
			emailTextField.setEditable(true);
			telephoneTextField.setEditable(true);
			editLabel.setVisible(true);
		});

		saveButton.setOnMouseClicked(event -> {
			contact.setNickname(nicknameTextField.getText());

			// Prüfen der Mailadresse
			if (emailTextField.getText() != null) {
				if(emailTextField.getText().matches(EMAIL_PATTERN)) {
					contact.setEmail(emailTextField.getText());
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Ungültige E-Mail-Adresse");
					alert.setContentText("Die eingegebene E-Mail-Adresse ist ungültig.");
					alert.setHeaderText("");
					alert.showAndWait();
					return;
				}
			}

			contact.getTelephonnumber().clear();
			contact.addTelephonnumber(telephoneTextField.getText());

			// Kontaktliste aktualisieren
			for (ContactListener cl : contactListeners) {
				cl.onContactListChanged();
			}
			stage.close();
		});

		// Public Key löschen-Button
		deletePublicKeyButton.setOnMouseClicked(event -> {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Public Key löschen");
			alert.setHeaderText("Soll der Public Key von " + contact.getNickname() + " wirklich gelöscht werden?");

			ButtonType loeschenOKButton = new ButtonType("Löschen");
			ButtonType abbruchButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(loeschenOKButton, abbruchButton);

			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == loeschenOKButton){
				contact.deleteKey();
				publicKeyTextField.setText(contact.getPublicKey());
			}

		});

		// Kontakt löschen-Button
		deleteContactButton.setOnMouseClicked(event -> {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Kontakt löschen");
			alert.setHeaderText("Soll " + contact.getNickname() + " wirklich gelöscht werden?");

			ButtonType loeschenOKButton = new ButtonType("Löschen");
			ButtonType abbruchButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(loeschenOKButton, abbruchButton);

			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == loeschenOKButton){
				for (ContactListener cl : contactListeners) {
					cl.onContactDeleted(contact);
				}
				stage.close();
			}
		});

		closeWindowButton.setOnMouseClicked(event -> {
			stage.close();
			event.consume();
		});

	}

}
