package net.sharksystem.sharknet.api;


import java.sql.Timestamp;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplMessage implements Message {

	String message;
	Contact sender, recipient;
	Timestamp time;
	boolean isSigned, isEncrypted;

	/**
	 * Constructor for Messages which are from the Datebase and are not going to be sended, just used by the API to fill List of Messages
	 * @param message
	 * @param time
	 * @param sender
	 * @param recipient
	 * @param isSigned
     * @param isEncrypted
     */
	public ImplMessage(String message, Timestamp time, Contact sender, Contact recipient, boolean isSigned, boolean isEncrypted){
		this.message = message;
		this.time = time;
		this.sender = sender;
		this.recipient = recipient;
		this.isSigned = isSigned;
		this.isEncrypted = isEncrypted;

	}

	/**
	 * Constuctor for New Messages that are going to be sended
	 * @param message
	 * @param recipient
     */

	public ImplMessage(String message, Contact recipient){
		this.message = message;
		this.recipient = recipient;
		sendMessage();
	}

	/**
	 * writes the Message in the Database and sends it, is only called by the constructor for new Messages
	 */
	private void sendMessage(){
		//ToDo: Shark - safe the Message in the Database and send it
	}

	@Override
	public Timestamp getTimestamp() {
		return time;
	}
	@Override
	public Contact getSender() {
		return sender;
	}

	@Override
	public Contact getRecipient() {
		return recipient;
	}

	@Override
	public String getContent() {
		return message;

		//ToDo: Implement Filefunctionality
	}

	@Override
	public boolean isSigned() {
		return isSigned;
	}

	@Override
	public boolean isEncrypted() {
		return isEncrypted;
	}

	@Override
	public void deleteMessage() {
		//ToDo: Shark - delete the message from the Database

	}
}
