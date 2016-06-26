package net.sharksystem.sharknet.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import javax.inject.Inject;

public class ShowContactController extends AbstractController {

	@FXML
	private ImageView profilePictureImageView;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField nicknameTextField;
	@FXML
	private TextField emailTextField;
	@FXML
	private TextField telephoneTextField;
	@FXML
	private TextField infoTextField;
	@FXML
	private TextField publicKeyTextField;
	@FXML
	private Button editButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button closeWindowButton;

	@Inject
	private SharkNet sharkNetModel;

	private Contact contact;
	private Stage stage;


	public ShowContactController(Contact c){
		super(App.class.getResource("views/showContactView.fxml"));
		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle("");
		stage.setScene(new Scene(root, 600, 500));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();

		this.contact = c;
		nicknameTextField.setEditable(false);
		nameTextField.setEditable(false);
		emailTextField.setEditable(false);
		telephoneTextField.setEditable(false);
		infoTextField.setEditable(false);
		publicKeyTextField.setEditable(false);
	}



	@Override
	protected void onFxmlLoaded() {
		nicknameTextField.setText(contact.getNickname());
		nameTextField.setText(contact.getName());
		emailTextField.setText(contact.getEmail());
		//telephoneTextField.setText(testkontakt.getTelephonnumber());
		infoTextField.setText(contact.getNote());
		publicKeyTextField.setText(contact.getPublicKey());




		editButton.setOnMouseClicked(event -> {
			nicknameTextField.setEditable(true);
			//nameTextField.setEditable(true);
			emailTextField.setEditable(true);
			telephoneTextField.setEditable(true);
			infoTextField.setEditable(true);
		});

		saveButton.setOnMouseClicked(event -> {
			contact.setNickname(nicknameTextField.getText());
			contact.setEmail(emailTextField.getText());
		});

		closeWindowButton.setOnMouseClicked(event -> {
			stage.close();
			event.consume();
		});

	}

}
