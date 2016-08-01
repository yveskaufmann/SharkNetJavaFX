package net.sharksystem.sharknet.api;

import java.sql.Timestamp;
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
	public List<Feed> getFeeds(boolean descending);
    public List<Feed> getFeeds(int start_index, int stop_index, boolean descending);
	public List<Feed> getFeeds(Interest i, int start_index, int stop_index, boolean descending);
	public List<Feed> getFeeds(String search, int start_index, int stop_index, boolean descending);
	public List<Feed> getFeeds(Timestamp start, Timestamp end, int start_index, int stop_index, boolean descending);

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
	public Feed newFeed(Content content, Interest interest, Contact sender);


	/**
	 * Initilizes a Profile an Safes it in the Knowledgebase
	 * @return
     */
	public Profile newProfile(String nickname, String deviceID);

	/**
	 * Initializes a Chat and safes it in the KnowledgeBase
	 * @param recipients
	 */
	public Chat newChat(List<Contact> recipients);

	/**
	 * Adds a Contact to the KnowledgeBase
	 * @param nickname
	 * @param uid
	 */
	public Contact newContact(String nickname, String uid, String publickey);


	/**
	 * Set the Profile the User want to use during the session
	 * @param myProfile
	 * @param password
	 * @return true if the authentification was sucessfull, false if not
     */

	public boolean setProfile(Profile myProfile, String password);

	/**
	 * Returns the Profile which is active at the moment
	 * @return
     */
	public Profile getMyProfile();

	public SchoolMetadata getSchoolMetadata();

	public SchoolMetadata getDummySchoolMetadata();

	public void setSchoolMetadata(SchoolMetadata schoolMetadata);
	/**
	 * Exchange Contact via NFC (just Method, not implemented)
     */
	public void exchangeContactNFC();

	/**
	 * Adds Listener to the List of Listener
 	 * @param p
	 * @param listener
     */
	public void addListener(Profile p, GetEvents listener);

	/**
	 * Removes a Listener
	 * @param p
	 * @param listener
     */
	public void removeListener(Profile p, GetEvents listener);

	/**
	 * Methods calls all Methods of registered listeners when the profile matches a contact in the recipientslist of the message
	 * @param m
     */
	public void informMessage(Message m);

	/**
	 * Methods calls all Methods of registered listeners and informs about feed
	 * @param f
     */
	public void informFeed(Feed f);

	/**
	 * Methods calls all Methods of registered listeners and informs about comment
	 * @param c
     */
	public void informComment(Comment c);

	/**
	 * Methods calls all Methods of registered listeners and informs about contact (for example when exchanged via NFC)
	 * @param c
	 */
	public void informContact(Contact c);


}
