package net.sharksystem.sharknet.javafx.controls.medialist;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

import java.net.URL;

/**
 * A MediaListCellController is responsible to render a item
 * in a MediaListView.
 *
 * @param <T> type of items which are rendered by this controller.
 */
public abstract class MediaListCellController<T> extends AbstractController {

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	/**
	 * Provides access to the surround list cell instance.
	 *
	 * <p>Enables subclasses to interact with the surrounding cell instance,
	 * such as register click, selection handlers and so on.
	 *
	 */
	protected MediaListCell<T> cell;

	/**
	 * Contains the item which should be rendered by {@link #onItemChanged(Object)}.
	 */
	private ObjectProperty<T> item;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	/**
	 * Creates a MediaListController for a specified view and media list cell.
	 *
	 * @param fxmlFile the view of this controller
	 * @param cell the surrounding {@link MediaListCell} which is responsible to render this controller.
     */
	public MediaListCellController(URL fxmlFile, MediaListCell<T> cell) {
		super(fxmlFile);
		this.cell = cell;
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	/**
	 * Specifies the item which should currently displayed inside
	 * the current cell. When the item property is changed
	 * the {@link }
	 *
	 * @defaultValue null ==> empty
     */
	final ObjectProperty<T> itemProperty() {
		if (item == null) {
			item = new SimpleObjectProperty<T>(this, "item") {
				@Override
				protected void invalidated() {
					super.invalidated();
					onItemChanged(item.get());
				}
			};
		}
		return item;
	}

	/**
	 * @see {@link #itemProperty()}
     */
	final T getItem() {
		return (item == null) ? null : item.get();
	}

	/**
	 * @see {@link #itemProperty()}
	 */
	final void setItem(T item) {
		itemProperty().set(item);
	}


	/**
	 * Specifies the surrounding cell which
	 * is rendering the controlled view of this controller.
	 *
	 * @param cell the surrounding {@link MediaListCell}
     */
	final void setCell(MediaListCell<T>  cell) {
		this.cell = cell;
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	/**
	 * <p>Will be invoked when the item is changed.
	 *
	 * <p>Subclasses must implement the view update logic, which updates the controller view so that it shows
	 * the data of the given item object.
	 * The following message cell controller example shows a typical implementation of implementation:
	 *
	 * <pre>
	 * public MessageCellController extends MediaListCellController<Message> {
	 *
	 *		&#64;FXML
	 * 		private Label username;
	 *
	 *		&#64;FXML
	 * 		private Label message;
	 *
	 *		// ...
	 *
	 * 		&#64;Override
	 *		protected void onItemChanged(Message item) {
	 *			username.setText(item.getSender().getUsername());
	 *			message.setText(item.getMessage());
	 *		}
	 *
	 *		// ...
	 *
	 * }
	 * </pre>
	 *
	 *
	 * @param item the item which must be shown by this controller.
     */
	protected abstract void onItemChanged(T item);
}

