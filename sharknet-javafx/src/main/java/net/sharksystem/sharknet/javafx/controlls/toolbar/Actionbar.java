package net.sharksystem.sharknet.javafx.controlls.toolbar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.javafx.actions.Action;
import net.sharksystem.sharknet.javafx.actions.ActionCallback;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;

import java.util.HashMap;
import java.util.Map;

public class Actionbar extends Region {

	private StringProperty title;
	private ObservableList<Action> actions;
	private ObjectProperty<Node> navigationNode;
	private HBox actionBox;
	private Text titleText;
	private Map<Action, Node> nodeLookUp;

	public Actionbar() {
		getStyleClass().add("toolbar");
		navigationNode().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
				getChildren().remove(oldValue);
			}

			if (newValue != null) {
				getChildren().add(newValue);
			}
        });

		titleText = new Text();
		titleText.getStyleClass().add("text");
		titleText.textProperty().bind(titleProperty());
		getChildren().add(titleText);

		actionBox = new HBox();
		actionBox.getStyleClass().add("action-box");
		getChildren().add(actionBox);

		nodeLookUp = new HashMap<>();
		actions = FXCollections.observableArrayList();
		actions.addListener((ListChangeListener<Action>) c -> {
			while (c.next()) {
				if (!c.wasPermutated() && !c.wasUpdated()) {

					c.getRemoved().forEach((action) -> {
						Node node = nodeLookUp.remove(action);
						actionBox.getChildren().remove(node);
					});

					c.getAddedSubList().forEach((action) -> {
						Button actionButton = createActionButton(action);
						nodeLookUp.put(action, actionButton);
						actionBox.getChildren().add(actionButton);
					});
				}
			}
        });

		setMaxHeight(USE_PREF_SIZE);
		setMinHeight(USE_PREF_SIZE);
	}

	private Button createActionButton(Action action) {
		Button button = new Button();
		if (! "".equals(action.getTooltip())) {
			button.setTooltip(new Tooltip(action.getTooltip()));
		}
		action.getIcon().ifPresent((icon) -> {
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

	public ObjectProperty<Node> navigationNode() {
		if (navigationNode == null) {
			navigationNode = new SimpleObjectProperty<>(this, "navigationNode");
		}
		return navigationNode;
	}

	public void setNavigationNode(Node node) {
		navigationNode().set(node);
	}

	public Node getNavigationNode() {
		return navigationNode == null ? null : navigationNode().get();
	}

	public void addActionEntry(Action action) {
		actions.add(action);
	}

	public void removeActionEntry(Action action) {
		actions.remove(action);
	}

	public void setNavigationAction(Action navigationAction) {
		setNavigationNode(createActionButton(navigationAction));
	}
}
