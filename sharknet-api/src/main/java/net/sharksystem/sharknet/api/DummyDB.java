package net.sharksystem.sharknet.api;

import java.util.*;

/**
 * Created by timol on 28.05.2016.
 */
public class DummyDB {
	List<Feed> feed_list = new LinkedList<>();
	List<Profile> profile_list = new LinkedList<>();
	List<Contact> contact_list = new LinkedList<>();
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
	public List<Feed> getFeed_list() {return feed_list;}
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
	public List<Contact> getContact_list() {return contact_list;}
	public void setContact_list(List<Contact> contact_list) {this.contact_list = contact_list;}
	public void addContact(Contact c){contact_list.add(c);}
	public void removeContact(Contact c){contact_list.remove(c);}


//Manage the Chat Lists
	public void addChat(Chat c){
		chat_list.add(c);
		chatmessage.put(c, new LinkedList<Message>());
	}
	public void removeChat(Chat c){
		chat_list.remove(c);
		chatmessage.remove(c);
	}
	public List<Chat> getChat_list() {return chat_list;}
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


}
