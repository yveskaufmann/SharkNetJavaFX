package net.sharksystem.sharknet.api;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by timol on 16.05.2016.
 */

public class ImplChat implements Chat {

	List<Contact> contact_list = new LinkedList<>();
	Contact sender;
	String title;
	String picture;
	int id;


	public ImplChat(List <Contact> contact_List, Contact sender){
		this.contact_list = contact_List;
		this.sender = sender;
		setDefaultTitle();
		setID();
	}

	@Override
	public void sendMessage(String message) {

		Message m = new ImplMessage(message, contact_list, sender);

		//ToDo: Clearify - always renew vector when sth is deleted
	}

	@Override
	public void delete() {
		//ToDo: Shark - delete Chat from Database
		//DummyDB implementation
		DummyDB.getInstance().removeChat(this);

	}

	@Override
	public List<Message> getMessages() {
		//ToDo: Shark - find Messages blonging to the chat and fill List of Messages

		//DummyDB Implememntation
		List <Message> message_list = DummyDB.getInstance().getMessageList(this);
		return message_list;
	}

	@Override
	public void save() {
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
		return contact_list;
	}

	@Override
	public void setPicture(String picture) {
		//ToDo: Implement - Picture
	}

	@Override
	public String getPicture() {
		return null;
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
	 * This Method is used to fill a Chat with Messages that are already in the Database and is only called by the API itself
	 */


	private void setDefaultTitle(){
		String[] title_array = new String[contact_list.size()];
		int i = 0;
		for(Contact c : contact_list){
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

}
