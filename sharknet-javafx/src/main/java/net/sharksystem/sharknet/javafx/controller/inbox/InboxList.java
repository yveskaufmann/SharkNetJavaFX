package net.sharksystem.sharknet.javafx.controller.inbox;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import net.sharksystem.sharknet.api.Feed;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListView;

public class InboxList extends MediaListView<Feed> {

	private Timeline timeline;

	public InboxList() {
		super();
		getStyleClass().remove("list-view");
		getStyleClass().remove("media-list");
		getStyleClass().add("inbox-list");
		setCellFactory(param -> new MediaListCell<>(InboxEntryController.class));
		setFixedCellSize(-1.0);

		/* Updates periodically the list */
		// TODO: ensure that list cells manage its state
		timeline = new Timeline(new KeyFrame(Duration.minutes(1.0), event -> {
			refresh();
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	/******************************************************************************
	 *
	 * Public API
	 *
	 ******************************************************************************/

	/**
	 * Pauses the periodically update, which
	 * ensures that relative time labels are updated
	 * each period.
	 *
	 * Must be called if this list becomes hidden
	 */
	public void pauseUpdatee() {
		timeline.pause();
	}

	/**
	 * Resume to the periodically update, which
	 * ensures that relative time labels are updated
	 * each period.
	 *
	 * Must be called before this list becomes visible again.
	 */
	public void resumeUpdate() {
		timeline.play();
	}

	/**
	 * Stop the periodically updates must be called
	 * before the app exits.
	 */
	public void stopUpdate() {
		if (! timeline.getStatus().equals(Animation.Status.STOPPED)) {
			timeline.stop();
		}
	}

}
