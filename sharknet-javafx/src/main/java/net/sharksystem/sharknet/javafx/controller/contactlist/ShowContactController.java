package net.sharksystem.sharknet.javafx.controller.contactlist;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import javax.inject.Inject;
import java.util.Optional;

public class ShowContactController extends AbstractController {

	//@FXML
	//private TextField nameTextField;
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
	}

	public ShowContactController(Contact c, boolean nfc_or_qrcode){
		super(App.class.getResource("views/contactlist/showContactView.fxml"));
		this.contact = c;
		stage = new Stage();
		stage.setTitle("");
		Parent root = super.getRoot();
		stage.setScene(new Scene(root, 600, 400));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();
		editButton.setVisible(false);
		editLabel.setVisible(true);
		saveButton.setText("Kontakt Übernehmen");
	}

	@Override
	protected void onFxmlLoaded() {

		imageManager.readImageFrom(contact.getPicture()).ifPresent(profilePictureImageView::setImage);

		nicknameTextField.setEditable(false);
		//nameTextField.setEditable(false);
		emailTextField.setEditable(false);
		telephoneTextField.setEditable(false);
		infoTextField.setEditable(false);
		publicKeyTextField.setEditable(false);

		nicknameTextField.setText(contact.getNickname());
		//nameTextField.setText(contact.getName());
		nameLabel.setText(contact.getName());
		emailTextField.setText(contact.getEmail());
		//telephoneTextField.setText(testkontakt.getTelephonnumber());
		infoTextField.setText(contact.getNote());
		publicKeyTextField.setText(contact.getPublicKey());

		editButton.setOnMouseClicked(event -> {
			nicknameTextField.setEditable(true);
			//nameTextField.setEditable(true);
			emailTextField.setEditable(true);
			telephoneTextField.setEditable(true);
			editLabel.setVisible(true);
		});

		saveButton.setOnMouseClicked(event -> {
			contact.setNickname(nicknameTextField.getText());
			//contact.setName(nameTextField.getText());
			contact.setEmail(emailTextField.getText());
			stage.close();
		});

		closeWindowButton.setOnMouseClicked(event -> {
			stage.close();
			event.consume();
		});

		deletePublicKeyButton.setOnMouseClicked(event -> {
			contact.deleteKey();
			publicKeyTextField.setText(contact.getPublicKey());
		});

		deleteContactButton.setOnMouseClicked(event -> {

			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Kontakt löschen");
			alert.setHeaderText("Soll " + contact.getNickname() + " wirklich gelöscht werden?");

			ButtonType loeschenOKButton = new ButtonType("Löschen");
			ButtonType abbruchButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(loeschenOKButton, abbruchButton);

			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == loeschenOKButton){
				sharkNetModel.getContacts().remove(contact);

				//TODO changelistener

				stage.close();;

			}
		});

	}

}
