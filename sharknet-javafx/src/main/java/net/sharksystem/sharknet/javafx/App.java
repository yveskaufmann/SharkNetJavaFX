package net.sharksystem.sharknet.javafx;


import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import net.sharksystem.sharknet.javafx.context.ApplicationContext;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.modules.SharkNetModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.logging.LogManager;

public class App extends Application {

	private static final Logger Log = LoggerFactory.getLogger(App.class);
	private FrontController frontController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		frontController = new FrontController(primaryStage);
		frontController.show();
	}


	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new SharkNetModule());
		ApplicationContext.getInstance().registerInjector(injector);

		enableLCDTextAntiAliasing();
		enableLogging();
		launch(args);
	}

	private static void enableLCDTextAntiAliasing() {
		// Improves javafx font rendering results
		System.setProperty("prism.lcdtext", "false");
		System.setProperty("prism.text", "t2k");
	}



	private static void enableLogging() {
		try {
			LogManager.getLogManager().readConfiguration(
				App.class.getResource("logging.properties").openStream()
			);
		} catch (IOException ex) {
			// When no logging.properties is provided, no logging is required.
		}
	}
}
