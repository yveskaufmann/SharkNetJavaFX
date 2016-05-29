package net.sharksystem.sharknet.api;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */

public class ImplChat implements Chat {
//ToDo: Implement Conact c is not being used (only listed contacts)
	Contact c;
	List<Message> message_list = new LinkedList<>();
	List<Contact> contact_list = new LinkedList<>();

	public ImplChat(Contact c){

		this.contact_list.add(c);
		this.c = c;

	}
	public ImplChat(List <Contact> contact_List){
		this.contact_list = contact_List;
	}

	@Override
	public void sendMessage(String message) {

		Message m = new ImplMessage(message, c);
		message_list.add(m);

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
		fillChat();
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

	/**
	 * This Method is used to fill a Chat with Messages that are already in the Database and is only called by the API itself
	 */
	public void fillChat(){

		//ToDo: Shark - find Messages blonging to the chat and fill List of Messages

		//DummyDB Implememntation
		message_list = DummyDB.getInstance().getMessageList(this);

	}

}
