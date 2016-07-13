package net.sharksystem.sharknet.javafx.controller.settings;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.context.AbstractContext;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

/**
 * Created by ich on 08.07.16.
 */
public class ChooseRoutingContactsController extends AbstractController {

	private Stage stage;


	public ChooseRoutingContactsController(){
		//TODO
		super(App.class.getResource("views/contactlist/XXXXXXXXX"));
		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle("Kontakte für Routing auswählen");
		stage.setScene(new Scene(root, 800, 600));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		stage.show();

	}

	@Override
	protected void onFxmlLoaded() {
	}

}
