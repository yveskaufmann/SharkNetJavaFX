package net.sharksystem.sharknet.javafx.controller;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;

import java.util.List;

/**
 * Created by Benni on 18.05.2016.
 */
public class LoginController extends AbstractController {

	@Inject
	private SharkNet sharkNet;
	private FrontController frontController;

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
		List<Profile> profileList = sharkNet.getProfiles();
		// TODO: add Profiles to View
	}
}
