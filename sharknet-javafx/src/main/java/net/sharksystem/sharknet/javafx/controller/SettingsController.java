package net.sharksystem.sharknet.javafx.controller;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sharksystem.sharknet.api.Setting;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;

@Controller( title = "%sidebar.settings")
public class SettingsController extends AbstractController {

	private FrontController frontController;

	@Inject
	private SharkNet sharkNetModel;

	private Setting settings;
	private int MAXMBTEST;
	private int switchOffWifiDirectAfterMin;
	private boolean radar;

	@FXML
	private Button settingsSaveButton;
	@FXML
	private Button syncStartButton;
	@FXML
	private Button routingSaveButton;
	@FXML
	private CheckBox contactsSyncCheckbox;
	@FXML
	private CheckBox messagesSyncCheckbox;
	@FXML
	private CheckBox timelineSyncCheckbox;
	@FXML
	private CheckBox homeworkSyncCheckbox;
	@FXML
	private CheckBox profileSyncCheckbox;
	@FXML
	private TextField mailAddressInput;
	@FXML
	private TextField smtpServerInput;
	@FXML
	private TextField imapServerInput;
	@FXML
	private TextField smtpPasswordInput;
	@FXML
	private TextField imapPasswordInput;
	@FXML
	private TextField maximumRouteSizeInput;
	@FXML
	private TextField wifiDirectOffMinutesInput;
	@FXML
	private ToggleGroup syncMedium;
	@FXML
	private ToggleGroup radarSwitch;
	@FXML
	private RadioButton radarOnRadioButton;
	@FXML
	private RadioButton radarOffRadioButton;
	@FXML
	private RadioButton syncMediumSelectWifi;
	@FXML
	private RadioButton syncMediumSelectBluetooth;
	@FXML
	private RadioButton syncMediumSelectMail;
	@FXML
	private RadioButton syncMediumSelectNFC;
	@FXML
	private RadioButton syncMediumSelectTCP;


	public SettingsController() {
		super(App.class.getResource("views/settingsView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);

	}


	@FXML
	private void onSettingsSaveButtonClick() {
		//mailAddress = mailAddressInput.getText();


		settings.setSmtpServer(smtpServerInput.getText());
		settings.setImapServer(imapServerInput.getText());
		settings.setSmtpPassword(smtpPasswordInput.getText());
		settings.setImapPassword(imapPasswordInput.getText());

		//TODO sharkNetModel.getMyProfile().setMail(mailAddress);

		try{
			// TODO
			settings.setWifiON(Integer.parseInt(wifiDirectOffMinutesInput.getText()));
		}catch (Exception e){
			e.printStackTrace();
		}

		System.out.println("Mail address= "+ mailAddressInput.getText());
		System.out.println("SMTP= "+ smtpServerInput.getText());
		System.out.println("IMAP= " + imapServerInput.getText());
		System.out.println("Wifi Direct off after: " + wifiDirectOffMinutesInput.getText() + " min");


		if(radarOnRadioButton.isSelected()){
			settings.setRadarON(true);
		}else if(radarOffRadioButton.isSelected()){
			settings.setRadarON(false);
		}
	}

	@FXML
	private void onRoutingSaveButtonClick() {
		//TODO
		if (maximumRouteSizeInput.getText() != "") {
			try {
				MAXMBTEST = Integer.parseInt(maximumRouteSizeInput.getText());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("MaxMB= " + MAXMBTEST);


		System.out.println("Routing-Einstellungen speichern");

		if(maximumRouteSizeInput.getText() != "") {
			try {
				settings.setMaxFileSize(Integer.parseInt(maximumRouteSizeInput.getText()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void onStartSyncButtonClick() {
		if(settings.getBluetooth()){
			System.out.println("Medium= Bluetooth");
			syncViaBluetooth();
		}
		if(settings.getMail()){
			System.out.println("Medium= Mail");
			syncViaMail();
		}
		if(syncMediumSelectNFC.isSelected()){
			System.out.println("Medium= NFC");
			syncViaNFC();
		}
		if(settings.getWifi()){
			System.out.println("Medium= Wifi");
			syncViaWifi();
		}
		if(settings.getTcp()){
			System.out.println("Medium= TCP");
			syncViaTCP();
		}



		// TODO aus Model holen:
		if(profileSyncCheckbox.isSelected()){
			System.out.println("Profil synchronisieren");
		}
		if(contactsSyncCheckbox.isSelected()){
			System.out.println("Kontakte synchronisieren");
		}
		if(timelineSyncCheckbox.isSelected()){
			System.out.println("Timeline synchronisieren");
		}
		if(messagesSyncCheckbox.isSelected()){
			System.out.println("Chats synchronisieren");
		}
		if(homeworkSyncCheckbox.isSelected()){
			System.out.println("Hausaufgaben synchronisieren");
		}


		//TODO Sync

	}


	private void syncViaWifi(){}
	private void syncViaNFC(){}
	private void syncViaMail(){}
	private void syncViaBluetooth(){}
	private void syncViaTCP(){}


	@Override
	protected void onFxmlLoaded() {
		settings = sharkNetModel.getMyProfile().getSettings();


		//TODO switchOffWifiDirectAfterMin = settings.getWiFiOffMin();

		if(settings.getRadarON()){
			radarOnRadioButton.setSelected(true);
		}else radarOffRadioButton.setSelected(true);

		// Sync
		if(settings.getMail()){
			syncMediumSelectMail.setSelected(true);
		}
		else if(settings.getBluetooth()){
			syncMediumSelectBluetooth.setSelected(true);
		}
		else if(settings.getWifi()){
			syncMediumSelectWifi.setSelected(true);
		}
		else if(settings.getNfc()){
			syncMediumSelectNFC.setSelected(true);
		}
		else if(settings.getTcp()){
			syncMediumSelectTCP.setSelected(true);
		}


		// Textfelder füllen
		maximumRouteSizeInput.setText("" + settings.getMaxFileSize());
		mailAddressInput.setText("" + settings.getMail());
		imapServerInput.setText("" + settings.getImapServer());
		smtpServerInput.setText("" + settings.getSmtpServer());
		smtpPasswordInput.setText("" + settings.getSmtpPassword());
		imapPasswordInput.setText("" + settings.getImapPassword());
		maximumRouteSizeInput.setText("" );
		wifiDirectOffMinutesInput.setText("" + switchOffWifiDirectAfterMin);

		//TODO
		profileSyncCheckbox.setSelected(true);
		contactsSyncCheckbox.setSelected(true);
		timelineSyncCheckbox.setSelected(true);
		messagesSyncCheckbox.setSelected(true);
		homeworkSyncCheckbox.setSelected(true);

	}
}
