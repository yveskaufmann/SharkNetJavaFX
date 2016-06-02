package net.sharksystem.sharknet.javafx.controller;

import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;

public class ProfileController extends AbstractController {

	private FrontController frontController;

	public ProfileController() {
		super(App.class.getResource("views/profileView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		// TODO: implement profile controller
	}
}
