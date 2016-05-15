package net.sharksystem.sharknet.javafx;


import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.sharksystem.sharknet.javafx.controller.AppController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.logging.LogManager;

public class App extends Application {

	private static final Logger Log = LoggerFactory.getLogger(App.class);
	private AppController appController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		setUserAgentStylesheet(getClass().getResource("style.css").toExternalForm());
		loadFonts();
		appController = new AppController(primaryStage);
		appController.show();
	}

	private void loadFonts() throws IOException {
		Font.loadFont(App.class.getResource("fonts/Roboto-Medium.ttf").toExternalForm(), 10);
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
