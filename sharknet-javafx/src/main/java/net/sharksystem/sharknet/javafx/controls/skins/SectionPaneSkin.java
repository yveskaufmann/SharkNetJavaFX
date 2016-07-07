package net.sharksystem.sharknet.javafx.controls.skins;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.animation.*;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
import net.sharksystem.sharknet.javafx.controls.SectionPane;
import net.sharksystem.sharknet.javafx.controls.behaviour.SectionPaneBehaviour;
import net.sharksystem.sharknet.javafx.utils.NodeUtils;


/**
 * The skin of a section pane.
 *
 * @Author Yves Kaufmann
 * @since 03.07.2016
 */
public class SectionPaneSkin extends BehaviorSkinBase<SectionPane, SectionPaneBehaviour> {


	private static final Duration TRANSITION_DURATION = new Duration(350.0);

	// caching results in poorer looking text (it is blurry), so we don't do it
	// unless on a low powered device (admittedly the test below isn't a great
	// indicator of power, but it'll do for now).
	private static final boolean CACHE_ANIMATION = PlatformUtil.isEmbedded();

	private static final String CONTENT_PROPERTY = "CONTENT";
	private static final String EXPANDED_PROPERTY = "EXPANDED";
	private static final String COLLAPSIBLE_PROPERTY = "COLLAPSIBLE";
	private static final String ALIGNMENT_PROPERTY = "ALIGNMENT";
	private static final String WIDTH_PROPERTY = "WIDTH";
	private static final String HEIGHT_PROPERTY = "HEIGHT";
	private static final String TITLE_REGION_ALIGNMENT_PROPERTY = "TITLE_REGION_ALIGNMENT";
	private static final String TEXT_PROPERTY = "TEXT";
	private static final String GRAPHIC_PROPERTY = "GRAPHIC";
	private static final String FONT_PROPERTY = "FONT";
	private static final String TITLE_CONTROL_PROPERTY = "title_control_property";
	private static final String STYLE_PROPERTY = "STYLE";

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private StackPane contentContainer;
	private Node content;
	private Rectangle clipRect;
	private TitleRegion titleRegion;
	private Pos pos;
	private HPos hpos;
	private VPos vpos;
	private double transitionStartValue = 1.0;
	private Timeline timeline;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	/**
	 * Constructor for all BehaviorSkinBase instances.
	 *
	 * @param sectionPane  The sectionPane for which this Skin should attach to.
	 */
	public SectionPaneSkin(SectionPane sectionPane) {
		super(sectionPane, new SectionPaneBehaviour(sectionPane));

		transitionStartValue = 1.0;

		titleRegion = new TitleRegion();

		content = getSkinnable().getContent();
		contentContainer = new StackPane() {
			{
				getStyleClass().setAll("content");
				if (content != null) {
					getChildren().add(content);
				}
			}
		};
		clipRect = new Rectangle();
		contentContainer.setClip(clipRect);

		if (sectionPane.isExpanded()) {
			setTransition(1.0f);
			setExpanded(sectionPane.isExpanded());
		} else {
			setTransition(0.0f);
			if (content != null) {
				content.setVisible(false);
			}
		}

		getChildren().addAll(contentContainer, titleRegion);

		registerChangeListener(sectionPane.titleControlProperty(), TITLE_CONTROL_PROPERTY);
		registerChangeListener(sectionPane.textProperty(), TEXT_PROPERTY);
		registerChangeListener(sectionPane.graphicProperty(), GRAPHIC_PROPERTY);
		registerChangeListener(sectionPane.graphicProperty(), FONT_PROPERTY);
		registerChangeListener(sectionPane.contentProperty(), CONTENT_PROPERTY);
		registerChangeListener(sectionPane.expandedProperty(), EXPANDED_PROPERTY);
		registerChangeListener(sectionPane.collapsibleProperty(), COLLAPSIBLE_PROPERTY);
		registerChangeListener(sectionPane.alignmentProperty(), ALIGNMENT_PROPERTY);
		registerChangeListener(sectionPane.widthProperty(), WIDTH_PROPERTY);
		registerChangeListener(sectionPane.heightProperty(), HEIGHT_PROPERTY);
		registerChangeListener(sectionPane.styleProperty(), STYLE_PROPERTY);
		registerChangeListener(titleRegion.alignmentProperty(), TITLE_REGION_ALIGNMENT_PROPERTY);

		pos = sectionPane.getAlignment();
		hpos = pos == null ? HPos.LEFT   : pos.getHpos();
		vpos = pos == null ? VPos.CENTER : pos.getVpos();
	}

