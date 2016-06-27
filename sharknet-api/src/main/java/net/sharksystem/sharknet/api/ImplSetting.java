package net.sharksystem.sharknet.api;

/**
 * Created by timol on 22.06.2016.
 */
public class ImplSetting implements Setting{

	/**
	 * Gernal exchange of Data
	 */
	boolean nfc, bluetooth, tcp, wifi, mail;
	/**
	 * Synchronization between Devices of the Same owner
	 */
	boolean syncnfc, syncbluetooth, synctcp, syncwifi, syncmail;
	/**
	 * Categories which are synchronized
	 */
	boolean syncProfile, syncConctact, syncChat, syncTimeline, syncHausaufgaben;
	/**
	 * Enables or Disables Radar functionality
	 */
	boolean radarON;
	/**
	 * Minutes till the Wifi is Auto-Turned off
	 */
	int wifiON;

	/**
	 * Maximal Filesize in MB which are going to be sended or reveived
	 */
	int maxFileSize;
	/**
	 * Mail-Data for Dataexchange
	 */
	String smtpServer, imapServer;
	String emailPassword, email;
	/**
	 * Owner of the Settings
	 */
	Profile owner;


	/**
	 * Constructor for existing Settings in the Database
	 * @param nfc
	 * @param bluetooth
	 * @param tcp
	 * @param wifi
	 * @param mail
	 * @param syncnfc
	 * @param syncbluetooth
	 * @param synctcp
	 * @param syncwifi
	 * @param syncmail
	 * @param syncProfile
	 * @param syncConctact
	 * @param syncChat
	 * @param syncTimeline
	 * @param syncHausaufgaben
	 * @param radarON
	 * @param wifiON
	 * @param maxFileSize
	 * @param smtpServer
     * @param imapServer
     * @param emailPassword
     * @param email
     * @param owner
     */
	public ImplSetting(boolean nfc, boolean bluetooth, boolean tcp, boolean wifi, boolean mail, boolean syncnfc, boolean syncbluetooth, boolean synctcp, boolean syncwifi, boolean syncmail, boolean syncProfile, boolean syncConctact, boolean syncChat, boolean syncTimeline, boolean syncHausaufgaben, boolean radarON, int wifiON, int maxFileSize, String smtpServer, String imapServer, String emailPassword, String email, Profile owner) {
		this.nfc = nfc;
		this.bluetooth = bluetooth;
		this.tcp = tcp;
		this.wifi = wifi;
		this.mail = mail;
		this.syncnfc = syncnfc;
		this.syncbluetooth = syncbluetooth;
		this.synctcp = synctcp;
		this.syncwifi = syncwifi;
		this.syncmail = syncmail;
		this.syncProfile = syncProfile;
		this.syncConctact = syncConctact;
		this.syncChat = syncChat;
		this.syncTimeline = syncTimeline;
		this.syncHausaufgaben = syncHausaufgaben;
		this.radarON = radarON;
		this.wifiON = wifiON;
		this.maxFileSize = maxFileSize;
		this.smtpServer = smtpServer;
		this.imapServer = imapServer;
		this.emailPassword = emailPassword;
		this.email = email;
		this.owner = owner;
	}

	/**
	 * Constructor for new Settings
	 * @param owner
     */
	public ImplSetting(Profile owner){
		this.owner = owner;
		setDefaultData();
	}

	@Override
	public boolean isSyncnfc() {
		return syncnfc;
	}
	@Override
	public void setSyncnfc(boolean syncnfc) {
		this.syncnfc = syncnfc;
	}
	@Override
	public boolean isSyncbluetooth() {
		return syncbluetooth;
	}
	@Override
	public void setSyncbluetooth(boolean syncbluetooth) {
		this.syncbluetooth = syncbluetooth;
	}
	@Override
	public boolean isSynctcp() {
		return synctcp;
	}
	@Override
	public void setSynctcp(boolean synctcp) {
		this.synctcp = synctcp;
	}
	@Override
	public boolean isSyncwifi() {
		return syncwifi;
	}
	@Override
	public void setSyncwifi(boolean syncwifi) {
		this.syncwifi = syncwifi;
	}
	@Override
	public boolean isSyncmail() {
		return syncmail;
	}
	@Override
	public void setSyncmail(boolean syncmail) {
		this.syncmail = syncmail;
	}

