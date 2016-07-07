package net.sharksystem.sharknet.javafx.controller.contactlist;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
	private TextArea publicKeyTextArea;
	@FXML
	private Button saveButton;
	@FXML
	private Button backButton;
	@FXML
	private Button newContactScanQRButton;
	@FXML
	private Button newContactNFCButton;
	@FXML
	private Label nfcInfoLabel;

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
		super(App.class.getResource("views/contactlist/newContactView.fxml"));
		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle("Neuen Kontakt erstellen");
		stage.setScene(new Scene(root, 600, 400));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();
	}


	private void scanQRCode(){
		publickey = getQRCodeFromCamera();
		ImplContact newContact = new ImplContact("", uid, publickey, sharkNetModel.getMyProfile());
		stage.close();
		ShowContactController s = new ShowContactController(newContact, true);
	}

	//TODO
	private void contactExchangeNFC(){
		nameInputTextField.setVisible(false);
		mailInputTextField.setVisible(false);
		switchOnNFC();
		Contact newContact = waitForContactExchangeNFC();
		if(newContact != null){
			stage.close();
			ShowContactController s = new ShowContactController(newContact, true);
		}

		nameInputTextField.setVisible(true);
		mailInputTextField.setVisible(true);
	}


	//TODO
	private String getQRCodeFromCamera(){
		String qrcode = "";

		//dummy
		qrcode= "DUMMYKEY from QR-Code";

		return qrcode;
	}


	private void switchOnNFC(){}
	private Contact waitForContactExchangeNFC(){
		//dummy
		publickey = "DUMMYKEY from NFC";
		ImplContact testkontakt = new ImplContact("NFCKontakt", uid, publickey, sharkNetModel.getMyProfile());

		return testkontakt;
	}



	@Override
	protected void onFxmlLoaded() {

		saveButton.setOnMouseClicked(event -> {
			if(nameInputTextField.getText().length() > 0){
				System.out.println("Neuen Kontakt erstellen: " + nameInputTextField.getText());
				ImplContact newContact = new ImplContact(nameInputTextField.getText(), uid, publickey, sharkNetModel.getMyProfile());
				if(mailInputTextField.getText() != "") {
					newContact.setEmail(mailInputTextField.getText());
				}
				if(publicKeyTextArea.getText() != ""){
					newContact.setPublicKey(publicKeyTextArea.getText());
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
