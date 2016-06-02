package net.sharksystem.sharknet.javafx.controls;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import net.sharksystem.sharknet.javafx.utils.FontBasedIcon;


public class FontIcon extends Label {

	public static final String DEFAULT_STYLE_CLASS = "icon-label";

	public FontIcon(FontBasedIcon icon) {
		super(icon.getText());
		initialize();
	}

	public FontIcon(String icon) {
		super(icon);
		initialize();
	}

	private void initialize() {
		getStyleClass().addAll(DEFAULT_STYLE_CLASS);
	}

}
