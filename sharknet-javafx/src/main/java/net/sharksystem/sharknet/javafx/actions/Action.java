package net.sharksystem.sharknet.javafx.actions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.sharksystem.sharknet.javafx.utils.FontBasedIcon;

import java.util.Optional;


public class Action {

	private ObjectProperty<FontBasedIcon> icon;
	private StringProperty title;
	private StringProperty tooltip;
	private ObjectProperty<ActionCallback> callback;

	public Action() {}
	public Action(FontBasedIcon icon) {
		setIcon(icon);
	}

	public Action(String text) {
		setTitle(text);
	}

	public Action(FontBasedIcon icon, String text) {
		setIcon(icon);
		setTitle(text);
	}

	public Action(FontBasedIcon icon, String text, ActionCallback callback) {
		setIcon(icon);
		setTitle(text);
		setCallback(callback);
	}

	public Action(FontBasedIcon icon, ActionCallback callback) {
		setIcon(icon);
		setCallback(callback);
	}

	public final Optional<FontBasedIcon> getIcon() {
		return icon != null ? Optional.of(icon.get()) : Optional.empty();
	}

	public final void setIcon(FontBasedIcon icon) {
		iconProperty().set(icon);
	}

	public final ObjectProperty<FontBasedIcon> iconProperty() {
		if (icon == null) {
			icon = new SimpleObjectProperty<>(this, "icon");
		}
		return icon;
	}

	public final Optional<ActionCallback> getCallback() {
		return callback != null ? Optional.of(callback.get()) : Optional.empty();
	}

	public final void setCallback(ActionCallback callback) {
		callbackProperty().set(callback);
	}

	public final ObjectProperty<ActionCallback> callbackProperty() {
		if (callback == null) {
			callback = new SimpleObjectProperty<>(this, "callback");
		}
		return callback;
	}


	public final String getTitle() {
		return title != null ? title.get() : "";
	}

	public final void setTitle(String title) {
		if (title == null) title = "";
		titleProperty().set(title);
	}

	public final StringProperty titleProperty() {
		if (title == null) {
			title = new SimpleStringProperty(this, "title");
		}
		return title;
	}

	public final String getTooltip() {
		return tooltip != null ? tooltip.get() : "";
	}

	public final void setTooltip(String title) {
		if (title == null) title = "";
		titleProperty().set(title);
	}

	public final StringProperty tooltipProperty() {
		if (tooltip == null) {
			tooltip = new SimpleStringProperty(this, "tooltip");
		}
		return tooltip;
	}

}
