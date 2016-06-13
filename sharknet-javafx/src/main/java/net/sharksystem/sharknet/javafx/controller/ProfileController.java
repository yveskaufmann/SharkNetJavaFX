package net.sharksystem.sharknet.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;

public class ProfileController extends AbstractController {

	@FXML
	private TextField username;

	@FXML
	private TextField email;

	@FXML
	private Label label;

	private FrontController frontController;

	public ProfileController() {
		super(App.class.getResource("views/profile/profileTabPane.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		System.out.println(label);
		// TODO: implement profile controller
	}
}
