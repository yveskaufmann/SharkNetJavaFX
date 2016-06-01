package net.sharksystem.sharknet.javafx.controls.skins;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;

import java.util.List;

/**
 * Google Material Design Tabs
 *
 * Ensures that the tab title is in uppercase.
 */
public class TabPaneSkin extends com.sun.javafx.scene.control.skin.TabPaneSkin {

	private StackPane headersRegion ;

	public TabPaneSkin(TabPane tabPane) {
		super(tabPane);

		headersRegion = (StackPane) tabPane.lookup(".headers-region");

		/**
		 * Ensures that the title is in uppercase
		 */
		tabTitleToUppercase(tabPane.getTabs());
		tabPane.getTabs().addListener(new ListChangeListener<Tab>() {
			@Override
			public void onChanged(Change<? extends Tab> c) {
				while (c.next()) {
					tabTitleToUppercase(c.getAddedSubList());
				}
			}
		});
		getSkinnable().layout();
	}

	private void tabTitleToUppercase(List<? extends Tab> tabs) {
		for (Tab tab : tabs) {
			tab.setText(tab.getText().toUpperCase());
		}
	}

	@Override
	protected void layoutChildren(double x, double y, double w, double h) {
		super.layoutChildren(x, y, w, h);
		headersRegion.setTranslateX(w * 0.5 - headersRegion.prefWidth(-1) * 0.5);
	}
}
