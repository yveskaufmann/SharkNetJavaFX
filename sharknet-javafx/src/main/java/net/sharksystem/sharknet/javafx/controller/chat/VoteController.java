package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import org.controlsfx.control.spreadsheet.Grid;

import java.awt.*;
import java.util.Optional;

/**
 * Created by Benni on 21.06.2016.
 */
public class VoteController extends AbstractController {

	@FXML
	private ImageView imageViewAdd;
	@FXML
	private GridPane gridPaneAnswers;

	private int answerCount;

	public VoteController() {
		super(App.class.getResource("views/chat/voteView.fxml"));

		Parent root = super.getRoot();
		Stage stage = new Stage();
		stage.setScene(new Scene(root, 494, 414));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();

		answerCount = 0;
	}

	@Override
	protected void onFxmlLoaded() {
		imageViewAdd.setOnMouseClicked(event -> {
			onAddClick();
			event.consume();
		});


	}

	private void onAddClick() {

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add Answer");
		dialog.setHeaderText("Enter your answer");
		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {
			RowConstraints rCon = new RowConstraints();
			rCon.setPrefHeight(30.0);
			HBox boxLeft = new HBox();
			Label label = new Label();
			label.setText(result.get());
			boxLeft.getChildren().add(label);


			HBox boxRight = new HBox();
			ImageView remove = new ImageView();
			remove.setImage(new Image(App.class.getResourceAsStream("images/minus.png")));
			remove.setFitWidth(32.0);
			remove.setFitHeight(32.0);
			remove.setOnMouseClicked(event -> {
				onRemoveClick(gridPaneAnswers.getRowIndex(boxLeft));
				event.consume();
			});
			ImageView edit = new ImageView();
			edit.setImage(new Image(App.class.getResourceAsStream("images/edit.png")));
			edit.setFitWidth(32.0);
			edit.setFitHeight(32.0);
			edit.setOnMouseClicked(event -> {
				onEditClick(gridPaneAnswers.getRowIndex(boxLeft));
				event.consume();
			});
			
			boxRight.getChildren().add(remove);
			boxRight.getChildren().add(edit);

			gridPaneAnswers.getRowConstraints().add(rCon);
			gridPaneAnswers.add(boxLeft, 0, answerCount);
			gridPaneAnswers.add(boxRight, 1, answerCount);
			//gridPaneAnswers.addRow(answerCount,boxLeft);
			answerCount += 1;
		}


	}

	private void onRemoveClick(int row) {
		System.out.println("remove: " + row);

	}

	private void onEditClick(int row) {
		System.out.println("edit: " + row);
	}
}
