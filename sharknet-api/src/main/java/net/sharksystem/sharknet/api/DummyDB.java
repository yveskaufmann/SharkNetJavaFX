package net.sharksystem.sharknet.api;

import java.util.*;

/**
 * Created by timol on 28.05.2016.
 */
public class DummyDB {
	List<Feed> feed_list = new LinkedList<>();
	List<Profile> profile_list = new LinkedList<>();
	List<Contact> contact_list = new LinkedList<>();
	HashMap<Profile, List<Contact>> blacklist_map = new HashMap<>();
	List<Chat> chat_list = new LinkedList<>();
	HashMap<Chat, List<Message>> chatmessage = new HashMap<>();
	HashMap<Feed, List<Comment>> feedcomment = new HashMap<>();



	private DummyDB () {}

	private static DummyDB instance;

	public static DummyDB getInstance () {
		if (DummyDB.instance == null) {
			DummyDB.instance = new DummyDB ();
		}
		return DummyDB.instance;
	}


	//Feed Lists and Values
	public List<Feed> getFeed_list(Profile owner) {

		List<Feed> swap_f_list = new LinkedList<>();
		for(Feed f : feed_list){
			swap_f_list.add(f);
		}

		for(int i = swap_f_list.size()-1; i>=0; i--){
			Feed f = swap_f_list.get(i);
			if(!f.getOwner().isEqual(owner))swap_f_list.remove(f);
		}
		return swap_f_list;
	}



	public void setFeed_list(List<Feed> feed_list) {this.feed_list = feed_list;}

	public void addfeed(Feed f){
		feed_list.add(f);
		feedcomment.put(f, new LinkedList<Comment>());
	}
	public void removefeed(Feed f){
		feed_list.remove(f);
		feedcomment.remove(f);
	}

	//Comments Lists and Values
	public void addComment(Comment c, Feed f){
		if (feedcomment.containsKey(f)){
			feedcomment.get(f).add(c);
		}
		else {
			feedcomment.put(f, new LinkedList<Comment>());
			feedcomment.get(f).add(c);
		}
	}
	public void removeComment(Comment c, Feed f){
		feedcomment.get(f).remove(c);
	}

	//Manage Profile Suff
	public void  addProfile(Profile p){profile_list.add(p);}
	public void removeProfile(Profile p){profile_list.remove(p);}
	public List<Profile> getProfile_list() {return profile_list;}
	public void setProfile_list(List<Profile> profile_list) {this.profile_list = profile_list;}

	//Manage Contacts
	public List<Contact> getContact_list(Profile owner) {


		List<Contact> swap_c_list = new LinkedList<>();
		for(Contact c : contact_list){
			swap_c_list.add(c);
		}

		for(int i = swap_c_list.size()-1; i>=0; i--){
			Contact c = swap_c_list.get(i);
			if(!c.getOwner().isEqual(owner))swap_c_list.remove(c);
		}
		return swap_c_list;


	}
	public void setContact_list(List<Contact> contact_list) {this.contact_list = contact_list;}
	public void addContact(Contact c){contact_list.add(c);}
	public void removeContact(Contact c){contact_list.remove(c);}


//Manage the Chat Lists
	public void addChat(Chat c){
		if(!chat_list.contains(c)){
			chat_list.add(c);
			chatmessage.put(c, new LinkedList<Message>());
		}
	}
	public void removeChat(Chat c){
		chat_list.remove(c);
		chatmessage.remove(c);
	}
	public List<Chat> getChat_list(Profile owner) {


		List<Chat> swap_c_list = new LinkedList<>();
		for(Chat c : chat_list){
			swap_c_list.add(c);
		}

		for(int i = swap_c_list.size()-1; i>=0; i--){
			Chat c = swap_c_list.get(i);
			if(!c.getOwner().isEqual(owner))swap_c_list.remove(c);
		}
		return swap_c_list;

	}
	public void setChat_list(List<Chat> chat_list) {this.chat_list = chat_list;}


//Manage the Messages Lists

	public void addMessage(Message m, Chat c ){
		if(chatmessage.containsKey(c)){
				chatmessage.get(c).add(m);
		}
		else {
			chatmessage.put(c, new LinkedList<Message>());
			chatmessage.get(c).add(m);
		}
	}
	public void removeMessage(Message m, Chat c){
			chatmessage.get(c).remove(m);
	}

	public List<Message> getMessageList(Chat c){
		return chatmessage.get(c);
	}

	public boolean validChatID(int id){
		for(Chat c : chat_list){
			if ( c.getID()== id) return false;
		}
		return true;
	}

	public void blacklistAdd(Contact c, Profile owner){
		if(blacklist_map.containsKey(owner)) blacklist_map.get(owner).add(c);
		else{
			blacklist_map.put(owner, new LinkedList<Contact>());
			blacklist_map.get(owner).add(c);
		}
	}

	public void blacklistRemove(Contact c, Profile owner){
		if(blacklist_map.containsKey(owner) && blacklist_map.get(owner).contains(c)){
			blacklist_map.get(owner).remove(c);
		}
	}
	public List<Contact> blacklistGet(Profile owner){
		return blacklist_map.get(owner);
	}
}
