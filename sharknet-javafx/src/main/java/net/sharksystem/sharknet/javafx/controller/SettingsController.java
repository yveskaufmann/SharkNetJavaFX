package net.sharksystem.sharknet.javafx.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.AbstractController;


@Controller( title = "%sidebar.settings")
public class SettingsController extends AbstractController {

	private AppController appController;

	@FXML
	private Button settingsSaveButton;

	@FXML
	private Button syncStartButton;

	@FXML
	private CheckBox contactsSyncCheckbox;

	@FXML
	private CheckBox messagesSyncCheckbox;

	@FXML
	private CheckBox timelineSyncCheckbox;

	@FXML
	private RadioButton syncMediumSelectWifi;

	@FXML
	private RadioButton syncMediumSelectBluetooth;

	@FXML
	private RadioButton syncMediumSelectMail;

	@FXML
	private RadioButton syncMediumSelectUSB;


	public SettingsController(AppController appController) {
		super(App.class.getResource("views/settingsView.fxml"));
		this.appController = appController;
	}

	@Override
	protected void onFxmlLoaded() {
		//TODO: Implement settings controller
	}
}
