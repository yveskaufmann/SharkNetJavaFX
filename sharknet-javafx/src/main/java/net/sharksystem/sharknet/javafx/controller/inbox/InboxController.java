package net.sharksystem.sharknet.javafx.controller.inbox;

import javafx.fxml.FXML;
import net.sharksystem.sharknet.api.ImplContact;
import net.sharksystem.sharknet.api.ImplFeed;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Action;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

@Controller( title = "%inbox.title")
public class InboxController extends AbstractController {

	private FrontController frontController;

	@FXML InboxList inboxListView;



	public InboxController(FrontController frontController) {
		super(App.class.getResource("views/inbox/inboxView.fxml"));
		this.frontController = frontController;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		for(int i = 0; i < 20; i++) {// Test dummy data
			inboxListView.getItems().add(
				new ImplFeed("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat vero.",
					null, new ImplContact("Lorem ipsum", "1212121", "sadasdsadsadaasdas"))
			);
		}

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

