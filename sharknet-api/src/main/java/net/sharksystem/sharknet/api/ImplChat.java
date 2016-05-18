package net.sharksystem.sharknet.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplChat implements Chat {

	Contact c;
	List<Message> message_list = new LinkedList<>();

	public ImplChat(Contact c){
		this.c = c;
	}

	@Override
	public void sendMessage(String message) {

		Message m = new ImplMessage(message, c);
		message_list.add(m);

		//ToDo: Clearify - always renew vector when sth is deleted
	}

	@Override
	public void deleteChat() {
		//ToDo: Shark - delete Chat from Database
	}

	@Override
	public List<Message> getMessages() {
		fillChat();
		return message_list;
	}

	/**
	 * This Method is used to fill a Chat with Messages that are already in the Database and is only called by the API itself
	 */
	public void fillChat(){
		//ToDo: Shark - find Messages blonging to the chat and fill List of Messages
	}

}
