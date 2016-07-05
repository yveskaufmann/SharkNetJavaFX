package net.sharksystem.sharknet.javafx.controls;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Labeled;
import javafx.scene.control.Skin;
import javafx.scene.control.TitledPane;
import net.sharksystem.sharknet.javafx.controls.skins.SectionPaneSkin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A section pane allows it to separate controls
 * to a specific context which can be described
 * by a title it is like a {#SectionPane} with
 * various extensions like buttons in the title bar.
 *
 * @Author Yves Kaufmann
 * @since 03.07.2016
 */
@DefaultProperty("content")
public class SectionPane extends TitledPane {

	/***************************************************************************
	 *                                                                         *
	 * Constructors                                                            *
	 *                                                                         *
	 **************************************************************************/

	/**
	 * Creates a new section with no title or content.
	 */
	public SectionPane() {
		super();
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		setCollapsible(false);
		setContentDisplay(ContentDisplay.RIGHT);

	}

	/***************************************************************************
	 *                                                                         *
	 * Properties                                                              *
	 *                                                                         *
	 **************************************************************************/


	// --- title control
	private ObjectProperty<Node> titleControl;

	/**
	 * <p> The title control of the SectionPane which can be any Node
	 * such as UI controls or groups of nodes added to a layout container.
	 *
	 * <p>The title control is displayed on the right side of the title</p>
	 *
	 * @param value The title control for this SectionPane.
	 */
	public final void setTitleControl(Node value) {
		titleControlProperty().set(value);
	}

	/**
	 * The title control of the SectionPane.  {@code Null} is returned when
	 * if there is no content.
	 *
	 * @return The content of this SectionPane.
	 */
	public final Node getTitleControl() {
		return titleControl == null ? null : titleControl.get();
	}

	/**
	 * The title control of the SectionPane.
	 *
	 * @return The title control of the SectionPane.
	 */
	public final ObjectProperty<Node> titleControlProperty() {
		if (titleControl == null) {
			titleControl = new SimpleObjectProperty<Node>(this, "titleControl");
		}
		return titleControl;
	}
	

	/***************************************************************************
	 *                                                                         *
	 * Methods                                                                 *
	 *                                                                         *
	 **************************************************************************/

	/** {@inheritDoc} */
	@Override protected Skin<?> createDefaultSkin() {
		return new SectionPaneSkin(this);
	}

	/***************************************************************************
	 *                                                                         *
	 * Stylesheet Handling                                                     *
	 *                                                                         *
	 **************************************************************************/

	private static final String DEFAULT_STYLE_CLASS = "section-pane";

	private static class StyleableProperties {

		private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
		static {
			final List<CssMetaData<? extends Styleable, ?>> styleables =
				new ArrayList<CssMetaData<? extends Styleable, ?>>(Labeled.getClassCssMetaData());
			// TODO: add styleables
			STYLEABLES = Collections.unmodifiableList(styleables);
		}
	}

	/**
	 * @return The CssMetaData associated with this class, which may include the
	 * CssMetaData of its super classes.
	 * @since JavaFX 8.0
	 */
	public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
		return SectionPane.StyleableProperties.STYLEABLES;
	}

	/**
	 * {@inheritDoc}
	 * @since JavaFX 8.0
	 */
	@Override
	public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
		return getClassCssMetaData();
	}

}
