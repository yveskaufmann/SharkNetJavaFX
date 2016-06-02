package net.sharksystem.sharknet.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.sharksystem.sharknet.api.ImplSharkNet;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;

import java.util.List;

/**
 * Created by Benni on 18.05.2016.
 */
public class LoginController extends AbstractController {

	private FrontController frontController;
	private ImplSharkNet sharkNetModel;

	@FXML
	private Label labelProfileName;

	public LoginController(FrontController frontController) {
		super(App.class.getResource("views/loginView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);;
	}

	@Override
	protected void onFxmlLoaded() {
		//TODO: Implement chat controller
		loadProfiles();
	}


	@FXML
	private void onLoginClick(ActionEvent event) {
		System.out.println("onLoginClick");
	}

	@FXML
	private void onCancelClick(ActionEvent event) {
		System.out.println("onCancelClick");
	}

	private void loadProfiles() {
		List<Profile> profileList = sharkNetModel.getProfiles();
		// TODO: add Profiles to View
	}
}
