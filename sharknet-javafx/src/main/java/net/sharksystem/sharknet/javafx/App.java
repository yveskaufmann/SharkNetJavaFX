package net.sharksystem.sharknet.javafx;


import com.google.inject.*;
import javafx.application.Application;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.SharkNet;
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
		enableLCDTextAntiAliasing();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		frontController = new FrontController(primaryStage);
		frontController.show();
	}

	@Override
	public void stop() throws Exception {
		ApplicationContext.get().destroy();
	}

	/**
	 * Improves the poor rendering results of javafx.
	 *
	 * @see <a href="http://comments.gmane.org/gmane.comp.java.openjdk.openjfx.devel/5072">Open JavaFX development Mailing list</a>
	 */
	private static void enableLCDTextAntiAliasing() {
		System.setProperty("prism.lcdtext", "false");
		System.setProperty("prism.text", "t2k");
	}

	/**
	 * Launch the this fx application.
	 *
	 * @param args
     */
	public static void main(String[] args) {
		launch(args);
	}
}
