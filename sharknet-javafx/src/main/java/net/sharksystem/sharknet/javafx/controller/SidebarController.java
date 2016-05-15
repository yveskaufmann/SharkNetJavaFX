package net.sharksystem.sharknet.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

/**
 * This controller is responsible to provide a
 * menu which leads to the timeline, the chat,
 * the contact list, the profile and so on.
 */
public class SidebarController extends AbstractController {

	@FXML
	private Button profileButton;
	@FXML
	private Button groupButton;
	@FXML
	private Button inboxButton;
	@FXML
	private Button contactButton;
	@FXML
	private Button chatButton;
	@FXML
	private Button timelineButton;

	private AppController appController;

	public SidebarController(AppController appController) {
		super(App.class.getResource("views/sidebarView.fxml"));
		this.appController = appController;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		profileButton.setOnAction((event -> appController.performAppAction(AppController.AppAction.OPEN_PROFILE)));
		groupButton.setOnAction((event -> appController.performAppAction(AppController.AppAction.OPEN_GROUPS)));
		inboxButton.setOnAction((event -> appController.performAppAction(AppController.AppAction.OPEN_INBOX)));
		contactButton.setOnAction((event -> appController.performAppAction(AppController.AppAction.OPEN_CONTACTS)));
		chatButton.setOnAction((event -> appController.performAppAction(AppController.AppAction.OPEN_CHATS)));
		timelineButton.setOnAction((event -> appController.performAppAction(AppController.AppAction.OPEN_TIMELINE)));
	}
}
