package net.sharksystem.sharknet.javafx.controller.settings;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.TXSemanticTag;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import java.util.ArrayList;
import java.util.List;


/******************************************************************************
 *
 * Dieser Controller kümmert sich um die Auswahl der zu routenden Themen
 * (siehe Settings). Zugehörige View: routingInterestsView.fxml.
 *
 ******************************************************************************/


public class ChooseRoutingInterestsController extends AbstractController {

	@FXML
	private ListView allowedInterestsListView;
	@FXML
	private ListView deniedInterestsListView;
	@FXML
	private Button allowButton;
	@FXML
	private Button denyButton;
	@FXML
	private Button okButton;

	@Inject
	private SharkNet sharkNetModel;

	private List<TXSemanticTag> allInterests;
	private List<SemanticTag> allowedInterests;
	private List<SemanticTag> deniedInterests;
	private Stage stage;
	private Setting settings;
	private Contact c;

	public ChooseRoutingInterestsController() {
		super(App.class.getResource("views/routingInterestsView.fxml"));
		allInterests = new ArrayList<>();
		allowedInterests = new ArrayList<>();
		deniedInterests = new ArrayList<>();
		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle("Fürs Routing zugelassene Themen");
		stage.setScene(new Scene(root, 600, 400));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();
	}

	@Override
	protected void onFxmlLoaded() {
		c = sharkNetModel.getMyProfile().getContact();
		allInterests = c.getInterests().getAllTopics();
		this.settings = sharkNetModel.getMyProfile().getSettings();

		loadInterests();
		allowButton.setOnMouseClicked(event -> {
			onAllowInterest(deniedInterestsListView.getSelectionModel().getSelectedIndex());
			event.consume();
		});
			denyButton.setOnMouseClicked(event -> {
			onDenyInterest(allowedInterestsListView.getSelectionModel().getSelectedIndex());
			event.consume();
		});
			okButton.setOnMouseClicked(event -> {
			event.consume();
			stage.close();
		});
	}

	// Interest zur Liste erlaubter Interessen hinzufügen
	private void onAllowInterest(int selectedIndex){
		if (selectedIndex >= 0) {
			allowedInterestsListView.getItems().add(deniedInterests.get(selectedIndex).getName());
			allowedInterests.add(deniedInterests.get(selectedIndex));
			allowedInterests.add(deniedInterests.get(selectedIndex));
			deniedInterestsListView.getItems().remove(selectedIndex);
			deniedInterests.remove(selectedIndex);
		}
	}
	// Interest zur Liste verweigerter Interessen hinzufügen
	private void onDenyInterest(int selectedIndex) {
		if (selectedIndex >= 0) {
			deniedInterests.add(allowedInterests.get(selectedIndex));
			deniedInterestsListView.getItems().add(allowedInterests.get(selectedIndex).getName());

			allowedInterestsListView.getItems().remove(selectedIndex);
			allowedInterests.remove(selectedIndex);
		}
	}

	// Laden der Listen
	private void loadInterests() {
		for (SemanticTag s : c.getInterests().getAllTopics()) {
			allowedInterestsListView.getItems().add(s.getName());
			allowedInterests.add(s);
		}
	}
}



