package net.sharksystem.sharknet.javafx;


import javafx.application.Application;
import javafx.stage.Stage;
import net.sharksystem.sharknet.javafx.controller.FrontController;
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
		enableLogging();
		launch(args);
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
