package net.sharksystem.sharknet.javafx.controls;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;
import net.sharksystem.sharknet.javafx.actions.ActionCallback;
import net.sharksystem.sharknet.javafx.actions.ActionEntry;
import net.sharksystem.sharknet.javafx.controls.skins.ActionbarSkin;


/**
 * Google Material inspired Actionbar
 */
@DefaultProperty("actions")
public class ActionBar extends Control {

	/***************************************************************************
	 *                                                                         *
	 * Fields                                                                  *
	 *                                                                         *
	 **************************************************************************/

	/**
	 * The title of which is drawn in the center of the actionbar.
	 */
	private StringProperty title;

	/**
	 * This node will be shown on the left side
	 * and is a placeholder for the menu button.
	 */
	private ObjectProperty<Node> navigationNode;

	/**
	 * List of actions which should be shown in
	 * side the actionbar.
	 */
	private final ObservableList<ActionEntry> actions = FXCollections.observableArrayList();

	/***************************************************************************
	 *                                                                         *
	 * Constructors                                                            *
	 *                                                                         *
	 **************************************************************************/


	public ActionBar() {
		initialize();
	}

	private void initialize() {
		getStyleClass().add(STYLE_CLASS);
		setAccessibleRole(AccessibleRole.TOOL_BAR);
	}

	/***************************************************************************
	 *                                                                         *
	 * Properties                                                              *
	 *                                                                         *
	 **************************************************************************/

	/**
	 * This property defines the title which is shown in side the actionbar.
	 *
	 * @defaultValue empty string
     */
	public final StringProperty titleProperty() {
		if (title == null) {
			title = new SimpleStringProperty(this, "title", "");
		}
		return title;
	}

	/**
	 * Defines the title which is shown in the center of the actionbar.
	 *
	 * @param title
     */
	public final void setTitle(String title) {
		if (title == null) title = "";
		titleProperty().set(title);
	}

	/**
	 * Retrieves the current active title.
	 *
	 * @return the current setted title.
     */
	public final String getTitle() {
		return title == null ? "" : titleProperty().get();
	}

	/**
	 * This node will be shown on the left side of the actionbar
	 * and is intended as placeholder for a menu button or other navigation buttons
	 * like a back button.
	 *
	 * @defaultValue null
     */
	public final ObjectProperty<Node> navigationNodeProperty() {
		if (navigationNode == null) {
			navigationNode = new SimpleObjectProperty<>(ActionBar.this, "navigationNode");
		}
		return navigationNode;
	}

	/**
	 * Defines the node which is displayed as navigation.
	 *
	 * @param node the navigation node.
     */
	public final void setNavigationNode(Node node) {
		navigationNodeProperty().set(node);
	}

	/**
	 * @return the current defined navigation node.
     */
	public final Node getNavigationNode() {
		return navigationNode == null ? null : navigationNodeProperty().get();
	}


	/**
	 * List of actions which will be shown in the right side
	 * of the actionbar. The Actionbar tries to show so
	 * many actions as possible, if there are no place
	 * the remaining actions will be shown inside a menu.
	 *
	 * @defaultValue empty list
     */
	public final ObservableList<ActionEntry> actions() {
		return actions;
	}

	/***************************************************************************
	 *                                                                         *
	 * Methods                                                                 *
	 *                                                                         *
	 **************************************************************************/

	/**
	 * Converts an ActionEntry to a corresponding button.
	 *
	 * @param action the action which should be converted to a button.
	 * @return the new created button.
     */
	public static final  Button createActionButton(ActionEntry action) {
		Button button = new Button();
		button.getStyleClass().add("icon-label");
		if (! "".equals(action.getTooltip())) {
			button.setTooltip(new Tooltip(action.getTooltip()));
		}
		action.getIconOptional().ifPresent((icon) -> {
			button.setText(icon.getText());
		});

		button.setOnAction((event -> {
			action.getCallback().ifPresent(ActionCallback::invoke);
		}));

		return button;
	}

	/***************************************************************************
	 *                                                                         *
	 * Skin                                                                    *
	 *                                                                         *
	 **************************************************************************/

	/** {@inheritDoc} */
	@Override protected Skin<?> createDefaultSkin() {
		return new ActionbarSkin(this);
	}

	/***************************************************************************
	 *                                                                         *
	 *                         Stylesheet Handling                             *
	 *                                                                         *
	 **************************************************************************/

	/**
	 * Default style class which is used by this control
	 */
	private final static String STYLE_CLASS = "actionbar";


}
