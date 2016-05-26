package net.sharksystem.sharknet.javafx.controller;

import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

public class TimelineController extends AbstractController {

	private FrontController frontController;

	public TimelineController(FrontController frontController) {
		super(App.class.getResource("views/timelineView.fxml"));
		this.frontController = frontController;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		// TODO: implement timeline controller
	}
}
