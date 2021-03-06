package net.sharksystem.sharknet.api;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by timol on 06.06.2016.
 */
public class ImplBlacklist implements Blacklist {

	Profile owner;

	public ImplBlacklist(Profile owner){
		this.owner = owner;
	}

	@Override
	public void add(Contact c) {
		//ToDo: Shark - add Contact to Blacklist and remove it from contactlist of the profile (?)
		DummyDB.getInstance().blacklistAdd(c, owner);

	}

	@Override
	public void remove(Contact c) {
		//ToDo: Shark - remove Contact from Blacklist
		DummyDB.getInstance().blacklistRemove(c, owner);

	}

	@Override
	public List<Contact> getList() {

		//ToDo: Shark - fill List with Contact from Blacklist
		List<Contact> blacklist_list = DummyDB.getInstance().blacklistGet(owner);
		if(blacklist_list == null){
			blacklist_list = new LinkedList<>();
		}

		return blacklist_list;
	}
}
