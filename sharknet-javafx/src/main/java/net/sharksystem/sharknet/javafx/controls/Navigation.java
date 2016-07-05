package net.sharksystem.sharknet.javafx.controls;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import net.sharksystem.sharknet.javafx.actions.ActionCallback;
import net.sharksystem.sharknet.javafx.actions.ActionEntry;
import net.sharksystem.sharknet.javafx.controls.skins.NavigationSkin;

/**
 *
 */
@DefaultProperty("items")
public class Navigation extends Control {

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public Navigation() {
		super();
		initialize();
	}

	public Navigation(ActionEntry...items) {
		super();
		initialize();
		this.items.addAll(items);
	}

	private void initialize() {
		setAccessibleRole(AccessibleRole.MENU);
		getStyleClass().add(DEFUALT_STYLE_CLASS);
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	private ObservableList<ActionEntry> items = FXCollections.observableArrayList();
	public ObservableList<ActionEntry> getItems() {
		return items;
	}

	public final ObjectProperty<ActionCallback> onActionProperty() { return onActionEntry; }
	public final void setOnAction(ActionCallback value) { onActionProperty().set(value); }
	public final ActionCallback getOnAction() { return onActionProperty().get(); }
	private ObjectProperty<ActionCallback> onActionEntry = new ObjectPropertyBase<ActionCallback>() {

		@Override
		public Object getBean() {
			return Navigation.this;
		}

		@Override
		public String getName() {
			return "onAction";
		}
	};


	/******************************************************************************
	 *
	 * Styling
	 *
	 ******************************************************************************/

	@Override
	protected Skin<?> createDefaultSkin() {
		return new NavigationSkin(this);
	}

	private static final String DEFUALT_STYLE_CLASS = "navigation";

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/
}

