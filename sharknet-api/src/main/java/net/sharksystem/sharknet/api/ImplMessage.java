package net.sharksystem.sharknet.api;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplMessage implements Message {

	Profile owner;
	Contact sender;
	List<Contact> recipient_list;
	Timestamp time;
	boolean isSigned, isEncrypted;
	Content content;
	Boolean disliked = false;

	/**
	 * Constructor for Messages which are from the Datebase and are not going to be sended, just used by the API to fill List of Messages
	 * @param content
	 * @param time
	 * @param sender
	 * @param recipient_list
	 * @param isSigned
     * @param isEncrypted
     */
	public ImplMessage(Content content, Timestamp time, Contact sender, Profile owner, List<Contact> recipient_list, boolean isSigned, boolean isEncrypted){
		this.content = content;
		this.time = time;
		this.sender = sender;
		this.recipient_list= recipient_list;
		this.isSigned = isSigned;
		this.isEncrypted = isEncrypted;
		this.owner = owner;

	}

	/**
	 * Constuctor for New Messages that are going to be sended
	 * @param content
	 * @param recipient_list
     */

	public ImplMessage(Content content, List<Contact> recipient_list, Contact sender, Profile owner){
		this.content = content;
		this.owner = owner;
		this.recipient_list = recipient_list;
		this.sender = sender;
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		time = new java.sql.Timestamp(now.getTime());
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
	public Content getContent() {
		return content;
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

		Chat chat = getChat();
		DummyDB.getInstance().removeMessage(this, chat);

	}

	@Override
	public void dislike() {
		disliked = true;
		//ToDo: Shark - safe that the message was disliked
	}

	/**
	 * Method is called to find the Chat the Message belongs to in the DummyDB
	 * @return
     */

	private Chat getChat(){
		//Implementation of DummyDB
		//ToDo: Shark - lookup for the Chat
		DummyDB db = DummyDB.getInstance();
		List<Chat> chats = db.getChat_list(owner);
		for(Chat c : chats){
			List<Contact> cs = c.getContacts();
			if(cs.equals(recipient_list)){
				return c;
			}
		}
		return null;

	}

	@Override
	public boolean isMine(){
		if(sender.isEqual(owner.getContact())){
			return true;
		}
		else return false;
	}

	@Override
	public boolean isdisliked() {
		return disliked;
	}

}
