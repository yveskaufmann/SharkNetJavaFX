package net.sharksystem.sharknet.javafx.controller;


import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

public class ContactController extends AbstractController {

	private AppController appController;

	public ContactController(AppController appController) {
		super(App.class.getResource("views/contactsView.fxml"));
		this.appController = appController;
	}

	@Override
	protected void onFxmlLoaded() {
		//TODO: Implement contact controller
	}
}
