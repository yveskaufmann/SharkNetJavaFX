package net.sharksystem.sharknet.javafx.controller;


import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

@Controller(title = "%sidebar.radar")
public class RadarController extends AbstractController {

	public RadarController() {
		super(App.class.getResource("views/radarView.fxml"));
	}

	@Override
	protected void onFxmlLoaded() {
		//TODO: Implement radar controller
	}
}
