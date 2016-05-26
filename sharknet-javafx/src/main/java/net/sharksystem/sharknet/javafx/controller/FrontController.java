package net.sharksystem.sharknet.javafx.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.ActionEntry;
import net.sharksystem.sharknet.javafx.actions.annotations.Action;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.controller.inbox.InboxController;
import net.sharksystem.sharknet.javafx.controlls.ActionBar;
import net.sharksystem.sharknet.javafx.controlls.Workbench;
import net.sharksystem.sharknet.javafx.i18n.I18N;
import net.sharksystem.sharknet.javafx.utils.AbstractController;
import net.sharksystem.sharknet.javafx.utils.AbstractWindowController;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * The Root Controller of the application
 * which is responsible to assemble and manage
 * all child controllers.
 */
public class FrontController extends AbstractWindowController {

	private static final Logger Log  = LoggerFactory.getLogger(FrontController.class);

	/******************************************************************************
	 *                                                                             
	 * Fields
	 *                                                                              
	 ******************************************************************************/
	
	/**
	 * Currently active controller
	 */
	private ObjectProperty<AbstractController> activeController;

	/**
	 * Map of controller which are controlled by the app controller.
	 */
	private ObservableMap<Class<? extends AbstractController>, AbstractController> registeredControllers;

	/**
	 * Map with meta data of a controller.
	 */
	private Map<Class<? extends AbstractController>, ControllerMeta> controllerMetaData;

	/**
	 * Sidebar Controller
	 */
	private SidebarController sidebarController;

	/**
	 * A Toolbar which contains AbstractContext sensitive actions
	 * these action can be provided by implementing a action Provider
	 * Interface.
	 */
	@FXML
	private ActionBar toolbar;

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

	@FXML
	private Workbench workbench;
	
	/******************************************************************************
	 *                                                                             
	 * Constructors                                                                   
	 *                                                                              
	 ******************************************************************************/
	
	public FrontController(Stage stage) {
		super(App.class.getResource("views/appView.fxml"), stage);
		registeredControllers = FXCollections.observableHashMap();
		controllerMetaData = new HashMap<>();
		setTitle(I18N.getString("app.title"));
		addMainControllers();
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	/**
	 * Defines which controller is currently active inside the
	 * workbench view.
	 *
	 * @defaultValue null
     */
	public ReadOnlyObjectProperty<AbstractController> activeControllerProperty() {
		if (activeController == null) {
			activeController = new SimpleObjectProperty<>(this, "activeController");
		}
		return activeController;
	};

	/******************************************************************************
	 *                                                                             
	 * Methods
	 *                                                                              
	 ******************************************************************************/
	
	private void addMainControllers() {
		sidebarController = new SidebarController(this);
		registerController(TimelineController.class);
		registerController(InboxController.class);
		registerController(ProfileController.class);
		registerController(ChatController.class);
		registerController(ContactController.class);
		registerController(GroupController.class);
		registerController(SettingsController.class);
	}


	public void registerController(Class<? extends AbstractController> controllerType) {
		if (! registeredControllers.containsKey(controllerType)) {
			Constructor constructor = null;
			try {
				constructor = controllerType.getConstructor(getClass());
				registeredControllers.put(controllerType, (AbstractController) constructor.newInstance(FrontController.this));
				controllerMetaData.put(controllerType, new ControllerMeta(controllerType));
			} catch (Exception e) {
				Log.error("Failed to register the controller: {}.\nA constructor must accept a FrontController instance.", controllerType.getName());
			}
		}
	};

	public void unregisterController(Class<? extends AbstractController> controllerType) {
		if (registeredControllers.containsKey(controllerType)) {
			AbstractController controller = registeredControllers.remove(controllerType);
			controllerMetaData.remove(controllerType);
			Log.debug("Shutdown controller {}", controllerType.getName());
			controller.onShutdown();
		}
	};

	public void goToView(Class<? extends AbstractController> controllerType) {
		AbstractController controller =	registeredControllers.get(controllerType);
		if (controller != null) {
			goToView(controller);
		}
	}

	public void goToView(AbstractController controller) {
		// TODO: register unknown controller
		Class<?> controllerType = controller.getClass();
		ControllerMeta meta = controllerMetaData.getOrDefault(controllerType, new ControllerMeta(controller));

		if (! "".equals(meta.title) && "".startsWith("%")) {
			meta.title = I18N.getString(meta.title);
		} else {
			meta.title = getTitle();
		}

		if (activeControllerProperty().get() != null) {
			activeController.get().onPause();
		}

		toolbar.setTitle(meta.title);
		toolbar.actions().setAll(meta.actionEntries);
		mainPane.getChildren().setAll(controller.getRoot());
		activeController.set(controller);
		controller.onResume();
	}



	/******************************************************************************
	 *
	 * Event-Handlers
	 *
	 ******************************************************************************/

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
		toolbar.setNavigationNode(ActionBar.createActionButton(new ActionEntry(
			FontAwesomeIcon.NAVICON, () -> {
				if (workbench.isSidebarPinned()) {
					workbench.hideSidebar();
				} else {
					workbench.showSidebar();
				}
			}
		)));

		goToView(InboxController.class);
	}


	/******************************************************************************
	 *
	 * Meta-Data Reader
	 *
	 ******************************************************************************/

	/**
	 * Reads and stores MetaData about a controller.
	 */
	private class ControllerMeta {
		List<ActionEntry> actionEntries = new ArrayList<>();
		String title = "";
		Class<? extends AbstractController> cls;

		ControllerMeta(AbstractController controller) {
			this(controller.getClass());
		}

		ControllerMeta(Class<? extends AbstractController> controllerClass) {
			cls = controllerClass ;
			readMetaData();
		}

		void readMetaData() {
			readControllerAnnotation();
			readActionMethods();
		}

		void readControllerAnnotation() {
			if (cls.isAnnotationPresent(Controller.class)) {
				Controller controllerAnnotation = cls.getAnnotation(Controller.class);
				title = controllerAnnotation.title();
			}
		}

		void readActionMethods() {
			actionEntries.clear();
			Stream.of(cls.getMethods())
				.filter((method) -> method.isAnnotationPresent(Action.class))
				.sorted((o1, o2) -> {
					int p1 = o1.getAnnotation(Action.class).priority();
					int p2 = o2.getAnnotation(Action.class).priority();
					return p1 == p2 ? 0 : p1 < p2 ? -1 : 1;
				})
				.forEach(this::addActionEntry);

		}

		void addActionEntry(Method method) {
			Action action = method.getAnnotation(Action.class);
			Class<?> declaringCls = method.getDeclaringClass();

			ActionEntry entry = new ActionEntry();
			entry.setTooltip(action.tooltip());
			entry.setTitle(action.text());
			entry.setIcon(action::icon);
			entry.setCallback(() -> {
				try {
					if (registeredControllers.containsKey(declaringCls)) {
						AbstractController controllerInstance = registeredControllers.get(declaringCls);
						method.invoke(controllerInstance);
					}
				} catch (Exception e) {
					Log.error("Failed to invoke the controller action {}{}.\n"
							+ "Make sure the method is public or protected.",
						declaringCls.getName(),
						method.getName());
				}
			});
			actionEntries.add(entry);
		}
	}
}
