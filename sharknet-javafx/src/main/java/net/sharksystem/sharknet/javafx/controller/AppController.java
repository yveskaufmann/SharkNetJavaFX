package net.sharksystem.sharknet.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.ActionEntry;
import net.sharksystem.sharknet.javafx.actions.annotations.Action;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.controller.inbox.InboxController;
import net.sharksystem.sharknet.javafx.controlls.toolbar.Actionbar;
import net.sharksystem.sharknet.javafx.i18n.I18N;
import net.sharksystem.sharknet.javafx.utils.AbstractController;
import net.sharksystem.sharknet.javafx.utils.AbstractWindowController;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * The Root Controller of the application
 * which is responsible to assemble and manage
 * all child controllers.
 */
public class AppController extends AbstractWindowController {

	private static final Logger Log  = LoggerFactory.getLogger(AppController.class);

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
	 * A Toolbar which contains AbstactContext sensitive actions
	 * these action can be provided by implementing a action Provider
	 * Interface.
	 */
	@FXML
	private Actionbar toolbar;

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
		getScene().getStylesheets().add(App.class.getResource("style.css").toExternalForm());
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

		// Example Set of Actions
		toolbar.setNavigationAction(new ActionEntry(
			FontAwesomeIcon.NAVICON, () -> {
				sidebarController.toggleSidebar();
			}
		));


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
		String title = getTitle();
		Class<?> cls = controller.getClass();

		if (cls.isAnnotationPresent(Controller.class)) {
			Controller controllerAnnotation = cls.getAnnotation(Controller.class);
			title = controllerAnnotation.title();
			if (! "".equals(title)) {
				title = I18N.getString(title);
			}
		}

		toolbar.clearActionEntries();
		List<ActionEntry> actionEntryList = new ArrayList<>();
		Stream.of(cls.getMethods())
			.filter((method) -> method.isAnnotationPresent(Action.class))
			.sorted((o1, o2) -> {
				int p1 = o1.getAnnotation(Action.class).priority();
				int p2 = o2.getAnnotation(Action.class).priority();
				return p1 == p2 ? 0 : p1 < p2 ? -1 : 1;
			})
			.forEach((method -> {
				Action action = method.getAnnotation(Action.class);
				ActionEntry entry = new ActionEntry();
				entry.setTooltip(action.tooltip());
				entry.setTitle(action.text());
				entry.setIcon(action::icon);
				entry.setCallback(() -> {
					try {
						method.invoke(controller);
					} catch (Exception e) {
						Log.error("Failed to invoke the controller action {}{}.\n"
								+ "Make sure the method is public or protected.",
							controller.getClass().getName(),
							method.getName());
					}
				});
				actionEntryList.add(entry);
			}));

		toolbar.setTitle(title);
		toolbar.clearActionEntries();
		toolbar.actionEntriesProperty().addAll(actionEntryList);
		actionEntryList.clear();

		mainPane.getChildren().clear();
		mainPane.getChildren().add(controller.getRoot());
	}
}
