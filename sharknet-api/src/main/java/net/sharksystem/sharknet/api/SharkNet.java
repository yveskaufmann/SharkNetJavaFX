package net.sharksystem.sharknet.api;

import java.util.List;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents the Main-Functionality of SharkNet
 */
public interface SharkNet {

	/**
	 * Returns a List of all personal Profiles
	 * @return
     */
    public List<Profile> getProfiles();

	/**
	 * Returns a list of all Feeds which should be displayed in the Timeline
	 * @return
     */
    public List<Feed> getFeeds(int Anzahl);

	/**
	 * returns a list of all safed contacts
	 * @return
     */
    public List<Contact> getContacts();

	/**
	 * returns a list of all chats
	 * @return
     */
    public List<Chat> getChats();

	/**
	 * initializes a new Feed an Safes it in the Knowledgebase
	 */
	public Feed newFeed(String content, Interest interest, Contact sender);


	/**
	 * Initilizes a Profile an Safes it in the Knowledgebase
	 * @param c
	 * @return
     */
	public Profile newProfile(Contact c);

	/**
	 * Initializes a Chat and safes it in the KnowledgeBase
	 * @param c
	 */
	public Chat newChat(Contact c);

	/**
	 * Adds a Contact to the KnowledgeBase
	 * @param nickname
	 * @param uid
	 */
	public Contact newContact(String nickname, String uid, String publickey);

}
