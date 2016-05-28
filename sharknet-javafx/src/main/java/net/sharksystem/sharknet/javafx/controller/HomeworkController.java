package net.sharksystem.sharknet.javafx.controller;

import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

@Controller(title = "%sidebar.homework")
public class HomeworkController extends AbstractController {

	private FrontController frontController;

	public HomeworkController(FrontController frontController) {
		super(App.class.getResource("views/homeworkView.fxml"));
		this.frontController = frontController;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		// TODO: implement homework controller
	}
}
