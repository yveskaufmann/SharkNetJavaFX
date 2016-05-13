package net.sharksystem.sharknet.javafx;


import javafx.application.Application;
import javafx.stage.Stage;
import net.sharksystem.sharknet.javafx.controller.AppController;

public class App extends Application {

	private AppController appController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		setUserAgentStylesheet(App.class.getResource("style.css").toExternalForm());
		appController = new AppController(primaryStage);
		appController.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
