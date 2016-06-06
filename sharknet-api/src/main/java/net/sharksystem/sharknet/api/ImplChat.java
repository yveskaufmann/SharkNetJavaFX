package net.sharksystem.sharknet.api;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by timol on 16.05.2016.
 */

public class ImplChat implements Chat {

	String title;
	Content picture;
	int id;
	Profile owner;
	Timestamp lastmessage;

	/**
	 * Constructor for new Chat which is going to be saved in the DB
	 * @param contact_List
	 * @param owner
     */

	public ImplChat(List <Contact> contact_List, Profile owner){
		this.owner = owner;
		safeContactList(contact_List);
		setDefaultPic();
		setDefaultTitle();
		setID();
		setDefaultLastMessage();
		save();
	}

	/**
	 * Constructor for Chats from the Database which are not going to be saved
	 * @param owner
	 * @param title
	 * @param picture
     * @param id
     */

	public ImplChat(Profile owner, String title, Content picture, int id, Timestamp lastmessage){
		this.title=title;
		this.picture = picture;
		this.id = id;
		this.owner = owner;
		this.lastmessage = lastmessage;

	}


	@Override
	public void sendMessage(Content content) {
		Message m = new ImplMessage(content, getContacts(), owner.getContact(), owner);
	}

	@Override
	public void delete() {
		//ToDo: Shark - delete Chat from Database
		//DummyDB implementation
		DummyDB.getInstance().removeChat(this);
	}

	@Override
	public List<Message> getMessages() {
		//ToDo: Shark - find Messages blonging to the chat AND ALSO THE OWNER OF THE CHAT (which is the currently aktive profile and fill List of Messages
		//DummyDB Implememntation
		List <Message> message_list = DummyDB.getInstance().getMessageList(this);
		return message_list;
	}

	@Override
	public List<Message> getMessages(int startIndex, int stopIndex) {
		//ToDo: Shark - fill List with Messages from the chat within the given intervall - sorted by time
		List <Message> message_list = DummyDB.getInstance().getMessageList(this, startIndex, stopIndex);
		return message_list;

	}

	@Override
	public List<Message> getMessages(Timestamp start, Timestamp stop) {
		//ToDo: Shark - fill List with Messages from the chat within the given timerange - sorted by time
		List <Message> message_list = DummyDB.getInstance().getMessageList(this, start, stop);
		return message_list;
	}

	@Override
	public List<Message> getMessages(Timestamp start, Timestamp stop, int startIndex, int stopIndex) {
		//ToDo: Shark - fill List with Messages from the chat within the given intervall and timerange - sorted by time
		List <Message> message_list = DummyDB.getInstance().getMessageList(this, startIndex, stopIndex, start, stop);
		return message_list;

	}

	@Override
	public List<Message> getMessages(String search, int startIndex, int stopIndex) {
		//ToDo: Shark - fill List with Messages from the chat within the given intervall and containing search string - sorted by time
		List <Message> message_list = DummyDB.getInstance().getMessageList(this, search, startIndex, stopIndex);
		return message_list;
	}

	/**
	 * Save Chat to the DB
	 */
	private void save() {
		//ToDo: Shark - Safe Chat to the Database
		//DummmyDB implementaion
		DummyDB.getInstance().addChat(this);
	}

	@Override
	public void update() {
		//ToDo: Shark - Update Chat in KB
	}

	@Override
	public List<Contact> getContacts() {
		return DummyDB.getInstance().getChatContacts(this);
	}

	@Override
	public void setPicture(Content picture) {
		this.picture = picture;
	}

	@Override
	public Content getPicture() {
		return picture;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public Profile getOwner() {
		return owner;
	}

	@Override
	public Timestamp getTimestamp() {
		//ToDo: Shark - get Timestamp from the most recent Message
		Timestamp recentMessage = null;
		if(!DummyDB.getInstance().getMessageList(this).isEmpty()){
			DummyDB.getInstance().getMessageList(this).get(0).getTimestamp();
		}

		return recentMessage;
	}

	/**
	 * This Method is used to give the Chat a random ID including to check within the Database that the id is unique
	 */
	private void setID(){

		Random rand = new Random();
		int randomNum = rand.nextInt();

		//ToDo: Shark - Validate that Id ist unique (just in the Local KB not in the hole Sharknet-Network)
		//TODo: Shark - Check if the Chat already have a ID in the KB and use it, the chat can be find with the contact_list
		while(!DummyDB.getInstance().validChatID(randomNum)){
			randomNum = rand.nextInt(Integer.SIZE - 1);
		}
		id = randomNum;
	}

	/**
	 * Sets the default Title which is the Nickname of all Contacts in the List
	 */
	private void setDefaultTitle(){
		String[] title_array = new String[getContacts().size()];
		int i = 0;
		for(Contact c : getContacts()){
			title_array[i] = c.getNickname();
			i++;
		}

		StringBuilder builder = new StringBuilder();
		for(String s : title_array) {
			builder.append(s);
			builder.append(" ");
		}
		title = builder.toString();
	}

	/**
	 * Sets a default picture. The Picture of the first Contact in the List
	 */
	private void setDefaultPic(){
		setPicture(getContacts().get(0).getPicture());
	}

	/**
	 * Sets the default Value for Timestamp lastmessage which is the creation date
	 */
	private void setDefaultLastMessage(){
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		lastmessage = new java.sql.Timestamp(now.getTime());
	}

	/**
	 * Private Method to SetContactList in Database
	 */
	private void safeContactList(List<Contact> contact_list) {
		//ToDo: Shark - Safe ContactList int the Database
		//DummmyDB implementaion
		DummyDB.getInstance().setChatContacts(this, contact_list);
	}

}

