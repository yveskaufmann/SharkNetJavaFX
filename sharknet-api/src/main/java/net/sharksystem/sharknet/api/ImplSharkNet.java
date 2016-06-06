package net.sharksystem.sharknet.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplSharkNet implements SharkNet {


	//ToDo: Implement - Notifications for the GUI (Action Listener)

	//ToDo: Implement - Initialisierung bauen (inkl übergabe KB etc)

	List<Profile> profile_list = new LinkedList<>();
	List<Contact> contact_list = new LinkedList<>();
	List<Chat> chat_list = new LinkedList<>();
	Profile myProfile;
	ArrayList<Dummy> chatListenerList = new ArrayList<Dummy>();

	@Override
	public List<Profile> getProfiles() {

		//ToDo: Shark - search in KB for Profiles an return a List of them
		//Implementation of DummyDB
		profile_list = DummyDB.getInstance().getProfile_list();
		return profile_list;
	}

	@Override
	public List<Feed> getFeeds() {
		//ToDo: Shark - Search in KB for Feeds and return a list of them - sorted by Time

		if(myProfile == null) return null;

		//Implementation of DummyDB
		List<Feed> feed_list = DummyDB.getInstance().getFeed_list(myProfile);
		return feed_list;
	}

	@Override
	public List<Feed> getFeeds(int start_index, int stop_index) {
		//ToDo: Shark - Search in KB for Feeds and return a list of them within the given intervall - sorted by time

		//Implementation of DummyDB
		if(myProfile == null) return null;
		List<Feed> feed_list = DummyDB.getInstance().getFeed_list(myProfile, start_index, stop_index);
		return feed_list;
	}

	@Override
	public List<Feed> getFeeds(Interest i, int start_index, int stop_index) {
		//ToDo: Implement - return feeds with interest i from start to stop, sorted by time
		return null;
	}

	@Override
	public List<Feed> getFeeds(String search, int start_index, int stop_index) {
		//ToDo: Shark - Search in KB for Feeds and return a list of them within the given intervall and containing the search string - sorted by time
		//Implementation of DummyDB
		if(myProfile == null) return null;
		List<Feed> feed_list = DummyDB.getInstance().getFeed_list(myProfile,search, start_index, stop_index);
		return feed_list;

	}

	@Override
	public List<Feed> getFeeds(Timestamp start, Timestamp end, int start_index, int stop_index) {

		//ToDo: Shark - Search in KB for Feeds and return a list of them within the given intervall and timerange - sorted by time

		//Implementation of DummyDB
		if(myProfile == null) return null;
		List<Feed> feed_list = DummyDB.getInstance().getFeed_list(myProfile, start_index, stop_index, start, end);
		return feed_list;
	}

	@Override
	public List<Contact> getContacts() {

		if(myProfile == null) return null;
		//ToDo: Shark - Search in KB for Contacts and return a list of them
		//Implementation of DummyDB
		contact_list = DummyDB.getInstance().getContact_list(myProfile);
		return contact_list;
	}

	@Override
	public List<Chat> getChats() {

		if(myProfile == null) return null;
		//ToDo: Shark - Search in KB vor Chats and return a list of them
		//Implementation of DummyDB
		chat_list = DummyDB.getInstance().getChat_list(myProfile);
		return chat_list;
	}

	@Override
	public Feed newFeed(Content content, Interest interest, Contact sender) {
		if(myProfile == null) return null;
		Feed f = new ImplFeed(content, interest, sender, myProfile);
		return f;
	}

	@Override
	public Profile newProfile(String nickname, String uid, String publickey) {
		Profile p = new ImplProfile(new ImplContact(nickname, uid, publickey, null));
		ImplContact c = (ImplContact)p.getContact();
		c.setOwner(p);
		profile_list.add(p);
		return p;
	}

	@Override
	public Chat newChat(List<Contact> recipients) {
		if(myProfile == null) return null;
		Chat chat = new ImplChat(recipients, myProfile);
		chat_list.add(chat);
		return chat;
	}

	@Override
	public Contact newContact(String nickname, String uid, String publickey) {
		if(myProfile == null) return null;
		Contact c = new ImplContact(nickname, uid, publickey, myProfile);
		contact_list.add(c);
		return c;

		//ToDo: Clearify - how to share contacts
	}

	@Override
	public boolean setProfile(Profile myProfile, String password) {
		if(myProfile.login(password)){
			this.myProfile = myProfile;
			return true;
		}
		else return false;
	}

	@Override
	public Profile getMyProfile() {
		return myProfile;
	}

	@Override
	public void addChatListener(Profile p, Dummy listener) {
		if (!chatListenerList.contains(listener)) {
			chatListenerList.add(listener);
		}
	}

	@Override
	public void addFeedListener(Profile p, Dummy listener) {

	}

	@Override
	public void informNewMessage(Profile p, Message m) {

	}

	@Override
	public void informNewFeed(Profile p, Feed f) {

	}

	public void fillWithDummyData(){
		Dummy d = new Dummy();
		d.fillWithDummyData(this);
	}

}
