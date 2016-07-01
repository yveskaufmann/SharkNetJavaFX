package net.sharksystem.sharknet.javafx.controls.medialist;


import javafx.css.PseudoClass;
import javafx.scene.control.ListCell;

import java.lang.reflect.InvocationTargetException;

public class MediaListCell<T> extends ListCell<T> {

	private MediaListCellController<T> cellController;
	private Class<? extends MediaListCellController<T>> controllerClass;

	public MediaListCell(Class<? extends MediaListCellController<T>> controllerClass) {
		super();
		this.controllerClass = controllerClass;
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		emptyProperty().addListener((observable, oldValue, newValue) -> {
			pseudoClassStateChanged(PSEUDO_CLASS_EMPTY, newValue);
		});
	}

	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);

		if (item == null || empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (cellController == null) {
				// TODO: Controller Builder
				try {
					cellController = controllerClass.getConstructor(MediaListCell.class).newInstance(this);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
			setGraphic(cellController.getRoot());
			// Ensure that the property is invalidated
			cellController.setItem(null);
			cellController.setItem(item);
		}
	}

	/******************************************************************************
	 *
	 * Styling
	 *
	 ******************************************************************************/

	/**
	 * Will be true if a cell is empty
	 */
	private static final PseudoClass PSEUDO_CLASS_EMPTY = PseudoClass.getPseudoClass("empty");

	/**
	 * Default style class
	 */
	private static final String DEFAULT_STYLE_CLASS = "media-list-item";
}
