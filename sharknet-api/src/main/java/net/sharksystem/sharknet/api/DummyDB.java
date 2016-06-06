package net.sharksystem.sharknet.api;

import java.awt.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

/**
 * Created by timol on 28.05.2016.
 */
public class DummyDB {
	List<Feed> feed_list = new LinkedList<>();
	List<Profile> profile_list = new LinkedList<>();
	List<Contact> contact_list = new LinkedList<>();
	HashMap<Chat, List<Contact>> chatcontact= new HashMap<>();
	HashMap<Profile, List<Contact>> blacklist_map = new HashMap<>();
	List<Chat> chat_list = new LinkedList<>();
	HashMap<Chat, List<Message>> chatmessage = new HashMap<>();
	HashMap<Feed, List<Comment>> feedcomment = new HashMap<>();


	/**
	 * DummyDB is a Singleton which is used to Emulate a Database which is later replaced with shark
	 * The Database saves the Lists only during runtime of the Programm
	 */
	private DummyDB() {
	}

	private static DummyDB instance;

	public static DummyDB getInstance() {
		if (DummyDB.instance == null) {
			DummyDB.instance = new DummyDB();
		}
		return DummyDB.instance;
	}


	/**
	 * Method returns the Feeds of a Profile (owner)
	 *
	 * @param owner
	 * @return
	 */
	public List<Feed> getFeed_list(Profile owner) {

		List<Feed> swap_f_list = new LinkedList<>();
		swap_f_list.addAll(feed_list);

		for (int i = swap_f_list.size() - 1; i >= 0; i--) {
			Feed f = swap_f_list.get(i);
			if (!f.getOwner().isEqual(owner)) swap_f_list.remove(f);
		}

		return (List<Feed>) sortList(swap_f_list);
	}
	public List<Feed> getFeed_list(Profile owner, int startIndex, int stopIndex) {
		List <Feed> swaplist = (List<Feed>) sortList(getFeed_list(owner));
		swaplist = (List<Feed>) cutList(swaplist, startIndex, stopIndex);
		return swaplist;
	}


	public List<Feed> getFeed_list(Profile owner, String search, int startIndex, int stopIndex) {
		List <Feed> swaplist = (List<Feed>) sortList(getFeed_list(owner));
		swaplist = (List<Feed>) search(search, swaplist);
		swaplist = (List<Feed>) cutList(swaplist, startIndex, stopIndex);
		return swaplist;
	}


	public List<Feed> getFeed_list(Profile owner, int startIndex, int stopIndex, Timestamp start, Timestamp stop) {
		List <Feed> swaplist = (List<Feed>) sortList(getFeed_list(owner));
		swaplist = (List<Feed>) cutList(swaplist, start, stop);
		swaplist = (List<Feed>) cutList(swaplist, startIndex, stopIndex);
		return swaplist;
	}


	public List<Feed> getFeed_list(Profile owner, Timestamp start, Timestamp stop) {
		List <Feed> swaplist = (List<Feed>) sortList(getFeed_list(owner));
		swaplist = (List<Feed>) cutList(swaplist, start, stop);
		return swaplist;
	}


	/**
	 * Add a feed to the List
	 *
	 * @param f
	 */

	public void addfeed(Feed f) {
		feed_list.add(f);
		feedcomment.put(f, new LinkedList<Comment>());
	}

	/**
	 * Removes a feed from the List
	 *
	 * @param f
	 */

	public void removefeed(Feed f) {
		feed_list.remove(f);
		feedcomment.remove(f);
	}

//Comments Lists and Values

	/**
	 * Add a Comment to the Database
	 * @param c
	 * @param f
     */

	public void addComment(Comment c, Feed f) {
		if (feedcomment.containsKey(f)) {
			feedcomment.get(f).add(c);
		} else {
			feedcomment.put(f, new LinkedList<Comment>());
			feedcomment.get(f).add(c);
		}
	}

	/**
	 * Remove Comment from the Database
	 * @param c
	 * @param f
     */
	public void removeComment(Comment c, Feed f) {
		feedcomment.get(f).remove(c);
	}

	/**
	 * Returns a sorted list of all Comments
	 * @param f
	 * @return
     */
	public List<Comment> getComments(Feed f){
		return (List<Comment>) sortList(feedcomment.get(f));
	}

