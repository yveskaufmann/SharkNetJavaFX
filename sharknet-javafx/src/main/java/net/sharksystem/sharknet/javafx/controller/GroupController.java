package net.sharksystem.sharknet.javafx.controller;


import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

public class GroupController extends AbstractController {

	private AppController appController;

	public GroupController(AppController appController) {
		super(App.class.getResource("views/groupView.fxml"));
		this.appController = appController;
	}

	@Override
	protected void onFxmlLoaded() {
		//TODO: Implement group controller
	}
}
