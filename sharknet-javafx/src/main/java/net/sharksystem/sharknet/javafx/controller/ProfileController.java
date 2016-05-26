package net.sharksystem.sharknet.javafx.controller;

import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

public class ProfileController extends AbstractController {

	private FrontController frontController;

	public ProfileController(FrontController frontController) {
		super(App.class.getResource("views/profileView.fxml"));
		this.frontController = frontController;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		// TODO: implement profile controller
	}
}
