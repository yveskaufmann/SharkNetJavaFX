package net.sharksystem.sharknet.javafx.controls;


import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import net.sharksystem.sharknet.javafx.controls.animations.DoublePropertyTransition;

import java.util.function.Consumer;

public class Workbench extends StackPane {

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

		public boolean isDirectionHorizontal() {
			return Left.equals(this) || Right.equals(this);
		}

		public boolean isDirectionVertical() {
			return Top.equals(this) || Bottom.equals(this);
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
	private DoubleBinding sidebarHiddenTranslationBinding;

	private Point2D mousePressPoint;
	private double startTranslation;

	private DoublePropertyTransition inAnimation;
	private DoublePropertyTransition outAnimation;


	private Button button;

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

		StackPane.setAlignment(content, Pos.CENTER);
		content.minWidthProperty().bind(prefWidthProperty());
		content.maxWidthProperty().bind(prefWidthProperty());

		getChildren().add(content);
		getChildren().add(overlay);
		getChildren().add(sidebar);

		initialize();


	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void initialize() {


		sidebarHiddenTranslationBinding = Bindings.createDoubleBinding(()-> getSidebarPosition().getDirection() * getSidebarWidth());

		sidebarTranslate.addListener((observable, oldValue, newValue) -> {
			double opacity = 1.0 - Math.abs(newValue.doubleValue() / sidebarHiddenTranslationBinding.get());
			overlay.setOpacity(opacity);
			overlay.setVisible(opacity > 0.0);
		});

		overlay.visibleProperty().addListener((observable, oldValue, newValue) -> {
			overlay.setStyle(!newValue ? "-fx-background-color: transparent;" : "");
			overlay.setMouseTransparent(!newValue);
			overlay.setPickOnBounds(newValue);
		});

		overlay.setOnMouseClicked((e) -> hideSidebar());

		sidebar.setOnMousePressed((e) -> {
			e.consume();
			mousePressPoint = new Point2D(e.getSceneX(), e.getSceneY());
			startTranslation = sidebar.translateXProperty().get();
		});

		sidebar.setOnMouseDragged((e) -> {
			double eventPoint = e.getSceneX();
			if (sidebarTranslate.get() >= -sidebarWidthProperty().get() && -getSidebarPosition().getDirection() * sidebarTranslate.get() <= 0 ) {
				sidebarTranslate.setValue(startTranslation + eventPoint - mousePressPoint.getX());
			}
		});

		sidebar.setOnMouseReleased((e) -> {
			boolean isTranslationOver50Percent = Math.abs(sidebarTranslate.get()) > getSidebarPosition().getDirection() * sidebarHiddenTranslationBinding.get() * 0.5;
			if (isTranslationOver50Percent) {
				hideSidebar();
			} else {
				showSidebar();
			}
		});

		updateSidebarPosition();
	}

	private void updateSidebarPosition() {
		SidebarPosition sidebarPosition = sidebarPositionProperty().get();
		Consumer<Double> setSidebarSize = (size) -> {
			sidebar.setMinWidth(-1);  sidebar.setMinHeight(-1);
			sidebar.setPrefWidth(-1); sidebar.setPrefHeight(-1);
			sidebar.setMaxWidth(-1);  sidebar.setMaxHeight(-1);

			if (sidebarPosition.isDirectionHorizontal()) {
				sidebar.setMinWidth(size);
				sidebar.setPrefWidth(size);
				sidebar.setMaxWidth(size);
			} else {
				sidebar.setMinHeight(size);
				sidebar.setPrefHeight(size);
				sidebar.setMaxHeight(size);
			}

		};

		sidebarTranslate.set(0);
		sidebarTranslate.unbind();

		switch (sidebarPosition) {
			case Left:
				StackPane.setAlignment(sidebar, Pos.CENTER_LEFT);
				sidebarTranslate.bindBidirectional(sidebar.translateXProperty());
				break;
			case Right:
				StackPane.setAlignment(sidebar, Pos.CENTER_RIGHT);
				sidebarTranslate.bindBidirectional(sidebar.translateXProperty());
				break;
			case Bottom:
				StackPane.setAlignment(sidebar, Pos.BOTTOM_CENTER);
				sidebarTranslate.bindBidirectional(sidebar.translateYProperty());
				break;
			case Top:
				StackPane.setAlignment(sidebar, Pos.TOP_CENTER);
				sidebarTranslate.bindBidirectional(sidebar.translateYProperty());
				break;
		}

		sidebarHiddenTranslationBinding.invalidate();
		sidebarTranslate.set(1.0); // Ensures that sidebarTranslate is invalidated in the next call
		sidebarTranslate.set(isSidebarPinned() ? 0 : sidebarHiddenTranslationBinding.getValue());
		setSidebarSize.accept(getSidebarWidth());
		updateAnimations();
	}



	private void showSidebar() {
		if (inAnimation == null || Animation.Status.RUNNING.equals(inAnimation.getStatus())) return;
		/*
		Bounds bound = getLayoutBounds();

		Timeline timeline = new Timeline(
			new KeyFrame(Duration.ZERO,
				new KeyValue(content.prefWidthProperty(), content.getWidth(), Interpolator.EASE_BOTH),
				new KeyValue(content.translateXProperty(), content.getTranslateX(), Interpolator.EASE_BOTH)),
			new KeyFrame(Duration.millis(200),
				new KeyValue(content.prefWidthProperty(), bound.getWidth() - getSidebarWidth(), Interpolator.EASE_BOTH),
				new KeyValue(content.translateXProperty(), getSidebarWidth(), Interpolator.EASE_BOTH))
		);
		ParallelTransition transition = new ParallelTransition(inAnimation, timeline);
		*/

		inAnimation.setOnFinished((e) -> {
			setSidebarPinned(true);
		});
		inAnimation.play();

	}

	private void hideSidebar() {
		if (outAnimation == null || Animation.Status.RUNNING.equals(outAnimation.getStatus())) return;
		/*
		Bounds bound = getLayoutBounds();
		Timeline timeline = new Timeline(
			new KeyFrame(Duration.ZERO,
				new KeyValue(content.prefWidthProperty(), content.getWidth(), Interpolator.EASE_BOTH),
				new KeyValue(content.translateXProperty(), content.getTranslateX(), Interpolator.EASE_BOTH)),
			new KeyFrame(Duration.millis(200),
				new KeyValue(content.prefWidthProperty(), bound.getWidth(), Interpolator.EASE_BOTH),
				new KeyValue(content.translateXProperty(), 0, Interpolator.EASE_BOTH))
		);
			ParallelTransition transition = new ParallelTransition(timeline);
		*/

		outAnimation.setOnFinished((e) -> {
			setSidebarPinned(false);
		});
		outAnimation.play();

	}

	/**
	 * Open and close the sidebar.
	 */
	public void toggleSidebar() {
		setSidebarPinned(!isSidebarPinned());
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
			sidebarPosition = new SimpleObjectProperty<SidebarPosition>(this, "sidebarPosition", SidebarPosition.Left) {
				@Override
				protected void invalidated() {
					updateSidebarPosition();
				}
			};

		}
		return sidebarPosition;
	}

