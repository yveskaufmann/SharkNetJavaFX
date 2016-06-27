package net.sharksystem.sharknet.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.ImplContact;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import javax.inject.Inject;
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
	private Button newContactNFCButton;

	@Inject
	private SharkNet sharkNetModel;

	//@Inject
	//private ContactController contactController;

	private List<Contact> allContacts;
	private Stage stage;
	private String uid;
	private String publickey;
	//private String email;

	public ContactNewController(){
		super(App.class.getResource("views/newContactView.fxml"));
		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle("Neuen Kontakt erstellen");
		stage.setScene(new Scene(root, 600, 400));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();
	}


	private void scanQRCode(){
		//TODO
		publickey = "TESTKEY";
		System.out.println("QR-Code scannen: " + publickey);
		ImplContact newContact = new ImplContact("", uid, publickey, sharkNetModel.getMyProfile());
	}

	private void contactExchangeNFC(){}

	@Override
	protected void onFxmlLoaded() {

		saveButton.setOnMouseClicked(event -> {
			if(nameInputTextField.getText().length() > 0){
				System.out.println("Neuen Kontakt erstellen: " + nameInputTextField.getText());
				ImplContact newContact = new ImplContact(nameInputTextField.getText(), uid, publickey, sharkNetModel.getMyProfile());
				if(mailInputTextField.getText() != "") {
					newContact.setEmail(mailInputTextField.getText());
				}
				sharkNetModel.getContacts().add(newContact);
				stage.close();
				event.consume();
				//contactController.loadEntries();
			}
		});

		backButton.setOnMouseClicked(event -> {
			stage.close();
			event.consume();
		});

		newContactScanQRButton.setOnMouseClicked(event -> {
			scanQRCode();
		});

		newContactNFCButton.setOnMouseClicked(event -> {
			contactExchangeNFC();
		});
	}
}
