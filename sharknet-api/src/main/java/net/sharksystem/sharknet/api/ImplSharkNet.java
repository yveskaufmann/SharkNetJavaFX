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
		return profile_list;
	}

	@Override
	public List<Feed> getFeeds(int Anzahl) {

		//ToDo: Shark - Search in KB for Feeds and return a list of them
		return feed_list;
	}

	@Override
	public List<Contact> getContacts() {

		//ToDo: Shark - Search in KB for Contacts and return a list of them
		return contact_list;
	}

	@Override
	public List<Chat> getChats() {

		//ToDo: Shark - Search in KB vor Chats and return a list of them
		return chat_list;
	}

	@Override
	public Feed newFeed(String content, Interest interest, String sender) {
		Feed f = new ImplFeed(content, interest, sender);
		return f;
	}

	@Override
	public Profile newProfile(Contact c) {
		Profile p = new ImplProfile(c);
		return p;
	}

	@Override
	public Chat newChat(Contact c) {
		Chat chat = new ImplChat(c);
		return chat;
	}

	@Override
	public Contact newContact(String nickname, String uid, String publickey) {
		Contact c = new ImplContact(nickname, uid, publickey);
		return c;

		//ToDo: Clearify - how to share contacts
	}
}
