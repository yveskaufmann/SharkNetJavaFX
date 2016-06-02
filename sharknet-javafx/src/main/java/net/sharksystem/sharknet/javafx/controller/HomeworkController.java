package net.sharksystem.sharknet.javafx.controller;

import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

@Controller(title = "%sidebar.homework")
public class HomeworkController extends AbstractController {

	private FrontController frontController;

	public HomeworkController() {
		super(App.class.getResource("views/homeworkView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		// TODO: implement homework controller
	}
}
