package net.sharksystem.sharknet.api;


import java.sql.Timestamp;
import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplMessage implements Message {

	String message;
	Contact sender;
	List<Contact> recipient_list;
	Timestamp time;
	boolean isSigned, isEncrypted;

	/**
	 * Constructor for Messages which are from the Datebase and are not going to be sended, just used by the API to fill List of Messages
	 * @param message
	 * @param time
	 * @param sender
	 * @param recipient_list
	 * @param isSigned
     * @param isEncrypted
     */
	public ImplMessage(String message, Timestamp time, Contact sender, List<Contact> recipient_list, boolean isSigned, boolean isEncrypted){
		this.message = message;
		this.time = time;
		this.sender = sender;
		this.recipient_list= recipient_list;
		this.isSigned = isSigned;
		this.isEncrypted = isEncrypted;

	}

	/**
	 * Constuctor for New Messages that are going to be sended
	 * @param message
	 * @param recipient_list
     */

	public ImplMessage(String message, List<Contact> recipient_list){
		this.message = message;
		this.recipient_list = recipient_list;
		sendMessage();
	}

	/**
	 * writes the Message in the Database and sends it, is only called by the constructor for new Messages
	 */
	private void sendMessage(){
		//ToDo: Shark - safe the Message in the Database and send it
		//DummyDB Implementation
		DummyDB db = DummyDB.getInstance();
		db.addMessage(this, getChat());
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
	public List<Contact> getRecipients() {
		return recipient_list;
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

	/**
	 * Method is called to find the Chat the Message belongs to in the DummyDB
	 * @return
     */

	private Chat getChat(){
		DummyDB db = DummyDB.getInstance();
		List<Chat> chats = db.getChat_list();
		for(Chat c : chats){
			List<Contact> cs = c.getContacts();
			cs.equals(recipient_list);
/*			for(Contact currentc : cs){
				if(currentc.getUID().equals(rec_uid)){
					return c;
				}
		}
*/
		}
		return null;

	}
	//ToDo: Dummy - does not work with groupchats
}
