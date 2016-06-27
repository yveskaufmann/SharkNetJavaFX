package net.sharksystem.sharknet.api;

/**
 * Created by timol on 12.05.2016.
 */
public interface Setting {

	/**
	 * Returns if NFC is enabled
	 * @return
     */
	public boolean getNfc();

	/**
	 * Enable or Disable DataExchange via NFC
	 * @param nfc
     */
	public void setNfc(boolean nfc);

	/**
	 * Returns if Bluetooth is enabled
	 * @return
     */
	public boolean getBluetooth();

	/**
	 * Enable or Disable DataExchange via Bluetooth
	 * @param bluetooth
     */
	public void setBluetooth(boolean bluetooth);

	/**
	 * Returns if TCP is enabled
	 * @return
     */
	public boolean getTcp();

	/**
	 * Enable or Disable DataExchange via TCP
	 * @param tcp
     */
	public void setTcp(boolean tcp);

	/**
	 * Returns if Wifi is enabled
	 * @return
     */
	public boolean getWifi();

	/**
	 * Enable or Disable DataExchange via Wifi
	 * @param wifi
     */

	public void setWifi(boolean wifi);

	/**
	 * Returns if Mail for Dataexchange is enabled
	 * @return
     */
	public boolean getMail();

	/**
	 * Enable or Disable DataExchange via Mail
	 * @param mail
     */
	public void setMail(boolean mail);

	/**
	 * Returns if the Radar-Funcionality is enabled
	 * @return
     */
	public boolean getRadarON();


	/**
	 * Enable or Disable Radar Functionality
	 * @param radarON
     */
	public void setRadarON(boolean radarON);

	/**
	 * Returns the time Wifi should be enabled before it`s going to be disabled
	 * @return
     */
	public int getWifiON();

	/**
	 * Sets the time Wifi should be enabled before it`s going to be disabled
	 * @param wifiON
     */
	public void setWifiON(int wifiON);

	/**
	 * Returns maximal file size which is going to be sended or received
	 * @return
     */
	public int getMaxFileSize();

	/**
	 * Sets maximal file size which is going to be sended or received
	 * @param maxFileSize
     */
	public void setMaxFileSize(int maxFileSize);

	/**
	 * Returns SMTP-Server for Exchange Data per Mail
	 * @return
     */
	public String getSmtpServer();

	/**
	 * Sets SMTP-Server for Exchange Data per Mail
	 * @param smtpServer
     */
	public void setSmtpServer(String smtpServer);

	/**
	 * Returns IMAP-Server for Exchange Data per Mail
	 * @return
     */
	public String getImapServer();

	/**
	 * Sets IMAP-Server for Exchange Data per Mail
	 * @param imapServer
     */
	public void setImapServer(String imapServer);

	/**
	 * Save Settings in Database
	 */
	public void save();

	/**
	 * Deletes Settings in Database
	 */
	public void delete();

	/**
	 * Syncs Data through configured way
	 */
	public void syncData();

	/**
	 * Generates a new Pair of Keys and deletes the old ones
	 */
	public void generateKeyPair();

	/**
	 * Returns Email-Adress which is configured for Data-Exchange
	 * @return
     */
	public String getEmailPassword();

	/**
	 * Sets Email-Adress which is configured for Data-Exchange
	 * @param emailPassword
     */
	public void setEmailPassword(String emailPassword);

	/**
	 * Returns Password of Email-Adress which is configured for Data-Exchange
	 * @return
     */
	public String getEmail();

	/**
	 * Sets Password of Email-Adress which is configured for Data-Exchange
	 * @param email
     */
	public void setEmail(String email);


	/**
	 * Returns of synchronization of Hausaufgaben is enabled
	 * @return
     */
	public boolean isSyncHausaufgaben();

	/**
	 * Enable or disable synchronization of Hausaufgaben
	 * @param syncHausaufgaben
     */
	public void setSyncHausaufgaben(boolean syncHausaufgaben);

	/**
	 * Returns of synchronization of Timeline is enabled
 	 * @return
     */
    public boolean isSyncTimeline();

	/**
	 * Enable or disable synchronization of Timeline
	 * @param syncTimeline
     */
	public void setSyncTimeline(boolean syncTimeline);

	/**
	 * Returns of synchronization of Chat is enabled
	 * @return
     */
	public boolean isSyncChat();

	/**
	 * Enable or disable synchronization of Chat
	 * @param syncChat
     */
	public void setSyncChat(boolean syncChat);

	/**
	 * Returns of synchronization of Contacts is enabled
	 * @return
     */
	public boolean isSyncConctact();

	/**
	 * Enable or disable synchronization of Contacts
	 * @param syncConctact
     */
	public void setSyncConctact(boolean syncConctact);

	/**
	 * Returns of synchronization of Profile is enabled
	 * @return
     */
	public boolean isSyncProfile() ;

	/**
	 * Enable or disable synchronization of Profile
	 * @param syncProfile
     */

	public void setSyncProfile(boolean syncProfile);

}
