package net.sharksystem.sharknet.javafx.controller;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.I18N.I18N;
import net.sharksystem.sharknet.javafx.utils.AbstractWindowController;

/**
 * The Root Controller of the application
 * which is responsible to assemble and manage
 * all child controllers.
 */
public class AppController extends AbstractWindowController {


	public AppController(Stage stage) {
		super(App.class.getResource("views/appView.fxml"), stage);
		setTitle(I18N.getString("app.title"));
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

	}
}
