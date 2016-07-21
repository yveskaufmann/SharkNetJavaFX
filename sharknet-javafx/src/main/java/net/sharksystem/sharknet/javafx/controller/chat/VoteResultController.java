package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Voting;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.context.ApplicationContext;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static net.sharksystem.sharknet.javafx.i18n.I18N.getString;

/**
 * Created by Benni on 16.07.2016.
 */
public class VoteResultController extends AbstractController {

	@FXML
	private PieChart pieChartView;

	private Voting vote = null;

	public VoteResultController(Voting v) {
		super(App.class.getResource("views/chat/voteResultView.fxml"));
		vote = v;
		Parent root = super.getRoot();
		ApplicationContext.get().getInjector().injectMembers(this);
		Stage stage = new Stage();
		stage.setScene(new Scene(root, 700, 350));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		InputStream in = App.class.getResourceAsStream("images/shark-icon256x256.png");
		if (in != null) {
			stage.getIcons().add(new Image(in));
		}
		stage.setTitle(getString("chat.voteresult.title"));
		stage.show();
	}

	@Override
	protected void onFxmlLoaded() {

	}

	public void drawChart() {
		if (vote != null) {
			Iterator it = vote.getVotings().entrySet().iterator();
			HashMap<String, Integer> resultMap = new HashMap<>();

			// count the votings in a seperated map
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				String answer = pair.getKey().toString();
				List<Contact> voters = (List<Contact>) pair.getValue();
				int count = 0;
				if (voters != null) {
					for (Contact c : voters) {
						count++;
					}
				}
				resultMap.put(answer, count);
			}
			// add the result map to piechart
			Iterator iterator = resultMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry pair = (Map.Entry)iterator.next();
				String answer = pair.getKey().toString();
				int count = (Integer) pair.getValue();
				pieChartView.dataProperty().get().add(new PieChart.Data(answer, count));
			}

			pieChartView.setTitle(vote.getQuestion());
			pieChartView.setLabelsVisible(true);
			pieChartView.setLegendSide(Side.LEFT);
			// caption for chart pies
			pieChartView.getData().forEach(data ->
					data.nameProperty().bind(
						Bindings.concat(
							data.getName(), " (", data.pieValueProperty(), " Votes)"
						)
					)
			);

			for (Node node : pieChartView.lookupAll(".chart-legend-item")) {
				if (node instanceof Label) {
					((Label) node).setWrapText(true);
					((Label) node).setManaged(true);
					((Label) node).setPrefWidth(150);
				}
			}
		}
	}
}
