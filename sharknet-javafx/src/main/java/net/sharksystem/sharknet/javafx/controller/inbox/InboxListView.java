package net.sharksystem.sharknet.javafx.controller.inbox;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import net.sharksystem.sharknet.api.Feed;

import javax.annotation.PreDestroy;

/**
 * @Author Yves Kaufmann
 * @since 09.07.2016
 */
public class InboxListView extends ScrollPane {

	private static final String DEFAULT_STYLE_CLASS = "feed-list";

	/******************************************************************************
	 *
	 * Node Elemezs
	 *
	 ******************************************************************************/

	private VBox feedVbox;
	private Timeline timeline;

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	private ObservableList<Feed> feeds;

	/**
	 * List of feeds which should be rendered.
	 *
	 * @return the list of feeds.
     */
	public ObservableList<Feed> getFeeds() {
		if (feeds == null) {
			feeds = FXCollections.observableArrayList();
			feeds.addListener(this::onFeedsChanged);
		}
		return feeds;
	}

	/******************************************************************************
	 *
	 * Constructor
	 *
	 ******************************************************************************/

	public InboxListView() {
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		feedVbox = new VBox(20);
		feedVbox.setFillWidth(true);

		setContent(feedVbox);
		setHbarPolicy(ScrollBarPolicy.NEVER);
		setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

		timeline.setAutoReverse(true);
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	@PreDestroy
	private void onShutdown() {
		// stop the animation
		timeline.stop();
	}

	/******************************************************************************
	 *
	 * Event Handling
	 *
	 ******************************************************************************/

	private void onFeedsChanged(ListChangeListener.Change<? extends Feed> changes) {

	}
}
