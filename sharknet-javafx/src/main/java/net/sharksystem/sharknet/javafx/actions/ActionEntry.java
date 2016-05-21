package net.sharksystem.sharknet.javafx.actions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import net.sharksystem.sharknet.javafx.i18n.I18N;
import net.sharksystem.sharknet.javafx.utils.FontBasedIcon;

import java.util.Optional;


public class ActionEntry {

	private ObjectProperty<FontBasedIcon> icon;
	private StringProperty title;
	private StringProperty tooltip;
	private ObjectProperty<ActionCallback> callback;

	public ActionEntry() {}
	public ActionEntry(FontBasedIcon icon) {
		setIcon(icon);
	}

	public ActionEntry(String text) {
		setTitle(text);
	}

	public ActionEntry(FontBasedIcon icon, String text) {
		setIcon(icon);
		setTitle(text);
	}

	public ActionEntry(FontBasedIcon icon, String text, ActionCallback callback) {
		setIcon(icon);
		setTitle(text);
		setCallback(callback);
	}

	public ActionEntry(FontBasedIcon icon, ActionCallback callback) {
		setIcon(icon);
		setCallback(callback);
	}

	public final Optional<FontBasedIcon> getIconOptional() {
		return icon != null ? Optional.of(icon.get()) : Optional.empty();
	}

	public final String getIcon() {
		Optional<FontBasedIcon> icon = getIconOptional();
		if (icon.isPresent()) {
			return icon.get().getText();
		}
		return "";
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
			title.addListener(this::convertI18NToString);
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
			tooltip.addListener(this::convertI18NToString);
		}
		return tooltip;
	}

	private void convertI18NToString(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (oldValue != newValue && newValue.startsWith("%")) {
			if (observable instanceof StringProperty) {
				StringProperty.class.cast(observable).setValue(I18N.getString(newValue));
			}
		}
	}

}
