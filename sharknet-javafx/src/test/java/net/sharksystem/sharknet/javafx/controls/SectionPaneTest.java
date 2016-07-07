package net.sharksystem.sharknet.javafx.controls;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @Author Yves Kaufmann
 * @since 03.07.2016
 */
public class SectionPaneTest extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane root = new StackPane();
		root.setPadding(new Insets(16));

		Label label = new Label("Content");
		BorderPane content = new BorderPane();
		content.setCenter(label);

		SectionPane pane = new SectionPane();
		pane.setText("Example Section");
		pane.setCollapsible(true);
		pane.setTitleControl(new Button("Create Account"));
		pane.setContent(content);

		root.getChildren().add(pane);

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
