package net.sharksystem.sharknet.javafx.controller;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sharksystem.sharknet.api.Setting;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controller.settings.ChooseRoutingContactsController;
import net.sharksystem.sharknet.javafx.controller.settings.ChooseRoutingInterestsController;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;


/******************************************************************************
 *
 * Dieser Controller kümmert sich um die Einstellungsseite der Anwendung.
 * Es werden allgemeine Einstellungen, Routing-Einstellungen und die Sync
 * verwaltet. Zugehörige View: settingsView.fxml
 *
 ******************************************************************************/

@Controller( title = "%sidebar.settings")
public class SettingsController extends AbstractController {

	@Inject
	private SharkNet sharkNetModel;

	private FrontController frontController;
	private Setting settings;
	private boolean radar;
	private String tcpAddress = "tcp://192.168....";
	private int tcpPort = 6000;
	private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String SERVER_PATTERN = "^[-a-zA-Z0-9_.]+\\.[-a-zA-Z0-9_]+";

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
	private TextField smtpPortInput;
	@FXML
	private TextField imapPortInput;
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


	// Einstellungen speichern
	@FXML
	private void onSettingsSaveButtonClick() {

		// Prüfen der Mailadresse
		if (mailAddressInput.getText() != null) {
			if(mailAddressInput.getText().matches(EMAIL_PATTERN)) {
				settings.setEmail(mailAddressInput.getText());
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Ungültige E-Mail-Adresse");
				alert.setContentText("Die eingegebene E-Mail-Adresse ist ungültig.");
				alert.setHeaderText("");
				alert.showAndWait();
				return;
			}
		}

		// Prüfen der Eingabe von SMTP-Server
		if(smtpServerInput.getText() != null){
			if(smtpServerInput.getText().matches(SERVER_PATTERN)){
				settings.setSmtpServer(smtpServerInput.getText() + ":" + smtpPortInput);
			}else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Ungültiger SMTP-Server");
				alert.setContentText("Die eingegebene SMTP-Serveradresse ist ungültig.");
				alert.setHeaderText("");
				alert.showAndWait();
				return;
			}
		}
		// Prüfen der Eingabe von IMAP-Server
		if(imapServerInput.getText() != null){
			if(imapServerInput.getText().matches(SERVER_PATTERN)){
				settings.setImapServer(imapServerInput.getText());
				settings.setImapServer(imapServerInput.getText() + ":" + imapPortInput);
			}else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Ungültiger IMAP-Server");
				alert.setContentText("Die eingegebene IMAP-Serveradresse ist ungültig.");
				alert.setHeaderText("");
				alert.showAndWait();
				return;
			}
		}

		settings.setSmtpPassword(smtpPasswordInput.getText());
		settings.setImapPassword(imapPasswordInput.getText());

		// Maximale Mailgröße speichern
		try { settings.setMailboxSize(Integer.parseInt(maxMailSizeInput.getText())); }
			catch (Exception e){e.printStackTrace();}

		// Speichern, nach wie vielen Min. WiFi Direct ausgeschaltet werden soll
		try{ settings.setWifiON(Integer.parseInt(wifiDirectOffMinutesInput.getText())); }
			catch (Exception e){e.printStackTrace();}

		// Port für SMTP- und IMAP- Server holen
		try{ settings.setSmtpPort(Integer.parseInt(smtpPortInput.getText())); }
			catch (Exception e){ e.printStackTrace(); }
		try{ settings.setImapPort(Integer.parseInt(imapPortInput.getText())); }
			catch (Exception e){ e.printStackTrace(); }

		if(radarOnRadioButton.isSelected()){
			settings.setRadarON(true);
		}else if(radarOffRadioButton.isSelected()){
			settings.setRadarON(false);
		}
	}


	// TCP-Server starten
	@FXML
	private void onStartTCPServerButtonClick(){
		tcpAddress = getMyInternalIP();
		try{
			tcpPort = Integer.parseInt(tcpPortInput.getText());
		}catch (Exception e){
			e.printStackTrace();
		}
		tcpStartedMessageLabel.setText("TCP-Server läuft unter:   " + tcpAddress + " : " + tcpPort);
		settings.startTCP();
	}


	// TCP-Server stoppen
	@FXML
	private void onStopTCPServerButtonClick(){
		settings.stopTCP();
		tcpStartedMessageLabel.setText("");
	}

	// Routingliste für Kontakte öffnen
	@FXML
	private void onChooseContactsRoutingButtonClick(){
		new ChooseRoutingContactsController();
	}
	// Routingliste für Interessen öffnen
	@FXML
	private void onChooseInterestsRoutingButtonClick(){
		new ChooseRoutingInterestsController();
	}


	// Routing-Einstellungen speichern
	@FXML
	private void onRoutingSaveButtonClick() {
		if (maximumRouteSizeInput.getText() != "") {
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

		// Auswerten der Checkboxen, was synchronisiert werden soll
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
	}


	@Override
	protected void onFxmlLoaded() {
		settings = sharkNetModel.getMyProfile().getSettings();
		tcpStartedMessageLabel.setText("");

		// Radar-Radiobutton setzen
		if(settings.getRadarON()){
			radarOnRadioButton.setSelected(true);
		}else radarOffRadioButton.setSelected(true);

		// Syncmedium-Radiobutton setzen
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
		imapServerInput.setText("" + settings.getImapServer());
		smtpServerInput.setText("" + settings.getSmtpServer());
		smtpPortInput.setText("" + settings.getSmtpPort());
		imapPortInput.setText("" + settings.getImapPort());
		smtpPasswordInput.setText("" + settings.getSmtpPassword());
		imapPasswordInput.setText("" + settings.getImapPassword());
		maxMailSizeInput.setText("" + settings.getMailboxSize());
		maximumRouteSizeInput.setText("" + settings.getMaxFileSize());
		wifiDirectOffMinutesInput.setText("" + settings.getWifiON());
		tcpPortInput.setText("6000");

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

	// Dummy, soll später die interne IP zurückgeben
	private String getMyInternalIP(){
		return "<Interne IP>";
	}
}
