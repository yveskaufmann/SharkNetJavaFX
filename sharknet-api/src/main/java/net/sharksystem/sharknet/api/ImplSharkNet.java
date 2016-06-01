package net.sharksystem.sharknet.api;

import java.security.PublicKey;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplSharkNet implements SharkNet {

	List<Feed> feed_list = new LinkedList<>();
	List<Profile> profile_list = new LinkedList<>();
	List<Contact> contact_list = new LinkedList<>();
	List<Chat> chat_list = new LinkedList<>();

	@Override
	public List<Profile> getProfiles() {

		//ToDo: Shark - search in KB for Profiles an return a List of them
		//Implementation of DummyDB
		profile_list = DummyDB.getInstance().getProfile_list();
		return profile_list;
	}

	@Override
	public List<Feed> getFeeds(int Anzahl) {

		//ToDo: Shark - Search in KB for Feeds and return a list of them
		//Implementation of DummyDB
		feed_list = DummyDB.getInstance().getFeed_list();
		return feed_list;
	}

	@Override
	public List<Contact> getContacts() {

		//ToDo: Shark - Search in KB for Contacts and return a list of them
		//Implementation of DummyDB
		contact_list = DummyDB.getInstance().getContact_list();
		return contact_list;
	}

	@Override
	public List<Chat> getChats() {

		//ToDo: Shark - Search in KB vor Chats and return a list of them
		//Implementation of DummyDB
		chat_list = DummyDB.getInstance().getChat_list();
		return chat_list;
	}

	@Override
	public Feed newFeed(String content, Interest interest, Contact sender) {
		Feed f = new ImplFeed(content, interest, sender);
		feed_list.add(f);
		return f;
	}

	@Override
	public Profile newProfile(Contact c) {
		Profile p = new ImplProfile(c);
		profile_list.add(p);
		return p;
	}

	@Override
	public Chat newChat(List<Contact> recipients, Contact sender) {
		Chat chat = new ImplChat(recipients, sender);
		chat_list.add(chat);
		return chat;
	}

	@Override
	public Contact newContact(String nickname, String uid, String publickey) {
		Contact c = new ImplContact(nickname, uid, publickey);
		contact_list.add(c);
		return c;

		//ToDo: Clearify - how to share contacts
	}

	public void fillWithDummyData(){
		Dummy d = new Dummy();
		d.fillWithDummyData(this);
	}
	public void updateListwithDummyData(List<Feed> feed_list, List<Profile> profile_list, List<Contact> contact_list, List<Chat> chat_list){
		this.feed_list = feed_list;
		this.profile_list = profile_list;
		this.contact_list = contact_list;
		this.chat_list = chat_list;
	}


}
