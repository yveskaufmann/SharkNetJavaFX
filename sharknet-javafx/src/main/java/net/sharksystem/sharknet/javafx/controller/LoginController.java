package net.sharksystem.sharknet.javafx.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

/**
 * Created by Benni on 18.05.2016.
 */
public class LoginController extends AbstractController {

	private AppController appController;

	public LoginController(AppController appController) {
		super(App.class.getResource("views/loginView.fxml"));
		this.appController = appController;

	}

	@Override
	protected void onFxmlLoaded() {
		//TODO: Implement chat controller
	}


	@FXML
	private void onLoginClick(ActionEvent event) {
		System.out.println("onLoginClick");
	}

	@FXML
	private void onCancelClick(ActionEvent event) {
		System.out.println("onCancelClick");
	}
}
