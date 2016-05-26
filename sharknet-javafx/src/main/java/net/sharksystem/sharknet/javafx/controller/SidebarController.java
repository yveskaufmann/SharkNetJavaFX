package net.sharksystem.sharknet.javafx.controller;

import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.animations.DoublePropertyTransition;
import net.sharksystem.sharknet.javafx.controller.inbox.InboxController;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

/**
 * This controller is responsible to provide a
 * menu which leads to the timeline, the chat,
 * the contact list, the profile and so on.
 *
 **/
public class SidebarController extends AbstractController {

	//TODO: add action concept from the actionbar

	@FXML
	private VBox sidebar;
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

	private FrontController frontController;


	public SidebarController(FrontController frontController) {
		super(App.class.getResource("views/sidebarView.fxml"));
		this.frontController = frontController;

	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		profileButton.setOnAction((event -> frontController.goToView(ProfileController.class)));
		groupButton.setOnAction((event -> frontController.goToView(GroupController.class)));
		inboxButton.setOnAction((event -> frontController.goToView(InboxController.class)));
		contactButton.setOnAction((event -> frontController.goToView(ContactController.class)));
		chatButton.setOnAction((event -> frontController.goToView(ChatController.class)));
		timelineButton.setOnAction((event -> frontController.goToView(TimelineController.class)));
	}

}
