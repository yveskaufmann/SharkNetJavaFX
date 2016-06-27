package net.sharksystem.sharknet.javafx.controller;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.api.Setting;

import java.util.List;

@Controller( title = "%sidebar.settings")
public class SettingsController extends AbstractController {

	private FrontController frontController;

	@Inject
	private SharkNet sharkNetModel;

	private Setting settings;
	private int maxRoutedMB;
	private int switchOffWifiDirectAfterMin;
	private String mailAddress;
	private String mailPassword;
	private String smtpServer;
	private String imapServer;
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
	private CheckBox homeworkSyncCheckbox;
	@FXML
	private CheckBox profileSyncCheckbox;
	@FXML
	private TextField settingsMailAddress;
	@FXML
	private TextField settingsSMTPServer;
	@FXML
	private TextField settingsIMAPServer;
	@FXML
	private TextField mailPasswordTextField;
	@FXML
	private TextField maximumRouteSize;
	@FXML
	private TextField settingsWiFiDirectOffMinutes;
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
		mailAddress = settingsMailAddress.getText();
		settings.setSmtpServer(settingsSMTPServer.getText());
		settings.setImapServer(settingsIMAPServer.getText());

		//TODO sharkNetModel.getMyProfile().setMail(mailAddress);
		//TODO settings.setMailPW = mailPasswordTextField.getText();

		try {
			settings.setMaxFileSize(Integer.parseInt(maximumRouteSize.getText()));
		}catch (Exception e){
			e.printStackTrace();}
		try{
			// TODO
			switchOffWifiDirectAfterMin = Integer.parseInt(settingsWiFiDirectOffMinutes.getText());
		}catch (Exception e){
			e.printStackTrace();
		}

		System.out.println("Mail address= "+ mailAddress);
		System.out.println("SMTP= "+ settingsSMTPServer.getText());
		System.out.println("IMAP= " + settingsIMAPServer.getText());
		System.out.println("MaxMB= " + Integer.parseInt(maximumRouteSize.getText()));
		System.out.println("Wifi Direct off after: " + switchOffWifiDirectAfterMin + " min");


		if(radarOnRadioButton.isSelected()){
			settings.setRadarON(true);
		}else if(radarOffRadioButton.isSelected()){
			settings.setRadarON(false);
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

		// Settings aus Model auslesen
		maxRoutedMB = settings.getMaxFileSize();
		imapServer = settings.getImapServer();
		smtpServer = settings.getSmtpServer();
		//TODO mailPassword = settings.getMailPassword();
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
		mailPasswordTextField.setText(mailPassword);
		maximumRouteSize.setText("" + maxRoutedMB);
		settingsMailAddress.setText(mailAddress);
		settingsIMAPServer.setText(imapServer);
		settingsSMTPServer.setText(smtpServer);
		maximumRouteSize.setText(""+maxRoutedMB);
		settingsWiFiDirectOffMinutes.setText(""+switchOffWifiDirectAfterMin);

		profileSyncCheckbox.setSelected(true);
		contactsSyncCheckbox.setSelected(true);
		timelineSyncCheckbox.setSelected(true);
		messagesSyncCheckbox.setSelected(true);
		homeworkSyncCheckbox.setSelected(true);

	}
}
