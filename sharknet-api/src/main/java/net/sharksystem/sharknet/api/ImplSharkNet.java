package net.sharksystem.sharknet.api;

import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplSharkNet implements SharkNet {
	@Override
	public List<Profile> getProfiles() {
		//ToDo: search in KB for Profiles an return a List of them
		return null;
	}

	@Override
	public List<Feed> getFeeds(int Anzahl) {
		//ToDo: Search in KB for Feeds and return a list of them
		return null;
	}

	@Override
	public List<Contact> getContacts() {
		//ToDo: Search in KB for Contacts and return a list of them
		return null;
	}

	@Override
	public List<Chat> getChats() {
		//ToDo: Search in KB vor Chats and return a list of them
		return null;
	}

	@Override
	public Feed newFeed(String content, String interest) {
		Feed f = new ImplFeed(content, interest);
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
	public Contact newContact(String nickname, String uid) {
		Contact c = new ImplContact(nickname, uid);
		return c;
	}
}
