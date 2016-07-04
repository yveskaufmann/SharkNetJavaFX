package net.sharksystem.sharknet.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplSharkNet implements SharkNet {

	Profile myProfile;
	HashMap<Profile, List<GetEvents>> ListenerMap = new HashMap<>();
	@Override
	public List<Profile> getProfiles() {

		//ToDo: Shark - search in KB for Profiles an return a List of them
		//Implementation of DummyDB
		List<Profile> profile_list = DummyDB.getInstance().getProfile_list();
		return profile_list;
	}

	@Override
	public List<Feed> getFeeds(boolean descending) {
		//ToDo: Shark - Search in KB for Feeds and return a list of them - sorted by Time

		if(myProfile == null) return null;

		//Implementation of DummyDB
		List<Feed> feed_list = DummyDB.getInstance().getFeed_list(myProfile, descending);
		return feed_list;
	}

	@Override
	public List<Feed> getFeeds(int start_index, int stop_index, boolean descending) {
		//ToDo: Shark - Search in KB for Feeds and return a list of them within the given intervall - sorted by time

		//Implementation of DummyDB
		if(myProfile == null) return null;
		List<Feed> feed_list = DummyDB.getInstance().getFeed_list(myProfile, start_index, stop_index, descending);
		return feed_list;
	}

	@Override
	public List<Feed> getFeeds(Interest i, int start_index, int stop_index, boolean descending) {

		//ToDo: Implement - return feeds with interest i from start to stop, sorted by time
		if(myProfile == null) return null;
		List<Feed> feed_list = DummyDB.getInstance().getFeed_list(myProfile, i, start_index, stop_index, descending);
		return feed_list;
	}

	@Override
	public List<Feed> getFeeds(String search, int start_index, int stop_index, boolean descending) {
		//ToDo: Shark - Search in KB for Feeds and return a list of them within the given intervall and containing the search string - sorted by time
		//Implementation of DummyDB
		if(myProfile == null) return null;
		List<Feed> feed_list = DummyDB.getInstance().getFeed_list(myProfile,search, start_index, stop_index, descending);
		return feed_list;

	}

	@Override
	public List<Feed> getFeeds(Timestamp start, Timestamp end, int start_index, int stop_index, boolean descending) {

		//ToDo: Shark - Search in KB for Feeds and return a list of them within the given intervall and timerange - sorted by time

		//Implementation of DummyDB
		if(myProfile == null) return null;
		List<Feed> feed_list = DummyDB.getInstance().getFeed_list(myProfile, start_index, stop_index, start, end, descending);
		return feed_list;
	}

	@Override
	public List<Contact> getContacts() {

		if(myProfile == null) return null;
		//ToDo: Shark - Search in KB for Contacts and return a list of them
		//Implementation of DummyDB
		List<Contact> contact_list = DummyDB.getInstance().getContact_list(myProfile);
		contact_list.remove(myProfile.getContact());
		contact_list.removeAll(myProfile.getBlacklist().getList());
		return contact_list;
	}

	@Override
	public List<Chat> getChats() {

		if(myProfile == null) return null;
		//ToDo: Shark - Search in KB vor Chats and return a list of them
		//Implementation of DummyDB
		List<Chat> chat_list = DummyDB.getInstance().getChat_list(myProfile);
		return chat_list;
	}

	@Override
	public Feed newFeed(Content content, Interest interest, Contact sender) {
		if(myProfile == null) return null;
		Feed f = new ImplFeed(content, interest, sender, myProfile);
		return f;
	}

	@Override
	public Profile newProfile(String nickname, String deviceID) {
		Profile p = new ImplProfile(nickname, deviceID);
		ImplContact c = (ImplContact)p.getContact();
		c.setOwner(p);
		return p;
	}

	@Override
	public Chat newChat(List<Contact> recipients) {
		if(myProfile == null) return null;

		//ToDo: Shark - Lookup if a chat for the contacts already exists if yes return the chatobject of the existing chat, if no make a new chat
		Chat chat = DummyDB.getInstance().existChat(recipients);
		if(chat == null) {
			chat = new ImplChat(recipients, myProfile);
		}
		return chat;
	}

	@Override
	public Contact newContact(String nickname, String uid, String publickey) {
		if(myProfile == null) return null;
		Contact c = new ImplContact(nickname, uid, publickey, myProfile);
		return c;

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
	public void exchangeContactNFC() {
		//ToDo: Shark - Implement Contact Exchange via NFC
	}

	@Override
	public void addListener(Profile p, GetEvents listener) {
		if(!ListenerMap.containsKey(p)){
			List<GetEvents> listenerList = new LinkedList<>();
			listenerList.add(listener);
			ListenerMap.put(p, listenerList);
		}
		else{
			if(!ListenerMap.get(p).contains(listener)){
				ListenerMap.get(p).add(listener);
			}
		}
	}

	public void informMessage(Message m){
		List<Profile> pList = getProfiles();
		for(Contact c : m.getRecipients()){
			for(Profile p : pList){
				if(p.getContact().isEqual(c)){
					List<GetEvents> listenerList = ListenerMap.get(p);
					if(listenerList != null) {
						for (GetEvents ev : listenerList) {
							ev.receivedMessage(m);
						}
					}
				}
			}
		}
	}

	public void informFeed(Feed f){
		Iterator it = ListenerMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			List<GetEvents> listener = (List<GetEvents>)pair.getValue();
			for(GetEvents ev : listener){
				ev.receivedFeed(f);
			}
		}
	}


	public void informComment(Comment c){
		Iterator it = ListenerMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			List<GetEvents> listener = (List<GetEvents>)pair.getValue();
			for(GetEvents ev : listener){
				ev.receivedComment(c);
			}
		}
	}


	public void fillWithDummyData(){
		Dummy d = new Dummy();
		d.fillWithDummyData(this);
	}

}