	/**
	 * Returns a sorted list of all Comments within the given timerange and intervall
	 * @param f
	 * @return
	 */
	public List<Comment> getComments(Feed f, int startIndex, int stopIndex, Timestamp start, Timestamp stop){
		List <Comment> swaplist = (List<Comment>) sortList(feedcomment.get(f));
		swaplist = (List<Comment>) cutList(swaplist, start, stop);
		swaplist = (List<Comment>) cutList(swaplist, startIndex, stopIndex);
		return swaplist;
	}

	/**
	 * Returns a sorted list of Comments within the given Intervall
	 * @param f
	 * @return
	 */
	public List<Comment> getComments(Feed f, int startIndex, int stopIndex){
		List <Comment> swaplist = (List<Comment>) sortList(feedcomment.get(f));
		swaplist = (List<Comment>) cutList(swaplist, startIndex, stopIndex);
		return swaplist;
	}

	/**
	 * Returns a sorted list of all Comments within the given Timerange
	 * @param f
	 * @return
	 */
	public List<Comment> getComments(Feed f, Timestamp start, Timestamp stop){
		List <Comment> swaplist = (List<Comment>) sortList(feedcomment.get(f));
		swaplist = (List<Comment>) cutList(swaplist, start, stop);
		return swaplist;
	}

	/**
	 * Returns a sorted list of all Comments which contains the searchstring
	 * @param f
	 * @return
	 */
	public List<Comment> getComments(Feed f, String search, int startIndex, int stopIndex){
		List <Comment> swaplist = (List<Comment>) sortList(feedcomment.get(f));
		swaplist = (List<Comment>) search(search, swaplist);
		swaplist = (List<Comment>) cutList(swaplist, startIndex, stopIndex);
		return swaplist;
	}




	//Manage Profile Suff

	/**
	 * Add Profile to the Database
	 * @param p
     */
	public void addProfile(Profile p) {
		profile_list.add(p);
	}

	/**
	 * Remove Proile from the Database
	 * @param p
     */
	public void removeProfile(Profile p) {
		profile_list.remove(p);
	}

	/**
	 * Returns List of all Profiles in Database
	 * @return
     */
	public List<Profile> getProfile_list() {
		return profile_list;
	}


	//Manage Contacts

	/**
	 * Returns a List of all Contacts of a Profile
	 * @param owner
	 * @return
     */

	//ToDo: Sort Alphabetically
	public List<Contact> getContact_list(Profile owner) {
		List<Contact> swap_c_list = new LinkedList<>();
		swap_c_list.addAll(contact_list);

		for (int i = swap_c_list.size() - 1; i >= 0; i--) {
			Contact c = swap_c_list.get(i);
			if (!c.getOwner().isEqual(owner)) swap_c_list.remove(c);
		}
		return swap_c_list;
	}

	/**
	 * Adds a Contact to the Database
	 * @param c
     */

	public void addContact(Contact c) {
		contact_list.add(c);
	}

	/**
	 * Removes a Contact from the Database
	 * @param c
     */

	public void removeContact(Contact c) {
		contact_list.remove(c);
	}


	//Manage the Chat Lists

	/**
	 * Adds a chat to the Database
	 * @param c
     */
	public void addChat(Chat c) {
		if (!chat_list.contains(c)) {
			chat_list.add(c);
			chatmessage.put(c, new LinkedList<Message>());
		}
	}

	/**
	 * Removes a Chat from the Database
	 * @param c
     */
	public void removeChat(Chat c) {
		chat_list.remove(c);
		chatmessage.remove(c);
	}

	/**
	 * Returns List of Contacts from a Chat
	 * @param c
	 * @return
     */
	public List<Contact> getChatContacts(Chat c){
		return chatcontact.get(c);
	}

	/**
	 * Sets the Contactlist of a Chat
 	 * @param c
	 * @param contacts
     */
	public void setChatContacts(Chat c, List<Contact> contacts){
		if(chatcontact.containsKey(c)) chatcontact.remove(c);
		chatcontact.put(c, contacts);
	}

	/**
	 * Removes a Contact from a Chat
	 * @param chat
	 * @param contact
     */
	public void removeChatContact(Chat chat, Contact contact){
		chatcontact.get(chat).remove(contact);
	}

