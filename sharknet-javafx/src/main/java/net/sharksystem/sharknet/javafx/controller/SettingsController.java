package net.sharksystem.sharknet.javafx.controller;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Interest;
import net.sharksystem.sharknet.api.Setting;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controller.settings.ChooseRoutingContactsController;
import net.sharksystem.sharknet.javafx.controller.settings.ChooseRoutingInterestsController;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;

import java.util.LinkedList;
import java.util.List;

@Controller( title = "%sidebar.settings")
public class SettingsController extends AbstractController {

	@Inject
	private SharkNet sharkNetModel;

	private FrontController frontController;
	private Setting settings;
	private int MAXMBTEST;
	private int switchOffWifiDirectAfterMin;
	private boolean radar;
	private String tcpAddress = "tcp://TEST....";
	private int tcpPort = 6000;
	private List<Contact> routingContacts = new LinkedList<>();
	private List<Interest> routingInterest = new LinkedList<>();

	@FXML
	private Button settingsSaveButton;
	@FXML
	private Button syncStartButton;
	@FXML
	private Button routingSaveButton;
	@FXML
	private Button chooseRoutingContactsButton;
	@FXML
	private Button chooseInterestsRoutingButton;
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
	private TextField portInput;
	@FXML
	private TextField smtpServerInput;
	@FXML
	private TextField imapServerInput;
	@FXML
	private TextField smtpPasswordInput;
	@FXML
	private TextField imapPasswordInput;
	@FXML
	private TextField maxMailSizeInput;
	@FXML
	private TextField maximumRouteSizeInput;
	@FXML
	private TextField wifiDirectOffMinutesInput;
	@FXML
	private TextField tcpPortInput;
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
	private RadioButton syncMediumSelectTCP;
	@FXML
	private Label tcpStartedMessageLabel;


	// Konstruktor
	public SettingsController() {
		super(App.class.getResource("views/settingsView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);
	}



	// Klick auf Speichern-Button
	@FXML
	private void onSettingsSaveButtonClick() {
		//mailAddress = mailAddressInput.getText();

		settings.setEmail(mailAddressInput.getText());
		// TODO settings.setPort(portInput.getText());
		settings.setSmtpServer(smtpServerInput.getText());
		settings.setImapServer(imapServerInput.getText());
		settings.setSmtpPassword(smtpPasswordInput.getText());
		settings.setImapPassword(imapPasswordInput.getText());

		// Maximale Mailgröße speichern
		try {
			settings.setMailboxSize(Integer.parseInt(maxMailSizeInput.getText()));
		}catch (Exception e){e.printStackTrace();}

		// Speichern, nach wie vielen Min. WiFi Direct ausgeschaltet werden soll
		try{
			// TODO
			settings.setWifiON(Integer.parseInt(wifiDirectOffMinutesInput.getText()));
		}catch (Exception e){e.printStackTrace();}

		System.out.println("Mail address= "+ mailAddressInput.getText());
		System.out.println("Port: " + portInput.getText());
		System.out.println("SMTP= "+ smtpServerInput.getText());
		System.out.println("IMAP= " + imapServerInput.getText());
		System.out.println("Wifi Direct off after: " + wifiDirectOffMinutesInput.getText() + " min");

		if(radarOnRadioButton.isSelected()){
			settings.setRadarON(true);
		}else if(radarOffRadioButton.isSelected()){
			settings.setRadarON(false);
		}
	}


	// TCP-Server starten
	@FXML
	private void onStartTCPServerButtonClick(){
		try{
			tcpPort = Integer.parseInt(tcpPortInput.getText());
		}catch (Exception e){ tcpPort = 6000; }
		System.out.println("TCP server started on port " + tcpPort);
		tcpStartedMessageLabel.setText("TCP-Server läuft unter:   " + tcpAddress + ":" + tcpPort);
		startTCPserver(tcpPort);
	}

	// TCP-Server stoppen
	@FXML
	private void onStopTCPServerButtonClick(){
		System.out.println("TCP server stopped");
		stopTCPserver();
		tcpStartedMessageLabel.setText("");
	}

	// Routingliste für Kontakte öffnen
	@FXML
	private void onChooseContactsRoutingButtonClick(){
		System.out.println("choose contacts for routing");
		new ChooseRoutingContactsController();
	}
	// Routingliste für Interessen öffnen
	@FXML
	private void onChooseInterestsRoutingButtonClick(){
		System.out.println("choose interests for routing");
		new ChooseRoutingInterestsController();
	}


	// Routing-Einstellungen speichern
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

	// Sync starten
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




	@Override
	protected void onFxmlLoaded() {
		settings = sharkNetModel.getMyProfile().getSettings();
		routingContacts = settings.getRoutingContacts();
		routingInterest = settings.getRoutingInterests();


		tcpStartedMessageLabel.setText("");

		//TODO switchOffWifiDirectAfterMin = settings.getWiFiOffMin();

		// Radar-Radiobutton setzen
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

		else if(settings.getTcp()){
			syncMediumSelectTCP.setSelected(true);
		}

		// Textfelder und Checkboxen füllen
		mailAddressInput.setText("" + settings.getEmail());
		//TODO portInput.setText("" + settings.getPort());
		imapServerInput.setText("" + settings.getImapServer());
		smtpServerInput.setText("" + settings.getSmtpServer());
		smtpPasswordInput.setText("" + settings.getSmtpPassword());
		imapPasswordInput.setText("" + settings.getImapPassword());
		maxMailSizeInput.setText("" + settings.getMailboxSize());
		maximumRouteSizeInput.setText("" + settings.getMaxFileSize());
		wifiDirectOffMinutesInput.setText("" + switchOffWifiDirectAfterMin);

		// Checkboxen setzen
		if(settings.isSyncProfile()){ profileSyncCheckbox.setSelected(true); }
		if(settings.isSyncConctact()){ contactsSyncCheckbox.setSelected(true); }
		if(settings.isSyncTimeline()){	timelineSyncCheckbox.setSelected(true); }
		if(settings.isSyncChat()){ messagesSyncCheckbox.setSelected(true); }
		if(settings.isSyncHausaufgaben()){ homeworkSyncCheckbox.setSelected(true); }
	}


	// Methoden stubs für Sync
	private void syncViaWifi(){}
	private void syncViaMail(){}
	private void syncViaBluetooth(){}
	private void syncViaTCP(){}
	private void startTCPserver(int tcpPort){}
	private void stopTCPserver(){}

}
