package net.sharksystem.sharknet.javafx.controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;


@Controller( title = "%sidebar.settings")
public class SettingsController extends AbstractController {

	private FrontController frontController;


	public SettingsController() {
		super(App.class.getResource("views/settingsView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);;
	}

	private int maxRoutedMB = 10;
	private int switchOffWifiDirectAfterMin = 30;
	private String mailAddress = "";
	private String mailPassword = "";
	private String smtpServer = "";
	private String imapServer = "";

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
	private TextField settingsMailAddress;
	@FXML
	private TextField settingsSMTPServer;
	@FXML
	private TextField settingsIMAPServer;

	/*@FXML
	private RadioButton syncMediumSelectWifi;
	@FXML
	private RadioButton syncMediumSelectBluetooth;
	@FXML
	private RadioButton syncMediumSelectMail;
	@FXML
	private RadioButton syncMediumSelectUSB;
	*/

	@FXML
	private ToggleGroup syncMedium;
	@FXML
	private ToggleGroup radarSwitch;


	@FXML
	private void onSettingsSaveButtonClick() {

		mailAddress = settingsMailAddress.getText();
		smtpServer = settingsSMTPServer.getText();
		imapServer = settingsIMAPServer.getText();
		//mailPassword = settings

		System.out.println("Mail address= "+ mailAddress);
		System.out.println("SMTP= "+ smtpServer);
		System.out.println("IMAP= " + imapServer);




		radarSwitch.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
				RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
				System.out.println("Selected Radio Button - " + chk.getText());
			}
		});
	}


	@FXML
	private void onStartSyncButtonClick() {
		syncMedium.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
				RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
				System.out.println("Selected Radio Button - " + chk.getText());
			}
		});
	}

	@FXML
	private void settingsRadarOnRadioButton() {

	}

	@FXML
	private void settingsRadarOffRadioButton() {

	}

	@Override
	protected void onFxmlLoaded() {

	}
}