	@Override
	protected void handleControlPropertyChanged(String propertyReference) {
		super.handleControlPropertyChanged(propertyReference);
		switch (propertyReference) {
			case CONTENT_PROPERTY:
				content = getSkinnable().getContent();
				if (content != null) {
					contentContainer.getChildren().clear();
				} else {
					contentContainer.getChildren().add(content);
				}
				break;
			case WIDTH_PROPERTY:
				clipRect.setWidth(getSkinnable().getWidth());
				break;
			case HEIGHT_PROPERTY:
				clipRect.setHeight(getSkinnable().getHeight());
				break;
			case TEXT_PROPERTY:
			case GRAPHIC_PROPERTY:
			case FONT_PROPERTY:
			case TITLE_CONTROL_PROPERTY:
			case STYLE_PROPERTY:
				titleRegion.update();
			break;

		}
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/


	private DoubleProperty transition;

	/**
	 * Returns the state of transition of the collapse animation
	 *
	 * @return the state of the collapse animation
	 */
	public double getTransition() {
		return transition == null ? 0.0 : transition.get();
	}

	/**
	 * Set the state of transition of the collapse animation.
	 *
	 * @param transition the state of the collapse animation
	 */
	public void setTransition(double transition) {
		transitionProperty().set(transition);
	}

	/***
	 * Returns the transition property which describes the state
	 * of the collapse animation
	 *
	 * @return
	 */
	private DoubleProperty transitionProperty() {
		if (transition == null) {
			transition = new SimpleDoubleProperty(this, "transition") {
				@Override
				protected void invalidated() {
					contentContainer.requestLayout();
				}
			};
		}
		return transition;
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/



	private void setExpanded(boolean expanded) {
		if (! getSkinnable().isCollapsible()) {
			setTransition(1.0f);
			return;
		}

		// we need to perform the transition between expanded / hidden
		if (getSkinnable().isAnimated()) {
			transitionStartValue = getTransition();
			doAnimationTransition();
		} else {
			if (expanded) {
				setTransition(1.0f);
			} else {
				setTransition(0.0f);
			}
			if (content != null) {
				content.setVisible(expanded);
			}
			getSkinnable().requestLayout();
		}
	}

	private void doAnimationTransition() {
		if (content == null) {
			return;
		}

		Duration duration;
		if (timeline != null && (timeline.getStatus() != Animation.Status.STOPPED)) {
			duration = timeline.getCurrentTime();
			timeline.stop();
		} else {
			duration = TRANSITION_DURATION;
		}

		timeline = new Timeline();
		timeline.setCycleCount(1);

		KeyFrame k1, k2;

		if (getSkinnable().isExpanded()) {
			k1 = new KeyFrame(
				Duration.ZERO,
				event -> {
					// start expand
					if (CACHE_ANIMATION) content.setCache(true);
					content.setVisible(true);
				},
				new KeyValue(transitionProperty(), transitionStartValue)
			);

			k2 = new KeyFrame(
				duration,
				event -> {
					// end expand
					if (CACHE_ANIMATION) content.setCache(false);
				},
				new KeyValue(transitionProperty(), 1, Interpolator.LINEAR)

			);
		} else {
			k1 = new KeyFrame(
				Duration.ZERO,
				event -> {
					// Start collapse
					if (CACHE_ANIMATION) content.setCache(true);
				},
				new KeyValue(transitionProperty(), transitionStartValue)
			);

			k2 = new KeyFrame(
				duration,
				event -> {
					// end collapse
					content.setVisible(false);
					if (CACHE_ANIMATION) content.setCache(false);
				},
				new KeyValue(transitionProperty(), 0, Interpolator.LINEAR)
			);
		}

		timeline.getKeyFrames().setAll(k1, k2);
		timeline.play();
	}

	/******************************************************************************
	 *
	 * Layout-Methods
	 *
	 ******************************************************************************/

	@Override protected void layoutChildren(final double x, double y,
											final double w, final double h) {
		double titleHeight = snapSize(titleRegion.prefHeight(-1));
		titleRegion.resize(w, titleHeight);
		positionInArea(titleRegion, x, y, w, titleHeight, 0, HPos.CENTER, VPos.CENTER);

		// content
		double contentHeight = (h - titleHeight) * getTransition();
		contentHeight = snapSize(contentHeight);

		y += snapSize(titleHeight);
		contentContainer.resize(w, contentHeight);
		clipRect.setHeight(contentHeight);
		positionInArea(contentContainer, x, y,
			w, contentHeight, 0, HPos.CENTER, VPos.CENTER);

	}

	@Override
	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		double titleWidth = snapSize(titleRegion.prefWidth(height));
		double contentWidth = snapSize(contentContainer.minWidth(height));
		return Math.max(titleWidth, contentWidth) + leftInset + rightInset;
	}

	@Override protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		double headerHeight = snapSize(titleRegion.prefHeight(width));
		double contentHeight = contentContainer.minHeight(width) * getTransition();
		return headerHeight + snapSize(contentHeight) + topInset + bottomInset;
	}

