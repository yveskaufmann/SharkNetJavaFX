package net.sharksystem.sharknet.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.context.ApplicationContext;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.controller.login.LoginController;
import net.sharksystem.sharknet.javafx.controller.login.LoginListener;
import net.sharksystem.sharknet.javafx.i18n.I18N;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import org.controlsfx.dialog.ExceptionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class App extends Application implements LoginListener {

	/**
	 * Class Logger instance
	 */
	private static final Logger Log = LoggerFactory.getLogger(App.class);

	/**
	 * Front controller of the application
	 */
	private FrontController frontController;
	private Stage stage;

	@Inject
	private SharkNet sharkNet;

	@Override
	public void init() throws Exception {
		ApplicationContext.get().init(this);
		Runtime.getRuntime().addShutdownHook(new Thread(ApplicationContext.get()::destroy));
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			Log.error("Detect uncaught exception in [" + t.getName() + " Thread]", e);
			ExceptionDialog exceptionDialog = new ExceptionDialog(e);
			exceptionDialog.setHeaderText(I18N.getString("app.uncaught.exception.header"));
			exceptionDialog.showAndWait();
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Image image = new Image(App.class.getResource("images/shark-icon256x256.png").toExternalForm(), 256, 256, true, true);
		primaryStage.getIcons().addAll(image);
		stage = primaryStage;
		startLoginController();
	}


	@Override
	public void stop() throws Exception {
		ApplicationContext.get().destroy();
	}

	public void logout() {
		Profile profile =  sharkNet.getMyProfile();
		if (profile != null) {
			Log.info("Logout the profile " + profile.getContact().getName());
			// TODO: disable of profile before we try to log in to a new user
		}

		stage.hide();
		Controllers.getInstance().unregister();
		startLoginController();
	}

	public void startLoginController() {
		if (! getParameters().getUnnamed().contains("--suppressLogin")) {
			LoginController login = new LoginController();
			login.setLoginListener(this);
		} else {
			onLoginSuccessful();
		}
	}

	@Override
	public void onLoginSuccessful() {
		try {
			stage.setScene(null);
			frontController = new FrontController(stage);

			String indexController = getParameters().getNamed().get("indexController");
			if (indexController != null) {
				try {
					Class.forName(indexController);
					frontController.setDefaultController((Class<? extends AbstractController>) Class.forName(indexController));
				} catch (Exception ex) {
					Log.error("Invalid value for 'indexController' passed, must be a full class name");
				}
			}
			frontController.show();

		} catch (Exception ex) {
			Log.error("Exception in a controller detected." ,ex);
			Platform.exit();
		}
	}

	/**
	 * Returns the application wide stylesheet location
	 *
	 * @return the location of the application stylesheet
     */
	public static String getAppStyleSheet() {
		return App.class.getResource("css/style.css").toExternalForm();
	}

	/**
	 * Launch the this fx application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Log.info("Launching SharkNet");
		/**
		 * Improves the poor rendering results of javafx.
		 *
		 * @see <a href="http://comments.gmane.org/gmane.comp.java.openjdk.openjfx.devel/5072">Open JavaFX development Mailing list</a>
		 */
		System.setProperty("prism.lcdtext", "false");
		System.setProperty("prism.text", "t2k");

		// A pre-loader could be used for the startLoginController
		// System.setProperty("javafx.preloader", AppPreloader.class.getName());
		launch(App.class, args);
	}
}
