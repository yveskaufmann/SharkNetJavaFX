package net.sharksystem.sharknet.javafx.controller;

import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Action;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

@Controller( title = "%inbox.title")
public class InboxController extends AbstractController {

	private AppController appController;

	public InboxController(AppController appController) {
		super(App.class.getResource("views/inboxView.fxml"));
		this.appController = appController;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		// TODO: implement inbox controller
	}

	@Action(icon = "\uf002 ", text = "%action.search")
	public void search() {
		System.out.println("Show Search entry");
	}

	@Action(text = "%action.my_broadcasts", priority = 1)
	public void showMyBroadcasts() {
		System.out.println("My Broadcasts");
	}

	@Action(text = "%action.all_broadcasts", priority = 2)
	public void showAllBroadcasts() {
		System.out.println("All Broadcasts");
	}

	@Action(text = "%action.single_chat", priority = 3)
	public void showSingleChats() {
		System.out.println("Single Chats");
	}

	@Action(text = "%action.group_chat", priority = 4)
	public void showGroupChats() { System.out.println("Group Chats"); }

	@Action(text = "%action.system_info", priority = 5)
	public void showSystemInfo() { System.out.println("System Info"); }

}

