package net.sharksystem.sharknet.javafx.controller;

import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

public class ProfileController extends AbstractController {

	private AppController appController;

	public ProfileController(AppController appController) {
		super(App.class.getResource("views/profileView.fxml"));
		this.appController = appController;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		// TODO: implement profile controller
	}
}