	/**
	 * Adds a Contact to a Chat
	 * @param chat
	 * @param contact
     */
	public void addChatContact(Chat chat, Contact contact){
		chatcontact.get(chat).add(contact);
	}


	/**
	 * Returns a List of Chats sorted by the most recent message
	 * @param owner
	 * @return
     */

	public List<Chat> getChat_list(Profile owner) {
		List<Chat> swap_c_list = new LinkedList<>();
		for (Chat c : chat_list) {
			swap_c_list.add(c);
		}
		for (int i = swap_c_list.size() - 1; i >= 0; i--) {
			Chat c = swap_c_list.get(i);
			if (!c.getOwner().isEqual(owner)) swap_c_list.remove(c);
		}
		return (List<Chat>) sortList(swap_c_list);

	}


//Manage the Messages Lists

	/**
	 * Add Message to Database
	 * @param m
	 * @param c
     */
	public void addMessage(Message m, Chat c) {
		if (chatmessage.containsKey(c)) {
			chatmessage.get(c).add(m);
		} else {
			chatmessage.put(c, new LinkedList<Message>());
			chatmessage.get(c).add(m);
		}
	}

	/**
	 * Removes Message from the Database
	 * @param m
	 * @param c
     */
	public void removeMessage(Message m, Chat c) {
		chatmessage.get(c).remove(m);
	}

	/**
	 * Returns a sorted list fo Chat all Messages - sorted by Time
	 * @param c
	 * @return
     */
	public List<Message> getMessageList(Chat c) {
		return (List<Message>)sortList(chatmessage.get(c));
	}

	/**
	 * Returns a sorted list fo Chat Messages within the given intevall- sorted by Time
	 * @param c
	 * @return
	 */
	public List<Message> getMessageList(Chat c, int startIndex, int stopIndex) {
		List <Message> swapmessages = (List<Message>) sortList(chatmessage.get(c));
		swapmessages = (List<Message>) cutList(swapmessages, startIndex, stopIndex);
		return swapmessages;
	}

	/**
	 * Returns a sorted list fo Chat Messages within the given timerange- sorted by Time
	 * @param c
	 * @return
	 */
	public List<Message> getMessageList(Chat c, Timestamp start, Timestamp stop) {
		List <Message> swapmessages = (List<Message>) sortList(chatmessage.get(c));
		swapmessages = (List<Message>) cutList(swapmessages, start, stop);
		return swapmessages;
	}

	/**
	 * Returns a sorted list fo Chat Messages within the given timerange and intevall- sorted by Time
	 * @param c
	 * @return
	 */
	public List<Message> getMessageList(Chat c, int startIndex, int stopIndex, Timestamp start, Timestamp stop) {

		List <Message> swapmessages = (List<Message>) sortList(chatmessage.get(c));
		swapmessages = (List<Message>) cutList(swapmessages, start, stop);
		swapmessages = (List<Message>) cutList(swapmessages, startIndex, stopIndex);
		return swapmessages;
	}

	/**
	 * Returns a sorted list fo Chat Messages within the given intevall containing the searchstring- sorted by Time
	 * @param c
	 * @return
	 */
	public List<Message> getMessageList(Chat c, String search, int startIndex, int stopIndex) {
		List <Message> swapmessages = (List<Message>) sortList(chatmessage.get(c));
		swapmessages = (List<Message>) search(search, swapmessages);
		swapmessages = (List<Message>) cutList(swapmessages, startIndex, stopIndex);
		return swapmessages;
	}


	/**
	 * Method which validates if a ID for a Chat is unique
	 *
	 * @param id
	 * @return
	 */

	public boolean validChatID(int id) {
		for (Chat c : chat_list) {
			if (c.getID() == id) return false;
		}
		return true;
	}


//Manage the Blacklist

	/**
	 * Adds a Contact to a Blacklist of a Profile (Owner)
	 *
	 * @param c
	 * @param owner
	 */
	public void blacklistAdd(Contact c, Profile owner) {
		if (blacklist_map.containsKey(owner)) blacklist_map.get(owner).add(c);
		else {
			blacklist_map.put(owner, new LinkedList<Contact>());
			blacklist_map.get(owner).add(c);
		}
	}

