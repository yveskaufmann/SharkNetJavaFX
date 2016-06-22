package net.sharksystem.sharknet.javafx.controls.skins;

import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.javafx.actions.ActionEntry;
import net.sharksystem.sharknet.javafx.controls.FontIcon;
import net.sharksystem.sharknet.javafx.controls.Navigation;

import java.nio.channels.Channels;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationSkin extends SkinBase<Navigation> {

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private Pane menuPane;
	private Map<ActionEntry, Node> menuItemToNodeMap = new HashMap<>();

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public NavigationSkin(Navigation navigation) {
		super(navigation);
		initialize();

		navigation.getItems().addListener((ListChangeListener<ActionEntry>) c -> {
 			while (c.next()) {
				removeMenuItemNodes(c.getRemoved());
				addMenuItemNodes(c.getAddedSubList());
			}
			getSkinnable().requestLayout();
        });

	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void initialize() {
		menuPane = new VBox();
		addMenuItemNodes(getSkinnable().getItems());

		getChildren().clear();
		getChildren().add(menuPane);

		getSkinnable().requestLayout();
	}

	private void addMenuItemNodes(List<? extends ActionEntry> addedSubList) {
		int i = 0;
		for (ActionEntry addedItem : addedSubList) {

			GridPane entryPane = new GridPane() {
				@Override
				public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
					switch (action) {
						case FIRE:
							fire(addedItem);
							break;
						default: super.executeAccessibleAction(action);
					}
				}
			};
			entryPane.setAccessibleRole(AccessibleRole.MENU_ITEM);
			entryPane.getStyleClass().add("item");

			RowConstraints entryRowConstraints = new RowConstraints(48);
			entryRowConstraints.setVgrow(Priority.SOMETIMES);
			entryRowConstraints.setValignment(VPos.CENTER);

			ColumnConstraints iconColumnConstraints = new ColumnConstraints(56);
			iconColumnConstraints.setHalignment(HPos.LEFT);
			iconColumnConstraints.setHgrow(Priority.SOMETIMES);
			FontIcon icon = new FontIcon(addedItem.getIcon());
			GridPane.setConstraints(icon, 0, 0);

			ColumnConstraints textColumnConstraints = new ColumnConstraints();
			textColumnConstraints.setHalignment(HPos.LEFT);
			textColumnConstraints.setHgrow(Priority.ALWAYS);
			Label itemText = new Label(addedItem.getText());
			if (i++ % 2 == 0) itemText.getStyleClass().add("label2");
			GridPane.setConstraints(itemText, 1, 0);

			entryPane.getRowConstraints().addAll(entryRowConstraints);
			entryPane.getColumnConstraints().addAll(iconColumnConstraints, textColumnConstraints);
			entryPane.getChildren().addAll(icon, itemText);

			entryPane.setOnMouseClicked((e) -> {
				fire(addedItem);
			});

			menuItemToNodeMap.put(addedItem, entryPane);
			menuPane.getChildren().add(entryPane);

		}
	}

	private void fire(ActionEntry action) {
		if (getSkinnable().getOnAction() != null) {
			getSkinnable().getOnAction().invoke(action);
		}
	}

	private void removeMenuItemNodes(List<? extends ActionEntry> toRemovedItems) {
		for (ActionEntry removedItem : toRemovedItems) {
			Node node = menuItemToNodeMap.remove(removedItem);
			if (node != null) menuPane.getChildren().remove(node);
		}
	}
}
