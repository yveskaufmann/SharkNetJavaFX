package net.sharksystem.sharknet.javafx.controller;

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
	private SharkNet sharkNetModel;

	//private List<String> selectedSyncItems;

	/*
	private int maxRoutedMB;
	private int switchOffWifiDirectAfterMin;
	private String mailAddress = "";
	private String mailPassword = "";
	private String smtpServer = "";
	private String imapServer = "";
	private boolean radar;
	*/

	//DUMMYS
	private int maxRoutedMB = 10;
	private int switchOffWifiDirectAfterMin = 30;
	private String mailAddress = "max@mustermann.de";
	private String mailPassword = "samplePW";
	private String smtpServer = "smtp.mustermann.de";
	private String imapServer = "imap.mustermann.de";
	private boolean radar = true;
	//


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
	private RadioButton syncMediumSelectUSB;


	public SettingsController() {
		super(App.class.getResource("views/settingsView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);

		//Setting settings = sharkNetModel.getMyProfile().getSettings();
		//ImplSetting implSetting = sharkNetModel.getMyProfile().getSettings()

	}


	@FXML
	private void onSettingsSaveButtonClick() {

		mailAddress = settingsMailAddress.getText();
		smtpServer = settingsSMTPServer.getText();
		imapServer = settingsIMAPServer.getText();
		try {
			maxRoutedMB = Integer.parseInt(maximumRouteSize.getText());
		}catch (Exception e){
			e.printStackTrace();}
		try{
			switchOffWifiDirectAfterMin = Integer.parseInt(settingsWiFiDirectOffMinutes.getText());
		}catch (Exception e){
			e.printStackTrace();
		}
		//mailPassword =

		System.out.println("Mail address= "+ mailAddress);
		System.out.println("SMTP= "+ smtpServer);
		System.out.println("IMAP= " + imapServer);
		System.out.println("MaxMB= " + maxRoutedMB);
		System.out.println("Wifi Direct off after: " + switchOffWifiDirectAfterMin + " min");

		if(radarOnRadioButton.isSelected()){
			System.out.println("Radar ON");
		}
		if(radarOffRadioButton.isSelected()){
			System.out.println("Radar Off");
		}

		//TODO Daten ins Model übernehmen
	}


	@FXML
	private void onStartSyncButtonClick() {
		if(syncMediumSelectBluetooth.isSelected()){
			System.out.println("Medium= Bluetooth");
			syncViaBluetooth();
		}
		if(syncMediumSelectMail.isSelected()){
			System.out.println("Medium= Mail");
			syncViaMail();
		}
		if(syncMediumSelectUSB.isSelected()){
			System.out.println("Medium= USB");
			syncViaUSB();
		}
		if(syncMediumSelectWifi.isSelected()){
			System.out.println("Medium= Wifi");
			syncViaWifi();
		}

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


	private void syncViaWifi(){

	}
	private void syncViaUSB(){

	}
	private void syncViaMail(){

	}
	private void syncViaBluetooth(){

	}


	@Override
	protected void onFxmlLoaded() {
		settingsMailAddress.setText(mailAddress);
		settingsIMAPServer.setText(imapServer);
		settingsSMTPServer.setText(smtpServer);
		maximumRouteSize.setText(""+maxRoutedMB);
		settingsWiFiDirectOffMinutes.setText(""+switchOffWifiDirectAfterMin);

		//Dummy
		radarOffRadioButton.setSelected(true);
		syncMediumSelectWifi.setSelected(true);

		profileSyncCheckbox.setSelected(true);
		contactsSyncCheckbox.setSelected(true);
		timelineSyncCheckbox.setSelected(true);
		messagesSyncCheckbox.setSelected(true);
		homeworkSyncCheckbox.setSelected(true);


		//radar = implSetting.getRadar();

		System.out.println("Settings aus Profil: ");

	}
}
