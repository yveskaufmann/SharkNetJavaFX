package net.sharksystem.sharknet.javafx.controls;

import com.sun.javafx.css.converters.PaintConverter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import net.sharksystem.sharknet.javafx.controls.skins.TextFieldSkin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Textfield in Googles material design.
 */
public class TextField extends javafx.scene.control.TextField {

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private BooleanProperty labelEnabled;

	/******************************************************************************
	 *
	 * Constructor
	 *
	 ******************************************************************************/

	public TextField() {
		super();
		initialize();
	}

	public TextField(String text) {
		super(text);
		initialize();
	}

	private void initialize() {
		getStyleClass().add(DEFAULT_STYLE_CLASS);
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	/**
	 * Specifies if the prompt text should be used as label text.
	 * The label text is shown above the text field when
	 * the field contains text or owns the focus.
	 *
	 * @defaultValue per default labels are activated.
     */
	public final BooleanProperty labelEnabledProperty() {
		if (labelEnabled == null) {
			labelEnabled = new SimpleBooleanProperty(this, "labelEnabled", true);
		}
		return labelEnabled;
	}

	/**
	 * @see #labelEnabledProperty()
     */
	public boolean isLabelEnabled() {
		return (labelEnabled == null) || labelEnabled.get();
	}

	/**
	 * @see #labelEnabledProperty()
     */
	public void setLabel(boolean showLabel) {
		labelEnabledProperty().setValue(showLabel);
	}

	/**
	 * Defines the color of the cursor.
	 *
	 * @defaultValue Color.Black
	 */
	private ObjectProperty<Paint> cursorColor;

	public final void setCursorColor(Paint color) {
		cursorColorProperty().setValue(color);
	}

	public final Paint getCursorColor() {
		if (cursorColor == null) return Color.BLACK;
		return cursorColor.get();
	}

	public final ObjectProperty<Paint> cursorColorProperty() {
		if (cursorColor == null) {
			cursorColor = new StyleableObjectProperty<Paint>(Color.BLACK) {
				@Override
				public Object getBean() {return TextField.this;}

				@Override
				public String getName() {return "cursorColor";}

				@Override
				public CssMetaData<? extends Styleable, Paint> getCssMetaData() {
					return StyleableProperties.CURSOR_COLOR;
				}
			};
		}
		return cursorColor;
	}

	/******************************************************************************
	 *
	 * Styling
	 *
	 ******************************************************************************/

	private static final String DEFAULT_STYLE_CLASS = "material-design";

	@Override
	protected Skin<?> createDefaultSkin() {
		return new TextFieldSkin(this);
	}



	private static class StyleableProperties {
		private static final CssMetaData<TextField, Paint> CURSOR_COLOR = new CssMetaData<TextField, Paint>("-fx-cursor-color", PaintConverter.getInstance(), Color.BLACK) {
			@Override
			public boolean isSettable(TextField styleable) {
				return styleable.cursorColor == null || !styleable.cursorColor.isBound();
			}

			@Override
			public StyleableProperty<Paint> getStyleableProperty(TextField styleable) {
				return (StyleableProperty<Paint>) styleable.cursorColorProperty();
			}
		};
	}

	// inherit the styleable properties from parent
	private List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

	@Override
	public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
		if (STYLEABLES == null) {
			final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(Control.getClassCssMetaData());
			styleables.add(StyleableProperties.CURSOR_COLOR);
			styleables.addAll(getClassCssMetaData());
			STYLEABLES = Collections.unmodifiableList(styleables);
		}
		return STYLEABLES;
	}

}
