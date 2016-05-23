package net.sharksystem.sharknet.javafx.controller;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.animations.DoublePropertyTransition;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

/**
 * This controller is responsible to provide a
 * menu which leads to the timeline, the chat,
 * the contact list, the profile and so on.
 *
 * The sidebar visibility can also be controlled by the
 * {@link #pinnedProperty()}.
 */
public class SidebarController extends AbstractController {

	private Rectangle clipRect;

	//TODO: add action concept from the actionbar

	@FXML
	private VBox sidebar;
	@FXML
	private Button profileButton;
	@FXML
	private Button groupButton;
	@FXML
	private Button inboxButton;
	@FXML
	private Button contactButton;
	@FXML
	private Button chatButton;
	@FXML
	private Button timelineButton;

	private AppController appController;

	private BooleanProperty pinned;
	private ParallelTransition flyInAnimation;
	private ParallelTransition flyOutAnimation;

	public SidebarController(AppController appController) {
		super(App.class.getResource("views/sidebarView.fxml"));
		this.appController = appController;

	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		profileButton.setOnAction((event -> appController.performAppAction(AppController.AppAction.OPEN_PROFILE)));
		groupButton.setOnAction((event -> appController.performAppAction(AppController.AppAction.OPEN_GROUPS)));
		inboxButton.setOnAction((event -> appController.performAppAction(AppController.AppAction.OPEN_INBOX)));
		contactButton.setOnAction((event -> appController.performAppAction(AppController.AppAction.OPEN_CONTACTS)));
		chatButton.setOnAction((event -> appController.performAppAction(AppController.AppAction.OPEN_CHATS)));
		timelineButton.setOnAction((event -> appController.performAppAction(AppController.AppAction.OPEN_TIMELINE)));

		sidebar.setPrefWidth(0);
		pinned = pinnedProperty();
		pinned.addListener((obv, oldPinned, newPinned) -> {
			if (oldPinned == newPinned) return;
			if (newPinned) {
				showSidebar();
			} else {
				hideSidebar();
			}
		});
	}

	public void hideSidebar() {

		if (flyInAnimation != null) {
			flyInAnimation.stop();
		}

		Duration duration = Duration.millis(600);
		TranslateTransition moveOutTransition = new TranslateTransition(duration, sidebar);
		moveOutTransition.fromXProperty();
		moveOutTransition.setToX(-sidebar.getWidth());

		DoublePropertyTransition shrinkTransition = new DoublePropertyTransition(duration, sidebar.prefWidthProperty());
		shrinkTransition.setStartValue(sidebar.prefWidth(-1));
		shrinkTransition.setEndValue(0);

		flyOutAnimation = new ParallelTransition(moveOutTransition, shrinkTransition);
		flyOutAnimation.setInterpolator(Interpolator.EASE_IN);
		flyOutAnimation.setOnFinished((evt) -> {
			sidebar.setPrefWidth(0);
			pinned.set(false);
		});
		flyOutAnimation.play();
	}

	public void showSidebar() {

		if (flyOutAnimation != null) {
			flyOutAnimation.stop();
		}

		TranslateTransition moveInTransition = new TranslateTransition(Duration.millis(600), sidebar);
		moveInTransition.fromXProperty();
		moveInTransition.setToX(0);

		DoublePropertyTransition shrinkTransition = new DoublePropertyTransition(Duration.millis(600), sidebar.prefWidthProperty());
		shrinkTransition.setStartValue(sidebar.prefWidthProperty().get());
		shrinkTransition.setEndValue(sidebar.getBoundsInLocal().getWidth());

		flyInAnimation = new ParallelTransition(moveInTransition, shrinkTransition);
		flyInAnimation.setInterpolator(Interpolator.EASE_OUT);
		flyInAnimation.setOnFinished((evt) -> {
			sidebar.setPrefWidth(sidebar.getBoundsInLocal().getWidth());
			pinned.set(true);
		});
		flyInAnimation.play();

	}

	public void toggleSidebar() {
		setPinned(!getPinned());
	}

	public BooleanProperty pinnedProperty() {
		if (pinned == null) {
			return new SimpleBooleanProperty(this, "pinned", false);
		}
		return pinned;
	}

	boolean getPinned() {
		return pinned != null && pinned.get();
	}

	public void setPinned(boolean pinned) {
		pinnedProperty().set(pinned);
	}

}
