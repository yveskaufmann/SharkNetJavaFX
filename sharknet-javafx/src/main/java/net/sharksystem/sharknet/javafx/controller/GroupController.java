package net.sharksystem.sharknet.javafx.controller;


import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

public class GroupController extends AbstractController {

	private FrontController frontController;

	public GroupController(FrontController frontController) {
		super(App.class.getResource("views/groupView.fxml"));
		this.frontController = frontController;
	}

	@Override
	protected void onFxmlLoaded() {
		//TODO: Implement group controller
	}
}
