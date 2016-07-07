package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.geometry.Insets;
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
import net.sharksystem.sharknet.api.Content;
import net.sharksystem.sharknet.api.ImplContent;
import net.sharksystem.sharknet.api.ImplVoting;
import net.sharksystem.sharknet.api.Voting;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;


import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * Created by Benni on 21.06.2016.
 */
public class VoteController extends AbstractController {

	@FXML
	private ImageView imageViewAdd;
	@FXML
	private ImageView imageViewSave;
	@FXML
	private GridPane gridPaneAnswers;
	@FXML
	private RadioButton radioButtonSingle;
	@FXML
	private RadioButton radioButtonMulti;
	@FXML
	private TextField textFieldQuestion;

	private List<ChatListener> listeners;
	private List<String> answers;
	private Stage stage;

	public VoteController() {
		super(App.class.getResource("views/chat/voteView.fxml"));

		Parent root = super.getRoot();
		stage = new Stage();
		stage.setScene(new Scene(root, 494, 350));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		InputStream in = App.class.getResourceAsStream("images/shark-icon256x256.png");
		if (in != null) {
			stage.getIcons().add(new Image(in));
		}
		stage.setTitle("Create Vote");
		stage.show();
		listeners = new ArrayList<>();
		answers = new ArrayList<>();
	}

	@Override
	protected void onFxmlLoaded() {
		imageViewAdd.setOnMouseClicked(event -> {
			onAddClick();
			event.consume();
		});

		imageViewSave.setOnMouseClicked(event -> {
			onSaveClick();
			event.consume();
		});
	}

	/**
	 * adding vote answer
	 */
	private void onAddClick() {
		// create and open dialog for the answers
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add Answer");
		dialog.setHeaderText("Enter your answer");
		Optional<String> result = dialog.showAndWait();
		// if input is valid
		if (result.isPresent()) {
			answers.add(result.get());
			refreshAnswerGrid();
		}
	}

	// draw the whole grid with answers, needed because we cant directly remove a specific row
	private void refreshAnswerGrid() {
		// first remove everything
		gridPaneAnswers.getChildren().removeAll(gridPaneAnswers.getChildren());

		for (int i = 0; i < answers.size(); i++) {
			// add radiobutton and answer to the first column
			RowConstraints rCon = new RowConstraints();
			rCon.setPrefHeight(30.0);
			HBox boxLeft = new HBox();
			Label label = new Label();
			label.setText(answers.get(i));
			RadioButton radioButton = new RadioButton();
			boxLeft.setMargin(label, new Insets(0, 0, 0, 25));
			boxLeft.getChildren().addAll(radioButton, label);

			// add button for edit and remove to the second column
			HBox boxRight = new HBox();
			// for removing later
			boxRight.setUserData(answers.get(i));
			ImageView remove = new ImageView();
			remove.setImage(new Image(App.class.getResourceAsStream("images/minus.png")));
			remove.setFitWidth(32.0);
			remove.setFitHeight(32.0);
			// used so we can use the whole image area for click event
			remove.setPickOnBounds(true);
			remove.setOnMouseClicked(event -> {
				onRemoveClick(gridPaneAnswers.getRowIndex(boxLeft));
				event.consume();
			});

			ImageView edit = new ImageView();
			edit.setImage(new Image(App.class.getResourceAsStream("images/edit.png")));
			edit.setFitWidth(32.0);
			edit.setFitHeight(32.0);
			// used so we can use the whole image area for click event
			edit.setPickOnBounds(true);
			edit.setOnMouseClicked(event -> {
				onEditClick(gridPaneAnswers.getRowIndex(boxLeft));
				event.consume();
			});
			boxRight.setMargin(remove, new Insets(0, 15, 0, 0));
			boxRight.getChildren().addAll(remove, edit);

			gridPaneAnswers.getRowConstraints().add(rCon);
			// add first and second column content
			gridPaneAnswers.add(boxLeft, 0, i);
			gridPaneAnswers.add(boxRight, 1, i);

		}

	}

	private void onSaveClick() {
		System.out.println("onSaveClick");

		boolean singleChoice = true;
		if (radioButtonMulti.isSelected()) {
			singleChoice = false;
		}
		// if a question is set and min. one answer
		if (textFieldQuestion.getText().length() > 0 && answers.size() > 0) {
			Content content = new ImplContent("");
			Voting vote = content.addVoting(textFieldQuestion.getText(), singleChoice);
			vote.addAnswers(answers);
			vote.save();
			for (ChatListener listener : listeners) {
				listener.onVoteAdded(content);
			}
			stage.close();
		}

	}

	private void onRemoveClick(int row) {
		System.out.println("remove: " + row);
		ObservableList<Node> childs = gridPaneAnswers.getChildren();
		Iterator<Node> iterator = childs.iterator();
		String answer = "";
		// get our answer text which we want to remove
		while (iterator.hasNext()) {
			Node child = iterator.next();
			if (gridPaneAnswers.getRowIndex(child) == row) {
				if (child instanceof HBox) {
					HBox box = (HBox) child;
					if (box.getUserData() instanceof String) {
						String tmp = (String) box.getUserData();
						if (tmp.length() > 0) {
							answer = tmp;
						}
					}
				}
				iterator.remove();
			}
		}
		removeAnswerFromList(answer);
		refreshAnswerGrid();
	}

	private void onEditClick(int row) {
		// ToDo: edit function
		System.out.println("edit: " + row);

		// create and open dialog for the answers
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Edit Answer");
		dialog.setHeaderText("Enter your answer");
		Optional<String> result = dialog.showAndWait();
		// if input is valid
		if (result.isPresent()) {
			// get childs of the gridpane -> hbox
			ObservableList<Node> childs = gridPaneAnswers.getChildren();
			for (Node child : childs) {
				if (gridPaneAnswers.getRowIndex(child) == row) {
					if (child instanceof HBox) {
						HBox hbox = (HBox) child;
						// get childs of the hbox, so we can find our label with the answer string
						ObservableList<Node> childChilds = hbox.getChildren();
						for (Node childChild : childChilds) {
							if (childChild instanceof Label) {
								String oldAnswer = ((Label) childChild).getText();
								((Label) childChild).setText(result.get());
								editAnswer(oldAnswer, result.get());
								return;
							}
						}
					}

				}
			}
		}


	}

	private void removeAnswerFromList(String answer) {
		Iterator<String> it = answers.iterator();
		while (it.hasNext()) {
			String entry = it.next();
			if (entry.equals(answer)) {
				it.remove();
				break;
			}
		}
	}

	private void editAnswer(String oldAnswer, String newAnswer) {
		for (String entry : answers) {
			if (entry.equals(oldAnswer)) {
				entry = newAnswer;
			}
		}
	}

	public void addListener(ChatListener l) {
		listeners.add(l);
	}
}