	/**
	 *
	 * @see #sidebarPositionProperty()
     */
	public SidebarPosition getSidebarPosition() {
		return sidebarPosition == null ? SidebarPosition.Left : sidebarPosition.get();
	}

	/**
	 * @see #sidebarPositionProperty()
     */
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
			sidebarPinned = new SimpleBooleanProperty(this, "sidebarPinned") {

				/**
				 * Must memorize old state in order to prevent animation loops
				 */
				boolean wasPinned = false;

				@Override
				protected void invalidated() {
					boolean shouldPinned = get();

					if (wasPinned && !shouldPinned) {
						hideSidebar();
					}

					if (!wasPinned && shouldPinned) {
						showSidebar();
					}

					wasPinned = shouldPinned;
				}
			};
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
	public DoubleProperty sidebarWidthProperty() {
		if (sidebarWidth == null) {
			sidebarWidth = new SimpleDoubleProperty(this, "sidebarWidthProperty", 250) {
				@Override
				protected void invalidated() {
					updateSidebarPosition();
				}
			};

		}
		return sidebarWidth;
	}

	public double getSidebarWidth() {
		return sidebarWidthProperty().get();
	}

	public void setSidebarWidth(double width) {
		sidebarWidthProperty().setValue(width);
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
	 * Event-Handling
	 *                                                                              
	 ******************************************************************************/



	/******************************************************************************
	 *
	 * Animations
	 *
	 ******************************************************************************/

	private void updateAnimations() {
		updateSidebarInAnimation();
		updateSidebarOutAnimation();
	}

	private void updateSidebarInAnimation() {

		if (inAnimation == null) {
			inAnimation = new DoublePropertyTransition(Duration.millis(200), sidebarTranslate, 0);
			inAnimation.setInterpolator(Interpolator.EASE_OUT);
		}
		inAnimation.jumpTo(Duration.ZERO);
		inAnimation.setTargetValue(0);
	}

	private void updateSidebarOutAnimation() {

		if (outAnimation == null) {
			outAnimation = new DoublePropertyTransition(Duration.millis(200), sidebarTranslate, sidebarHiddenTranslationBinding.get());
			outAnimation.setInterpolator(Interpolator.EASE_OUT);
		}
		outAnimation.jumpTo(Duration.ZERO);
		if (sidebarHiddenTranslationBinding != null ) sidebarHiddenTranslationBinding.invalidate();
		outAnimation.setTargetValue(sidebarHiddenTranslationBinding.get());
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
