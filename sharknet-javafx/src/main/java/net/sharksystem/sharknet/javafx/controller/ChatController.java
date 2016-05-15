package net.sharksystem.sharknet.javafx.controller;


import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

public class ChatController extends AbstractController {

	private AppController appController;

	public ChatController(AppController appController) {
		super(App.class.getResource("views/chatView.fxml"));
		this.appController = appController;
	}

	@Override
	protected void onFxmlLoaded() {
		//TODO: Implement chat controller
	}
}
