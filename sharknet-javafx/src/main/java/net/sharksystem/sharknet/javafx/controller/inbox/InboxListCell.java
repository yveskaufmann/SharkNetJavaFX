package net.sharksystem.sharknet.javafx.controller.inbox;


import javafx.scene.control.ListCell;
import net.sharksystem.sharknet.api.Feed;

public class InboxListCell extends ListCell<Feed> {

	private InboxEntryController entryController;

	public InboxListCell() {
		super();
		getStyleClass().add("inbox-item");
	}

	@Override
	protected void updateItem(Feed item, boolean empty) {
		super.updateItem(item, empty);

		if (item == null || empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (entryController == null) {
				entryController = new InboxEntryController(this);
			}
			setGraphic(entryController.getRoot());
			entryController.setFeed(item);
		}
	}
}
