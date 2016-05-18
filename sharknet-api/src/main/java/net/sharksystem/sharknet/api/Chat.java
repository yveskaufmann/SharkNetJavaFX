package net.sharksystem.sharknet.api;

import java.util.List;

//ToDo: Implement - Groupchat functionality
//ToDo: Clearify - Groups List of Contacts ? Chat returns always a List of Contacts (one - normal chat), Groupname

/**
 * Created by timol on 12.05.2016.
 *
 * Interface for the Chat Implementation - A Chat represents a Conversation with one or more contacts
 */
public interface Chat {



	/**
	 * Add a Message to the Chat and sends it
	 * @param message
     */
	public void sendMessage(String message);

	/**
	 * deletes the Chat from the KnowledgeBase
	 */
	public void deleteChat();

	/**
	 * returns a List of all Messages within the Chat
	 * @return
     */
    public List<Message> getMessages();
}
