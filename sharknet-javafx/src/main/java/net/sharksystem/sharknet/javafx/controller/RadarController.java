package net.sharksystem.sharknet.javafx.controller;


import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.AbstractController;

@Controller(title = "%sidebar.radar")
public class RadarController extends AbstractController {

	private FrontController frontController;

	public RadarController(FrontController frontController) {
		super(App.class.getResource("views/radarView.fxml"));
		this.frontController = frontController;
	}

	@Override
	protected void onFxmlLoaded() {
		//TODO: Implement radar controller
	}
}
