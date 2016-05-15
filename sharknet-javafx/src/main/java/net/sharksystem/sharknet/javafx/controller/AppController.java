package net.sharksystem.sharknet.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.i18n.I18N;
import net.sharksystem.sharknet.javafx.utils.AbstractController;
import net.sharksystem.sharknet.javafx.utils.AbstractWindowController;

import java.util.Map;

/**
 * The Root Controller of the application
 * which is responsible to assemble and manage
 * all child controllers.
 */
public class AppController extends AbstractWindowController {



	enum AppAction {
		OPEN_PROFILE,
		OPEN_CONTACTS,
		OPEN_CHATS,
		OPEN_INBOX,
		OPEN_TIMELINE,
		OPEN_SETTINGS,
		OPEN_GROUPS
	}
	private SidebarController sidebarController;

	private ProfileController profileController;
	private ChatController chatController;
	private ContactController contactController;
	private InboxController inboxController;
	private TimelineController timelineController;
	private GroupController groupController;
	private SettingsController settingsController;

	/**
	 * Container for the sidebar menu view
	 */
	@FXML
	private StackPane sidebarPane;

	/**
	 * Container for the main views like contacts, chats and so on.
	 */
	@FXML
	private StackPane mainPane;

	public AppController(Stage stage) {
		super(App.class.getResource("views/appView.fxml"), stage);
		setTitle(I18N.getString("app.title"));
		addMainControllers();


	}

	private void addMainControllers() {
		sidebarController = new SidebarController(this);
		profileController = new ProfileController(this);
		chatController = new ChatController(this);
		contactController = new ContactController(this);
		inboxController = new InboxController(this);
		timelineController = new TimelineController(this);
		groupController = new GroupController(this);
		settingsController = new SettingsController(this);
	}

	/**
	 * Will be called when the window is requested to be closed.
	 *
	 * @param windowEvent
	 */
	@Override
	public void onCloseRequest(WindowEvent windowEvent) {

	}

	/**
	 * Called when the scene was created
	 */
	@Override
	protected void onSceneCreated() {

	}

	/**
	 * Called when the stage was created
	 */
	@Override
	protected void onStageCreated() {
		getStage().sizeToScene();
		getStage().centerOnScreen();
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		sidebarPane.getChildren().add(sidebarController.getRoot());
		performAppAction(AppAction.OPEN_INBOX);
	}

	public void performAppAction(AppAction action) {

		switch (action) {
			case OPEN_CHATS:
				showControllerView(chatController);
				break;
			case OPEN_GROUPS:
				showControllerView(groupController);
				break;
			case OPEN_CONTACTS:
				showControllerView(contactController);
				break;
			case OPEN_INBOX:
				showControllerView(inboxController);
				break;
			case OPEN_PROFILE:
				showControllerView(profileController);
				break;
			case OPEN_SETTINGS:
				showControllerView(settingsController);
				break;
			case OPEN_TIMELINE:
				showControllerView(timelineController);
				break;
		}
	}

	protected void showControllerView(AbstractController controller) {
		mainPane.getChildren().clear();
		mainPane.getChildren().add(controller.getRoot());
	}
}
