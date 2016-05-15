package net.sharksystem.sharknet.javafx.controller;


import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

public class SettingsController extends AbstractController {

	private AppController appController;

	public SettingsController(AppController appController) {
		super(App.class.getResource("views/settingsView.fxml"));
		this.appController = appController;
	}

	@Override
	protected void onFxmlLoaded() {
		//TODO: Implement settings controller
	}
}
