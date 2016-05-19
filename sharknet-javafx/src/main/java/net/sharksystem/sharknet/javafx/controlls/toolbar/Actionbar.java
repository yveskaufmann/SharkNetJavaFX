package net.sharksystem.sharknet.javafx.controlls.toolbar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.javafx.actions.ActionEntry;
import net.sharksystem.sharknet.javafx.actions.ActionCallback;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;

import java.util.HashMap;
import java.util.Map;

public class Actionbar extends Region {

	private final int MAX_COUNT_ACTIONS = 1;

	private StringProperty title;
	private ObservableList<ActionEntry> actions;
	private ObjectProperty<Node> navigationNode;
	private HBox actionBox;
	private Text titleText;
	private Map<ActionEntry, Node> nodeLookUp;
	private Map<ActionEntry, MenuItem> menuLookUp;
	private ContextMenu contextMenu;
	private Button menuButton;


	public Actionbar() {
		getStyleClass().add("toolbar");


		contextMenu = new ContextMenu();

		titleText = new Text();
		titleText.getStyleClass().add("text");
		titleText.textProperty().bind(titleProperty());
		getChildren().add(titleText);

		actionBox = new HBox();
		actionBox.getStyleClass().add("action-box");
		getChildren().add(actionBox);

		nodeLookUp = new HashMap<>();
		menuLookUp = new HashMap<>();


		setMaxHeight(USE_PREF_SIZE);
		setMinHeight(USE_PREF_SIZE);
	}

	private Button createActionButton(ActionEntry action) {
		Button button = new Button();
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

	@Override
	protected void layoutChildren() {
		super.layoutChildren();

		double actionBoxPrefWidth = actionBox.prefWidth(-1);
		double actionBoxPrefHeight = actionBox.prefHeight(-1);
		double titlePrefWidth = titleText.prefWidth(-1);
		double titlePrefHeight = titleText.prefHeight(-1);
		double navPrefHeight = 0;

		if (navigationNode.get() != null) {
			navPrefHeight = navigationNode.get().prefHeight(-1);
		}

		double centerOfLine = Math.max(actionBoxPrefHeight, navPrefHeight);
		centerOfLine = Math.max(centerOfLine, titlePrefHeight);
		centerOfLine = centerOfLine / 2 + getPadding().getTop();


		if (navigationNode.get() != null) {
			navigationNode.get().resize(navigationNode.get().prefWidth(-1), navPrefHeight);
			navigationNode.get().relocate(getPadding().getLeft(), centerOfLine - navPrefHeight / 2);
		}

		actionBox.resize(actionBoxPrefWidth, actionBoxPrefHeight);
		actionBox.relocate(getWidth() - getPadding().getRight() - actionBoxPrefWidth, centerOfLine  - actionBoxPrefHeight / 2);

		titleText.resize(titlePrefWidth, titlePrefHeight);
		titleText.relocate((getWidth() - titlePrefWidth) / 2, centerOfLine - titlePrefHeight/2);
	}

	public StringProperty titleProperty() {
		if (title == null) {
			title = new SimpleStringProperty(this, "title");
		}
		return title;
	}

	public void setTitle(String title) {
		if (title == null) title = "";
		titleProperty().set(title);
	}

	public String getTitle() {
		return title == null ? "" : titleProperty().get();
	}

	public ObjectProperty<Node> navigationNodeProperty() {
		if (navigationNode == null) {
			navigationNode = new SimpleObjectProperty<>(this, "navigationNodeProperty");
			navigationNode.addListener((observable, oldValue, newValue) -> {
				if (oldValue != null) {
					getChildren().remove(oldValue);
				}

				if (newValue != null) {
					getChildren().add(newValue);
				}
			});
		}
		return navigationNode;
	}

	public void setNavigationNode(Node node) {
		navigationNodeProperty().set(node);
	}

	public Node getNavigationNode() {
		return navigationNode == null ? null : navigationNodeProperty().get();
	}

	public void setNavigationAction(ActionEntry navigationAction) {
		setNavigationNode(createActionButton(navigationAction));
	}

	public ObservableList<ActionEntry> actionEntriesProperty() {
		if (actions == null) {
			actions = FXCollections.observableArrayList();
			actions.addListener((ListChangeListener<ActionEntry>) c -> {
				while (c.next()) {
					if (!c.wasPermutated() && !c.wasUpdated()) {
						for (ActionEntry action : c.getRemoved()) {
							Node node = nodeLookUp.remove(action);
							if (node != null) {
								actionBox.getChildren().remove(node);
							} else {
								MenuItem item = menuLookUp.remove(action);
								contextMenu.getItems().remove(item);
							}
						}

						if (contextMenu.getItems().size() == 0) {
							actionBox.getChildren().remove(menuButton);
							menuButton = null;
						}

						for (ActionEntry action : c.getAddedSubList()) {
							if (actionBox.getChildren().size() >  MAX_COUNT_ACTIONS - 1) {
								MenuItem menuItem = new MenuItem(action.getTitle(), new Text(action.getIcon()));
								action.getCallback().ifPresent((callback) -> {
									menuItem.setOnAction((event -> callback.invoke()));
								});
								menuLookUp.put(action, menuItem);
								contextMenu.getItems().add(menuItem);
								if (menuButton == null) {
									menuButton = createActionButton(new ActionEntry(FontAwesomeIcon.NAVICON, () -> {
										contextMenu.show(menuButton, Side.LEFT, 0, 0);
									}));
									menuButton.getStyleClass().add("context-menu-button");
									actionBox.getChildren().add(menuButton);
								}
							} else {
								Button actionButton = createActionButton(action);
								nodeLookUp.put(action, actionButton);
								actionBox.getChildren().add(actionButton);
							}
						}
					}
				}
			});
		}
		return actions;
	}

	public void addActionEntry(ActionEntry action) {
		actionEntriesProperty().add(action);
	}

	public void removeActionEntry(ActionEntry action) {
		actionEntriesProperty().remove(action);
	}
	public void clearActionEntries() {
		actionEntriesProperty().clear();
	}


}
