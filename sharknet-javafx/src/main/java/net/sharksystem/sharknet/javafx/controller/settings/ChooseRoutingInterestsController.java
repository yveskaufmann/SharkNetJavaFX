package net.sharksystem.sharknet.javafx.controller.settings;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import java.util.ArrayList;
import java.util.List;

public class ChooseRoutingInterestsController extends AbstractController {

		@FXML
		private ListView allowedInterestsListView;
		@FXML
		private ListView deniedInterestssListView;
		@FXML
		private Button allowButton;
		@FXML
		private Button denyButton;
		@FXML
		private Button okButton;

		@Inject
		private SharkNet sharkNetModel;

		private List<SemanticTag> allInterests;
		private List<SemanticTag> allowedInterests;
		private List<SemanticTag> deniedInterests;
		private Stage stage;
		private Setting settings;

		public ChooseRoutingInterestsController() {
			super(App.class.getResource("views/routingContactsView.fxml"));
			allInterests = new ArrayList<>();
			allowedInterests = new ArrayList<>();
			Parent root = super.getRoot();
			stage = new Stage();
			stage.setTitle("Fürs Routing zugelassene Themen");
			stage.setScene(new Scene(root, 600, 400));
			stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
			stage.show();
		}

		@Override
		protected void onFxmlLoaded() {
			this.settings = sharkNetModel.getMyProfile().getSettings();

			loadInterests();

			allowButton.setOnMouseClicked(event -> {
				onAllowInterest(deniedInterestssListView.getSelectionModel().getSelectedIndex());
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


		private void onAllowInterest(int selectedIndex){
			if (selectedIndex >= 0) {
				allowedInterestsListView.getItems().add(deniedInterests.get(selectedIndex).getName());
				allowedInterests.add(deniedInterests.get(selectedIndex));
				allowedInterests.add(deniedInterests.get(selectedIndex));

				deniedInterestssListView.getItems().remove(selectedIndex);
				deniedInterests.remove(selectedIndex);
			}
		}

		private void onDenyInterest(int selectedIndex) {
			if (selectedIndex >= 0) {
				System.out.println("DENY: " + selectedIndex);
				deniedInterests.add(allowedInterests.get(selectedIndex));
				deniedInterestssListView.getItems().add(allowedInterests.get(selectedIndex).getName());

				allowedInterestsListView.getItems().remove(selectedIndex);
				allowedInterests.remove(selectedIndex);
			}
		}

		private void loadInterests() {
			/*allInterests = sharkNetModel.getMyProfile().getContact().getInterests().getAllTopics();
			allowedInterests = settings.getRoutingInterests();

			// Daten zu diesen Themen weiterleiten
			for (Contact c : allContacts) {

				if (allowedContacts.contains(c)) { allowedContactsListView.getItems().add(c.getNickname());	}
				else{
					deniedContactsListView.getItems().add(c.getNickname());
					deniedContacts.add(c);
				}
			}
*/
			//TODO
			Contact c = sharkNetModel.getMyProfile().getContact();

			if(c.getInterests().getAllTopics().isEmpty()){
				allowedInterestsListView.getItems().add("Keine Interessen vorhanden.");
			}
			for (SemanticTag s : c.getInterests().getAllTopics()) {
				allowedInterestsListView.getItems().add(s.getName());
				allowedInterests.add(s);
			}

		}
	}

