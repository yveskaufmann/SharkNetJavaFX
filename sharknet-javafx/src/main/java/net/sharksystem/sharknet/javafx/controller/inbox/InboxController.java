package net.sharksystem.sharknet.javafx.controller.inbox;

import javafx.fxml.FXML;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Action;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import javax.annotation.PostConstruct;

@Controller( title = "%inbox.title")
public class InboxController extends AbstractController {

	private FrontController frontController;

	@FXML
	private InboxList inboxListView;

	public InboxController() {
		super(App.class.getResource("views/inbox/inboxView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);
		System.out.println("inbox");

	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		loadEntries();

	}

	private void loadEntries() {
		SharkNet sharkNet = new ImplSharkNet();
		sharkNet.getFeeds(200);

		for(Feed feed : sharkNet.getFeeds(200)) {
			inboxListView.getItems().add(feed);
		}
	}

	@Action(icon = "\uf002 ", text = "%action.search")
	public void search() {
		loadEntries();
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

