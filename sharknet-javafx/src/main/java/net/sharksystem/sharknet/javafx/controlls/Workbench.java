package net.sharksystem.sharknet.javafx.controlls;


import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class Workbench extends StackPane {

	//TODO: Fix sidebar pinned property
	//TODO: Sidebar should closed when mouse is released
	//TODO: Animation must be toggleable
	//TODO: Fix SliderPosition for Top and Bottom
	//TODO: Mouse Support Drag the Menu

	/**
	 * Possible Positions of the sidebar
	 */
	public enum SidebarPosition {
		Left(-1.0),
		Right(1.0),
		Bottom(1.0),
		Top(-1.0);

		private double direction;

		 SidebarPosition(double outDirection) {
			this.direction = outDirection;
		}

		public double getDirection() {
			return direction;
		}
	}

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private StackPane content = new StackPane();
	private StackPane overlay = new StackPane();
	private StackPane sidebar = new StackPane();

	private BooleanProperty sidebarPinned;
	private DoubleProperty sidebarWidth;
	private ObjectProperty<SidebarPosition> sidebarPosition;

	private DoubleProperty sidebarTranslate = new SimpleDoubleProperty(this, "sidebarTranslate", 0.0);
	private DoubleProperty initialTranslation = new SimpleDoubleProperty(this, "initialTranslation", 0.0);

	private Point2D mousePressPoint;
	private double startTranslation;

	private Timeline inAnimation;
	private Timeline outAnimation;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public Workbench() {
		super();
		getStyleClass().add(DEFAULT_STYLE_CLASS);

		overlay.getStyleClass().add(DEFAULT_OVERLAY_STYLE_CLASS);
		overlay.setOpacity(0);
		overlay.setVisible(false);

		sidebar.getStyleClass().add(DEFAULT_SIDEBAR_STYLE_CLASS);
		sidebar.setPickOnBounds(false);

		getChildren().add(content);
		getChildren().add(overlay);
		getChildren().add(sidebar);

		initialize();
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	/**
	 * Specifies the position of the sidebar.
	 *
	 * @defaultValue SidebarPosition.Left
     */
	public ObjectProperty<SidebarPosition> sidebarPositionProperty() {
		if (sidebarPosition == null) {
			sidebarPosition = new SimpleObjectProperty<>(this, "sidebarPosition", SidebarPosition.Left);
		}
		return sidebarPosition;
	}

	public SidebarPosition getSidebarPosition() {
		return sidebarPositionProperty().get();
	}

	public void setSidebarPosition(SidebarPosition sidebarPosition) {
		sidebarPositionProperty().setValue(sidebarPosition);
	}

	/**
	 * Defines if the sidebar is shown or not.
	 *
	 * @defaultValue false the sidebar isn't shown.
     */
	public BooleanProperty sidebarPinnedProperty() {
		if (sidebarPinned == null) {
			sidebarPinned = new SimpleBooleanProperty(this, "sidebarPinned");
		}
		return sidebarPinned;
	}

	/**
	 *
	 * @return
	 * @see #sidebarPinnedProperty()
     */
	public boolean isSidebarPinned() {
		return sidebarPinnedProperty().get();
	}

	/**
	 * @see #sidebarPinnedProperty()
     */
	public void setSidebarPinned(boolean pinned) {
		sidebarPinnedProperty().set(pinned);
	}

	/**
	 * Defines the Max-Width of the sidebar.
	 *
	 * @defaultValue 250
     */
	public DoubleProperty sidebarWidth() {
		if (sidebarWidth == null) {
			sidebarWidth = new SimpleDoubleProperty(this, "sidebarWidth", 250);
		}
		return sidebarWidth;
	}

	public double getSidebarWidth() {
		return sidebarWidth().get();
	}

	public void setSidebarWidth(double width) {
		sidebarWidth().setValue(width);
	}

	public ObservableList<Node> getContent() {
		return content.getChildren();
	}

	public void setContent(Node ...node) {
		content.getChildren().setAll(node);
	}

	public ObservableList<Node> getSidebar() {
		return sidebar.getChildren();
	}

	public void setSidebar(Node ...node) {
		sidebar.getChildren().setAll(node);
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void initialize() {

		initialTranslation.bind(Bindings.createDoubleBinding(()-> getSidebarPosition().getDirection() * sidebarWidth().get()));

		overlay.visibleProperty().addListener((observable, oldValue, newValue) -> {
			overlay.setStyle(!newValue ? "-fx-background-color: transparent;" : "");
			overlay.setMouseTransparent(!newValue);
			overlay.setPickOnBounds(newValue);
		});

		sidebarPositionProperty().addListener((observable, oldValue, newValue) -> updateSidebarPosition(newValue));

		sidebarTranslate.addListener((observable, oldValue, newValue) -> {
			double opacity = 1.0 + (newValue.doubleValue() / -initialTranslation.doubleValue());
			overlay.setOpacity(opacity);
			overlay.setVisible(opacity > 0.0);
		});


		overlay.setOnMouseClicked((e) -> hideSidebar());
		sidebar.setOnMousePressed((e) -> {
			e.consume();
			mousePressPoint = new Point2D(e.getSceneX(), e.getSceneY());
			startTranslation = sidebar.translateXProperty().get();
		});
		sidebar.setOnMouseDragged((e) -> {
			double eventPoint = e.getSceneX();

			if (startTranslation + eventPoint < sidebarWidth().get() ) {
				sidebarTranslate.setValue(startTranslation + eventPoint - mousePressPoint.getX());
			}

		});
		updateSidebarPosition(getSidebarPosition());
		System.out.println(isSidebarPinned());
		if (isSidebarPinned()) {
			sidebarTranslate.set(0);
		} else {
			sidebarTranslate.set(-getSidebarWidth());

		}
	}

	private void updateSidebarPosition(SidebarPosition sidebarPosition) {
		sidebar.setMaxWidth(-1);
		sidebar.prefWidth(-1);
		sidebar.setPrefHeight(-1);
		sidebar.setMaxHeight(-1);

		switch (sidebarPosition) {
			case Left:
				StackPane.setAlignment(sidebar, Pos.CENTER_LEFT);
				sidebarTranslate.set(0);
				sidebarTranslate.unbind();
				sidebarTranslate.bindBidirectional(sidebar.translateXProperty());
				sidebar.setMaxWidth(getSidebarWidth());
				break;
			case Right:
				StackPane.setAlignment(sidebar, Pos.CENTER_RIGHT);
				sidebarTranslate.set(0);
				sidebarTranslate.unbind();
				sidebarTranslate.bindBidirectional(sidebar.translateXProperty());
				sidebar.setMaxWidth(getSidebarWidth());

				break;
			case Bottom:
				StackPane.setAlignment(sidebar, Pos.BOTTOM_CENTER);
				sidebarTranslate.set(0);
				sidebarTranslate.unbind();
				sidebarTranslate.bindBidirectional(sidebar.translateYProperty());
				sidebar.setMaxHeight(getSidebarWidth());
				sidebar.setPrefHeight(getSidebarWidth());
				break;
			case Top:
				StackPane.setAlignment(sidebar, Pos.TOP_CENTER);
				sidebarTranslate.set(0);
				sidebarTranslate.unbind();
				sidebarTranslate.bindBidirectional(sidebar.translateYProperty());
				sidebar.setPrefHeight(getSidebarWidth());
				sidebar.setMaxHeight(getSidebarWidth());
				break;
		}

		createAnimations(initialTranslation.doubleValue());
	}

	public void showSidebar() {
		if (inAnimation.getStatus().equals(Animation.Status.RUNNING)) return;
		inAnimation.play();
		inAnimation.setOnFinished((e) -> {
			setSidebarPinned(true);
		});

	}

	public void hideSidebar() {
		if (outAnimation.getStatus().equals(Animation.Status.RUNNING)) return;
		outAnimation.play();
		outAnimation.setOnFinished((e) -> {
			setSidebarPinned(false);
		});
	}

	/******************************************************************************
	 *                                                                             
	 * Event-Handling
	 *                                                                              
	 ******************************************************************************/



	/******************************************************************************
	 *
	 * Animations
	 *
	 ******************************************************************************/

	private void createAnimations(double initState) {;
		inAnimation = createSidebarInAnimation(initState, 0);
		outAnimation = createSidebarOutAnimation(initState, 0);
	}

	private Timeline createSidebarInAnimation(double start, double end) {
		return new Timeline(
			new KeyFrame(Duration.ZERO,
				new KeyValue(overlay.visibleProperty(), false, Interpolator.EASE_BOTH)
			),
			new KeyFrame(Duration.millis(100),
				new KeyValue(sidebarTranslate, start, Interpolator.EASE_BOTH),
				new KeyValue(overlay.visibleProperty(), true, Interpolator.EASE_BOTH)
			),
			new KeyFrame(Duration.millis(400),
				new KeyValue(overlay.opacityProperty(), 1, Interpolator.EASE_BOTH),
				new KeyValue(sidebarTranslate, end, Interpolator.EASE_BOTH)
			)
		);

	}

	private Timeline createSidebarOutAnimation(double start, double end) {
		return new Timeline(
			new KeyFrame(Duration.ZERO,
				new KeyValue(sidebarTranslate, end, Interpolator.EASE_BOTH)
			),
			new KeyFrame(Duration.millis(300),
				new KeyValue(sidebarTranslate, start, Interpolator.EASE_BOTH),
				new KeyValue(overlay.visibleProperty(), true, Interpolator.EASE_BOTH)
			),
			new KeyFrame(Duration.millis(400),
				new KeyValue(overlay.opacityProperty(), 0, Interpolator.EASE_BOTH),
				new KeyValue(overlay.visibleProperty(), false, Interpolator.EASE_BOTH)
			)
		);
	}

	/******************************************************************************
	 *
	 * Styling
	 *
	 ******************************************************************************/

	private static final String DEFAULT_STYLE_CLASS = "workbench";
	private static final String DEFAULT_SIDEBAR_STYLE_CLASS = "workbench-sidebar";
	private static final String DEFAULT_OVERLAY_STYLE_CLASS = "workbench-overlay";
}
