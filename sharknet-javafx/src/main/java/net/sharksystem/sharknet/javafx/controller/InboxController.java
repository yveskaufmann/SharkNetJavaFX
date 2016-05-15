package net.sharksystem.sharknet.javafx.controller;

import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

public class InboxController extends AbstractController {

	private AppController appController;

	public InboxController(AppController appController) {
		super(App.class.getResource("views/inboxView.fxml"));
		this.appController = appController;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		// TODO: implement inbox controller
	}
}