	/**
	 * Removes a Contact from a Blacklist of a Profile (Owner)
	 *
	 * @param c
	 * @param owner
	 */
	public void blacklistRemove(Contact c, Profile owner) {
		if (blacklist_map.containsKey(owner) && blacklist_map.get(owner).contains(c)) {
			blacklist_map.get(owner).remove(c);
		}
	}

	/**
	 * Returns the Blacklist which is a List of contacts of a Profile (owner)
	 *
	 * @param owner
	 * @return
	 */
	public List<Contact> blacklistGet(Profile owner) {
		return blacklist_map.get(owner);
	}


//Methods to Manage list-stuff

	/**
	 * Sorts the Lists of Timeable Objects which are Objects with the Method getTimestamp (Comment, Feed, Message)
	 *
	 * @param sortlist
	 * @return
	 */
	private List<? extends Timeable> sortList(List<? extends Timeable> sortlist) {
		List<Timeable> m_list = new LinkedList<>();
		m_list.addAll(sortlist);
		Timeable temp, temp2;
		for (int i = 1; i < m_list.size(); i++) {
			for (int j = 0; j < m_list.size() - 1; j++) {
				if(m_list.get(j).getTimestamp() != null && m_list.get(j+1).getTimestamp()!=null){
					if (m_list.get(j).getTimestamp().before(m_list.get(j + 1).getTimestamp())) {
						temp = m_list.get(j);
						temp2 = m_list.get(j + 1);
						m_list.remove(j + 1);
						m_list.remove(j);
						m_list.add(j, temp2);
						m_list.add(j + 1, temp);
					}
				}
			}
		}
		return m_list;
	}

	/**
	 * Returns a List with object within the given timerange
	 * @param cutlist
	 * @param start
	 * @param stop
     * @return
     */

	private List<? extends Timeable> cutList(List<? extends Timeable> cutlist, Timestamp start, Timestamp stop) {

		List<Timeable> swaplist = new LinkedList<>();
		if (start.before(stop)) {
			swaplist.addAll(cutlist);
			boolean reachStart = false;
			boolean reachStop = false;
			int iteratorstart = 0;
			int iteratorstop = 0;
			int iterator = 0;
			while ((!reachStart && !reachStop) || iterator < swaplist.size()) {
				if (!reachStart && reachStop) {
					if (swaplist.get(iterator).getTimestamp().before(start) || swaplist.get(iterator).getTimestamp().equals(start)) {
						reachStart = true;
					} else iteratorstart = iterator;
				}
				if (!reachStop) {
					if (swaplist.get(iterator).getTimestamp().before(stop)) {
						reachStop = true;
					} else iteratorstop = iterator;
				}
				iterator++;
			}
			for(int i = swaplist.size()-1; i >= 0; i--){
				if(i<iteratorstop && reachStop == true) swaplist.remove(i);
				if(i>=iteratorstart && reachStart == true) swaplist.remove(i);
			}
		}
		return swaplist;
	}

	/**
	 * Returns the List with the Objects within the given Intervall
	 * @param cutlist
	 * @param startIndex
	 * @param stopIndex
     * @return
     */
	private List<? extends Timeable> cutList(List<? extends Timeable> cutlist, int startIndex, int stopIndex) {
		List<Timeable> swaplist = new LinkedList<>();
		if (startIndex <= stopIndex && startIndex >= 0 && stopIndex >= 0) {
			if (stopIndex >= cutlist.size()) {
				stopIndex = cutlist.size();
			}
			if(startIndex > cutlist.size()-1){
				return swaplist;
			}
			swaplist.addAll(cutlist);
			int iterator = stopIndex;

			while (iterator < swaplist.size()) {
				swaplist.remove(iterator);
			}

			iterator = 0;
			while (iterator < startIndex) {
				swaplist.remove(iterator);
				iterator++;
			}

		}
		return swaplist;

	}


	private List<? extends ContainsContent> search(String searchterm, List<? extends  ContainsContent> searchList){
		List<ContainsContent> swaplist = new LinkedList<>();
		swaplist.addAll(searchList);
		searchterm = searchterm.toLowerCase();
		for(int t = swaplist.size() -1; t >= 0; t--){
			ContainsContent c = swaplist.get(t);
			if (!c.getContent().getMessage().toLowerCase().contains(searchterm)){
				swaplist.remove(c);
			}
		}
		return swaplist;
	}


}