	@Override
	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		double titleWidth = snapSize(titleRegion.prefWidth(height));
		double contentWidth = snapSize(contentContainer.prefWidth(height));
		return Math.max(titleWidth, contentWidth) + leftInset + rightInset;
	}

	@Override protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		double headerHeight = snapSize(titleRegion.prefHeight(width));
		double contentHeight = contentContainer.prefHeight(width) * getTransition();
		return headerHeight + snapSize(contentHeight) + topInset + bottomInset;
	}

	@Override
	protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		return Double.MAX_VALUE;
	}


	class TitleRegion extends StackPane {
		private final StackPane arrowRegion;
		Label label = null;
		Node titleControl = null;

		public TitleRegion() {
			getStyleClass().setAll("title");

			label = new Label();

			arrowRegion = new StackPane();
			arrowRegion.setId("arrowRegion");
			arrowRegion.getStyleClass().setAll("arrow-button");

			StackPane arrow = new StackPane();
			arrow.setId("arrow");
			arrow.getStyleClass().setAll("arrow");
			arrowRegion.getChildren().setAll(arrow);

			// RT-13294: TitledPane : add animation to the title arrow
			arrow.rotateProperty().bind(new DoubleBinding() {
				{ bind(transitionProperty()); }

				@Override protected double computeValue() {
					return -90 * (1.0 - getTransition());
				}
			});

			setAlignment(Pos.CENTER_LEFT);

			setOnMouseReleased(e -> {
				if( e.getButton() != MouseButton.PRIMARY ) return;
				ContextMenu contextMenu = getSkinnable().getContextMenu() ;
				if (contextMenu != null) {
					contextMenu.hide();
				}
				if (getSkinnable().isCollapsible() && getSkinnable().isFocused()) {
					getBehavior().toggle();
				}
			});

			// title region consists of the title and the arrow regions
			update();
		}

		private void update() {
			getChildren().clear();
			final SectionPane sectionPane = getSkinnable();

			if (sectionPane.isCollapsible()) {
				getChildren().add(arrowRegion);
			}
			getChildren().add(label);

			if (sectionPane.getTitleControl() != null) {
				titleControl = sectionPane.getTitleControl();
				getChildren().add(titleControl);
			} else {
				titleControl = null;
			}

			label.setGraphic(sectionPane.getGraphic());
			label.setText(sectionPane.getText());
			label.setFont(sectionPane.getFont());

			setCursor(getSkinnable().isCollapsible() ? Cursor.HAND : Cursor.DEFAULT);
			requestLayout();
		}

		@Override protected double computePrefWidth(double height) {
			double left = snappedLeftInset();
			double right = snappedRightInset();
			double arrowWidth = 0;
			double titleControlWidth = 0;
			double labelPrefWidth = labelPrefWidth(height);

			if (arrowRegion != null) {
				arrowWidth = snapSize(arrowRegion.prefWidth(height));
			}

			if (titleControl != null) {
				titleControlWidth = snapSize(titleControl.prefWidth(height));
			}

			return left + arrowWidth + labelPrefWidth + titleControlWidth + right;
		}

		@Override protected double computePrefHeight(double width) {
			double top = snappedTopInset();
			double bottom = snappedBottomInset();
			double arrowHeight = 0;
			double titleControlHeight = 0;
			double labelPrefHeight = labelPrefHeight(width);

			if (arrowRegion != null) {
				arrowHeight = snapSize(arrowRegion.prefHeight(width));
			}

			if (titleControl != null) {
				titleControlHeight = snapSize(titleControl.prefHeight(width));
			}

			return top + Math.max(Math.max(arrowHeight, labelPrefHeight), titleControlHeight) + bottom;
		}

		@Override protected void layoutChildren() {
			final double top = snappedTopInset();
			final double bottom = snappedBottomInset();
			final double left = snappedLeftInset();
			final double right = snappedRightInset();
			double width = getWidth() - (left + right);
			double height = getHeight() - (top + bottom);
			double arrowWidth = snapSize(arrowRegion.prefWidth(-1));
			double arrowHeight = snapSize(arrowRegion.prefHeight(-1));
			double labelWidth = snapSize(Math.min(width - arrowWidth / 2.0, labelPrefWidth(-1)));
			double labelHeight = snapSize(labelPrefHeight(-1));

			arrowRegion.resize(arrowWidth, arrowHeight);
			positionInArea(arrowRegion, left, top, arrowWidth, height,
                    /*baseline ignored*/0, HPos.CENTER, VPos.CENTER);

			label.resize(labelWidth, labelHeight);
			positionInArea(label, left + arrowWidth, top, labelWidth, height, 0, HPos.CENTER, VPos.CENTER);

			if (titleControl != null) {
				double controlWidth = snapSize(titleControl.prefWidth(-1));
				double controlHeight = snapSize(titleControl.prefHeight(-1));
				titleControl.resize(controlWidth, controlHeight);
				positionInArea(titleControl, width - controlWidth + right, top, controlWidth , height, 0, HPos.CENTER, VPos.CENTER);
			}
		}

		// Copied from LabeledSkinBase because the padding from TitledPane was being
		// applied to the Label when it should not be.
		private double labelPrefWidth(double height) {
			// Get the preferred width of the text
			final Labeled labeled = label;
			final Font font = label.getFont();
			final String string = labeled.getText();
			boolean emptyText = string == null || string.isEmpty();
			Insets labelPadding = labeled.getLabelPadding();
			double widthPadding = labelPadding.getLeft() + labelPadding.getRight();
			double textWidth = emptyText ? 0 : NodeUtils.computeTextWidth(font, string, 0);
			double graphicWidth = 0;

			final Node graphic = labeled.getGraphic();
			if (graphic != null) {
				graphicWidth = graphic.prefWidth(-1);
			}

		 	if (labeled.getContentDisplay() == ContentDisplay.LEFT
				|| labeled.getContentDisplay() == ContentDisplay.RIGHT) {
				return textWidth + labeled.getGraphicTextGap() + graphicWidth + widthPadding;
			} else {
				return Math.max(textWidth, graphicWidth) + widthPadding;
			}
		}

		// Copied from LabeledSkinBase because the padding from TitledPane was being
		// applied to the Label when it should not be.
		private double labelPrefHeight(double width) {
			final Labeled labeled = label;
			final Font font = label.getFont();
			final ContentDisplay contentDisplay = labeled.getContentDisplay();
			final double gap = labeled.getGraphicTextGap();
			final Insets labelPadding = labeled.getLabelPadding();
			final double widthPadding = snappedLeftInset() + snappedRightInset() + labelPadding.getLeft() + labelPadding.getRight();
			double graphicHeight = 0;

			String str = labeled.getText();
			if (str != null && str.endsWith("\n")) {
				// Strip ending newline so we don't count another row.
				str = str.substring(0, str.length() - 1);
			}

			if ((contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT)) {
				if (label.getGraphic() != null) {
					width -= (label.getGraphic().prefWidth(-1) + gap);
				}
			}

			width -= widthPadding;

			// TODO figure out how to cache this effectively.
			final double textHeight = NodeUtils.computeTextHeight(font, str,
				labeled.isWrapText() ? width : 0, 0,  TextBoundsType.LOGICAL);

			// Now we want to add on the graphic if necessary!
			double h = textHeight;
			final Node graphic = labeled.getGraphic();

			if (graphic != null) {
				graphicHeight = graphic.prefHeight(-1);
			}

			if (contentDisplay == ContentDisplay.TOP || contentDisplay == ContentDisplay.BOTTOM) {
				h = graphicHeight + gap + textHeight;
			} else {
				h = Math.max(textHeight, graphicHeight);
			}

			return h + labelPadding.getTop() + labelPadding.getBottom();
		}
	}
}
