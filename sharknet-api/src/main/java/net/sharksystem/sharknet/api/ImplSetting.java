package net.sharksystem.sharknet.api;

/**
 * Created by timol on 22.06.2016.
 */
public class ImplSetting implements Setting{
	boolean nfc, bluetooth, tcp, wifi, mail;
	boolean radarON;
	boolean wifiON;
	int maxFileSize;
	String smtpServer, imapServer;
	Profile owner;

	/**
	 * Constructor for existing Settings in the Database
	 * @param nfc
	 * @param bluetooth
	 * @param tcp
	 * @param wifi
	 * @param mail
	 * @param radarON
	 * @param wifiON
	 * @param maxFileSize
	 * @param smtpServer
     * @param imapServer
     * @param owner
     */
	public ImplSetting(boolean nfc, boolean bluetooth, boolean tcp, boolean wifi, boolean mail, boolean radarON, boolean wifiON, int maxFileSize, String smtpServer, String imapServer, Profile owner) {
		this.nfc = nfc;
		this.bluetooth = bluetooth;
		this.tcp = tcp;
		this.wifi = wifi;
		this.mail = mail;
		this.radarON = radarON;
		this.wifiON = wifiON;
		this.maxFileSize = maxFileSize;
		this.smtpServer = smtpServer;
		this.imapServer = imapServer;
		this.owner = owner;
	}

	/**
	 * Constructor for new Settings
	 * @param owner
     */
	public ImplSetting(Profile owner){
		this.owner = owner;
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
	public boolean getWifiON() {
		return wifiON;
	}
	@Override
	public void setWifiON(boolean wifiON) {
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


}
