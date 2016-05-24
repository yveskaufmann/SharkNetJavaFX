package net.sharksystem.sharknet.javafx.controlls;


import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class MediaListView<T> extends ListView<T> {

	public MediaListView() {
		super();
		initialize();
	}

	public MediaListView(ObservableList<T> items) {
		super(items);
		initialize();
	}

	private void initialize() {
		getStyleClass().add("media-list");
	}
}
