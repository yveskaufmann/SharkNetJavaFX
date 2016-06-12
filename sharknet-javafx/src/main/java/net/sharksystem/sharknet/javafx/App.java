package net.sharksystem.sharknet.javafx;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.sharksystem.sharknet.javafx.context.ApplicationContext;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application {

	/**
	 * Class Logger instance
	 */
	private static final Logger Log = LoggerFactory.getLogger(App.class);

	/**
	 * Front controller of the application
	 */
	private FrontController frontController;

	@Override
	public void init() throws Exception {
		ApplicationContext.get().init(this);
		Runtime.getRuntime().addShutdownHook(new Thread(ApplicationContext.get()::destroy));
		Thread.setDefaultUncaughtExceptionHandler((t, e) ->
			Log.error("Detect uncaught exception in [" + t.getName() + " Thread]", e));

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Image image = new Image(App.class.getResource("images/shark-icon256x256.png").toExternalForm(), 256, 256, true, true);
		primaryStage.getIcons().addAll(image);
		frontController = new FrontController(primaryStage);
		frontController.show();
	}

	@Override
	public void stop() throws Exception {
		ApplicationContext.get().destroy();
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

		// A pre-loader could be used for the login
		// System.setProperty("javafx.preloader", AppPreloader.class.getName());
		launch(App.class, args);
	}
}
