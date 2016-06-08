package net.sharksystem.sharknet.javafx.controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.*;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;


@Controller( title = "%sidebar.settings")
public class SettingsController extends AbstractController {

	private FrontController frontController;

	private SharkNet sharkNetModel;

	private int maxRoutedMB = 10;
	private int switchOffWifiDirectAfterMin = 30;
	private String mailAddress = "";
	private String mailPassword = "";
	private String smtpServer = "";
	private String imapServer = "";
	private boolean radar;

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
	@FXML
	private TextField maximumRouteSize;
	@FXML
	private TextField settingsWiFiDirectOffMinutes;


	public SettingsController() {
		super(App.class.getResource("views/settingsView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);

		//ImplSetting implSetting = sharkNetModel.getMyProfile().getSettings()

	}





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
		//maxRoutedMB = maximumRoutedMB.getText();
		//mailPassword = settings

		System.out.println("Mail address= "+ mailAddress);
		System.out.println("SMTP= "+ smtpServer);
		System.out.println("IMAP= " + imapServer);
		System.out.println("MaxMB= " + maxRoutedMB);


		radarSwitch.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
				RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
				System.out.println("Selected Radio Button - " + chk.getText());
				// implSetting.setRadar();
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
		radar = true;
	}

	@FXML
	private void settingsRadarOffRadioButton() {
		radar = false;
	}

	@Override
	protected void onFxmlLoaded() {
		settingsWiFiDirectOffMinutes.setText(""+switchOffWifiDirectAfterMin);
		maximumRouteSize.setText(""+maxRoutedMB);
		//radar = implSetting.getRadar();

	}
}
