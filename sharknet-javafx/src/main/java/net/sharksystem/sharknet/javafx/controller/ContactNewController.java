package net.sharksystem.sharknet.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.ImplSharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.model.SharkNetModel;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;

import java.util.ArrayList;
import java.util.List;


public class ContactNewController extends AbstractController {

	private ImplSharkNet sharkNet;
	private List<Contact> allContacts;

	private Stage stage;

	public ContactNewController(){
		super(App.class.getResource("views/newContactView.fxml"));

		sharkNet = SharkNetModel.getInstance().getSharkNetImpl();

		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle("Neuen Kontakt erstellen");
		stage.setScene(new Scene(root, 494, 414));
		//stage.getScene().getStylesheets().add(App.class.getResource("style.css").toExternalForm());
		stage.show();
	}

	@FXML
	private TextField nameInputTextField;
	@FXML
	private Button saveButton;
	@FXML
	private Button backButton;


	private void onSaveButtonClick(){
		System.out.println("Neuen Kontakt erstellen: " + nameInputTextField.getText());
	}

	private void onBackButtonClick(){}

	@Override
	protected void onFxmlLoaded() {

		/*
		saveButton.setOnMouseClicked(event -> {
			System.out.println("Neuen Kontakt erstellen: " + nameInputTextField.getText());
			event.consume();
		});
	*/}

}
