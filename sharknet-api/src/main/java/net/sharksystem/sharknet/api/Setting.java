package net.sharksystem.sharknet.api;

/**
 * Created by timol on 12.05.2016.
 */
public interface Setting {

	public boolean getNfc();

	public void setNfc(boolean nfc);

	public boolean getBluetooth();

	public void setBluetooth(boolean bluetooth);

	public boolean getTcp();

	public void setTcp(boolean tcp);

	public boolean getWifi();

	public void setWifi(boolean wifi);

	public boolean getMail();

	public void setMail(boolean mail);

	public boolean getRadarON();

	public void setRadarON(boolean radarON);

	public boolean getWifiON();

	public void setWifiON(boolean wifiON);

	public int getMaxFileSize();

	public void setMaxFileSize(int maxFileSize);

	public String getSmtpServer();

	public void setSmtpServer(String smtpServer);

	public String getImapServer();

	public void setImapServer(String imapServer);

	public void save();
	public void delete();

	public void syncData();
	public void generateKeyPair();


}