	@Override
	public boolean isSyncHausaufgaben() {
		return syncHausaufgaben;
	}

	@Override
	public void setSyncHausaufgaben(boolean syncHausaufgaben) {
		this.syncHausaufgaben = syncHausaufgaben;
	}

	@Override
	public boolean isSyncTimeline() {
		return syncTimeline;
	}

	@Override
	public void setSyncTimeline(boolean syncTimeline) {
		this.syncTimeline = syncTimeline;
	}

	@Override
	public boolean isSyncChat() {
		return syncChat;
	}

	@Override
	public void setSyncChat(boolean syncChat) {
		this.syncChat = syncChat;
	}

	@Override
	public boolean isSyncConctact() {
		return syncConctact;
	}

	@Override
	public void setSyncConctact(boolean syncConctact) {
		this.syncConctact = syncConctact;
	}

	@Override
	public boolean isSyncProfile() {
		return syncProfile;
	}

	@Override
	public void setSyncProfile(boolean syncProfile) {
		this.syncProfile = syncProfile;
	}

	@Override
	public String getEmailPassword() {
		return emailPassword;
	}

	@Override
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean getNfc() {
		return nfc;
	}

	@Override
	public void setNfc(boolean nfc) {
		this.nfc = nfc;
	}

	@Override
	public boolean getBluetooth() {
		return bluetooth;
	}

	@Override
	public void setBluetooth(boolean bluetooth) {
		this.bluetooth = bluetooth;
	}
	@Override

	public boolean getTcp() {
		return tcp;
	}
	@Override
	public void setTcp(boolean tcp) {
		this.tcp = tcp;
	}
	@Override
	public boolean getWifi() {
		return wifi;
	}
	@Override
	public void setWifi(boolean wifi) {
		this.wifi = wifi;
	}
	@Override
	public boolean getMail() {
		return mail;
	}
	@Override
	public void setMail(boolean mail) {
		this.mail = mail;
	}
	@Override
	public boolean getRadarON() {
		return radarON;
	}
	@Override
	public void setRadarON(boolean radarON) {
		this.radarON = radarON;
	}
	@Override
	public int getWifiON() {
		return wifiON;
	}
	@Override
	public void setWifiON(int wifiON) {
		this.wifiON = wifiON;
	}
	@Override
	public int getMaxFileSize() {
		return maxFileSize;
	}
	@Override
	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	@Override
	public String getSmtpServer() {
		return smtpServer;
	}
	@Override
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	@Override
	public String getImapServer() {
		return imapServer;
	}
	@Override
	public void setImapServer(String imapServer) {
		this.imapServer = imapServer;
	}

	@Override
	public void save() {
		//ToDo: Shark - Save Settings in DB
	}

	@Override
	public void delete() {
		//ToDo: Shark - delete Settings in DB
	}

	@Override
	public void syncData() {
		//ToDo: Shark - Implement Sync of Data for example with a copy of the KB
	}

	@Override
	public void generateKeyPair() {
		//ToDo: Shark - Generate a new Key Pair and save it in the contact
	}

	/**
	 * Generates the default Data when the settings are initialized for the first time
	 */
	private void setDefaultData(){
		radarON = false;
		nfc = false;
		bluetooth = false;
		tcp = false;
		wifi = true;
		mail = false;

		syncProfile = true;
		syncConctact = true;
		syncChat = true;
		syncTimeline = true;
		syncHausaufgaben = true;

		syncnfc = false;
		syncbluetooth = false;
		synctcp = false;
		syncwifi = false;
		syncmail = true;

		wifiON = 10;
		maxFileSize = 10;

	}

}
