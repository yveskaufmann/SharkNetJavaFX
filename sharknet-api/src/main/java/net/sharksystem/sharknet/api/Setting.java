package net.sharksystem.sharknet.api;

import java.util.List;

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
	public String getImapPassword();

	/**
	 * Sets Email-Adress which is configured for Data-Exchange
	 * @param imapPassword
     */
	public void setImapPassword(String imapPassword);

	/**
	 * Returns Password of Email-Adress which is configured for Data-Exchange
	 * @return
     */
	public String getEmail();

	/**
	 * Returns the Password for the smtp Server
	 * @return
     */
	public String getSmtpPassword();

	/**
	 * Returns the Password for the smtp Server
	 * @param smtpPassword
     */
	public void setSmtpPassword(String smtpPassword);

	/**
	 * Returns the size of the Mailbox used for Dataexchange
	 * @return
     */
	public int getMailboxSize();

	/**
	 * Sets the size of the Mailbox used for dataexchange
	 * @param mailboxSize
     */
	public void setMailboxSize(int mailboxSize);


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


	/**
	 * Returns if Sync per NFC is enabled
	 * @return
     */
	public boolean isSyncnfc();

	/**
	 * Enables or Disables Sync per NFC
	 * @param syncnfc
     */
	public void setSyncnfc(boolean syncnfc);
	/**
	 * Returns if Sync per Bluetooth is enabled
	 * @return
	 */
	public boolean isSyncbluetooth();

	/**
	 * Enables or Disables Sync per Bluetooth
	 * @param syncbluetooth
	 */
	public void setSyncbluetooth(boolean syncbluetooth);
	/**
	 * Returns if Sync per TCP is enabled
	 * @return
	 */
	public boolean isSynctcp();

	/**
	 * Enables or Disables Sync per TCP
	 * @param synctcp
	 */
	public void setSynctcp(boolean synctcp);
	/**
	 * Returns if Sync per Wifi is enabled
	 * @return
	 */
	public boolean isSyncwifi();

	/**
	 * Enables or Disables Sync per Wifi
	 * @param syncwifi
	 */
	public void setSyncwifi(boolean syncwifi);
	/**
	 * Returns if Sync per Mail is enabled
	 * @return
	 */
	public boolean isSyncmail();

	/**
	 * Enables or Disables Sync per Mail
	 * @param syncmail
	 */
	public void setSyncmail(boolean syncmail);

	/**
	 * start a TCP Server on the Client
	 */
	public void startTCP();

	/**
	 * Stops the TCP-Server on the Client
	 */
	public void stopTCP();

	/**
	 * Sends Contact of the Sender per TCP-Server (must be started) to the recipient
	 * @param sender
	 * @param recipient
     */
	public void sendProfile(Contact sender, Contact recipient);

	/**
	 * Adds the Contacts to the allowed Routing List
	 * @param routingContacts
     */
	public void addRoutingContacts(List<Contact> routingContacts);

	/**
	 * Adds the Interests to the allowed Routing List
	 * @param routingInterests
     */
	public void addRoutingInterests(List<Interest> routingInterests);

	/**
	 * Removes the Contacts from the allowed Routing List
	 * @param routingContacts
     */
	public void deleteRoutingContacts(List<Contact> routingContacts);

	/**
	 * Removes the Interests from the allowed Routing List
	 * @param routingInterests
     */
	public void deleteRoutingInterests(List<Interest> routingInterests);

	/**
	 * Returns the Contacts of the allowed Routing List
	 * @return
     */
	public List<Interest> getRoutingInterests();

	/**
	 * Returns the Interests of the allowed Routing List
	 * @return
     */
	public List<Contact> getRoutingContacts();

	/**
	 * Sets the maximal File Size of Routing Data
	 * @param routingFileSize
     */
	public void setRoutingFileSize(int routingFileSize);

	/**
	 * Returns the maximal File Size of Routing Data
	 * @return
     */
	public int getRoutingFileSize();

}
