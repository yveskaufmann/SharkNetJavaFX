package net.sharksystem.sharknet.javafx.controlls.skins;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.ActionEntry;
import net.sharksystem.sharknet.javafx.controlls.ActionBar;
import net.sharksystem.sharknet.javafx.controlls.behaviour.ActionBarBehaviour;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @see ActionBar
 */
public class ActionbarSkin extends BehaviorSkinBase<ActionBar, ActionBarBehaviour> {

	public static final int MAX_ACTIONS = 1;
	private HBox actionbox;
	private Text titleText;
	private Map<ActionEntry, Node> nodeLookUp;
	private Map<ActionEntry, MenuItem> menuLookUp;
	private ContextMenu contextMenu;
	private Button menuButton;


	public ActionbarSkin(ActionBar actionbar) {
		super(actionbar, new ActionBarBehaviour(actionbar));

		getSkinnable().getStylesheets().add(App.class.getResource("style.css").toExternalForm());

		contextMenu = new ContextMenu();
		titleText = new Text();
		titleText.getStyleClass().add("text");
		titleText.textProperty().bind(getSkinnable().titleProperty());
		getChildren().add(titleText);

		actionbox = new HBox();
		actionbox.getStyleClass().add("action-box");
		getChildren().add(actionbox);

		nodeLookUp = new HashMap<>();
		menuLookUp = new HashMap<>();


		actionbar.setMaxHeight(Control.USE_PREF_SIZE);
		actionbar.setMinHeight(Control.USE_PREF_SIZE);

		getSkinnable().navigationNodeProperty().addListener(this::onNavigationNodeChanged);
		getSkinnable().actions().addListener(this::onActionsChanged);

		onNavigationNodeChanged(getSkinnable().navigationNodeProperty(), null, getSkinnable().getNavigationNode());
		insertActionButtons(getSkinnable().actions());
		getSkinnable().toBack();
		getSkinnable().layout();
	}

	private void onActionsChanged(ListChangeListener.Change<? extends ActionEntry> c) {
		while (c.next()) {
            if (!c.wasPermutated() && !c.wasUpdated()) {
                for (ActionEntry action : c.getRemoved()) {
                    Node node = nodeLookUp.remove(action);
                    if (node != null) {
                        actionbox.getChildren().remove(node);
                    } else {
                        MenuItem item = menuLookUp.remove(action);
                        contextMenu.getItems().remove(item);
                    }
                }

                if (contextMenu.getItems().size() == 0) {
                    actionbox.getChildren().remove(menuButton);
                    menuButton = null;
                }
				insertActionButtons(c.getAddedSubList());
			}
        }
		getSkinnable().requestLayout();
	}

	private void insertActionButtons(List<? extends ActionEntry> actions) {
		for (ActionEntry action : actions) {
            if (actionbox.getChildren().size() >  MAX_ACTIONS - 1) {
                MenuItem menuItem = new MenuItem(action.getTitle(), new Text(action.getIcon()));
                action.getCallback().ifPresent((callback) -> {
                    menuItem.setOnAction((event -> callback.invoke()));
                });
                menuLookUp.put(action, menuItem);
                contextMenu.getItems().add(menuItem);
                if (menuButton == null) {
                    menuButton = ActionBar.createActionButton(new ActionEntry(FontAwesomeIcon.NAVICON, () -> {
                        contextMenu.show(menuButton, Side.LEFT, 0, 0);
                    }));
                    menuButton.getStyleClass().add("context-menu-button");
                    actionbox.getChildren().add(menuButton);
                }
            } else {
                Button actionButton = ActionBar.createActionButton(action);
                nodeLookUp.put(action, actionButton);
                actionbox.getChildren().add(actionButton);
            }
        }
	}

	private void onNavigationNodeChanged(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
			if (oldValue != null) {
				getChildren().remove(oldValue);
			}

			if (newValue != null) {
				getChildren().add(newValue);
			}
			getSkinnable().requestLayout();
	}

	@Override
	protected void layoutChildren(final double x,final double y,
								  final double w, final double h) {
		super.layoutChildren(x, y, w, h);

		double actionBoxPrefWidth = actionbox.prefWidth(-1);
		double actionBoxPrefHeight = actionbox.prefHeight(-1);
		double titlePrefWidth = titleText.prefWidth(-1);
		double titlePrefHeight = titleText.prefHeight(-1);
		double navPrefHeight = 0;

		Node navNode = getSkinnable().navigationNodeProperty().get();

		if (navNode != null) {
			navPrefHeight = navNode.prefHeight(-1);
		}

		double centerOfLine = Math.max(actionBoxPrefHeight, navPrefHeight);
		centerOfLine = Math.max(centerOfLine, titlePrefHeight);
		centerOfLine = centerOfLine / 2 + getSkinnable().getPadding().getTop();

		if (navNode!= null) {
			navNode.resize(navNode.prefWidth(-1), navPrefHeight);
			navNode.relocate(getSkinnable().getPadding().getLeft(), centerOfLine - navPrefHeight / 2);
		}

		actionbox.resize(actionBoxPrefWidth, actionBoxPrefHeight);
		actionbox.relocate(getSkinnable().getWidth()  - getSkinnable().getPadding().getRight() - actionBoxPrefWidth, centerOfLine  - actionBoxPrefHeight / 2);

		titleText.resize(titlePrefWidth, titlePrefHeight);
		titleText.relocate((getSkinnable().getWidth() - titlePrefWidth) / 2, centerOfLine - titlePrefHeight/2);
	}
}
