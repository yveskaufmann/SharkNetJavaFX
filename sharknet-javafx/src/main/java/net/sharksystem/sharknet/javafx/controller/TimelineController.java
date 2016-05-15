package net.sharksystem.sharknet.javafx.controller;

import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

public class TimelineController extends AbstractController {

	private AppController appController;

	public TimelineController(AppController appController) {
		super(App.class.getResource("views/timelineView.fxml"));
		this.appController = appController;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		// TODO: implement timeline controller
	}
}
