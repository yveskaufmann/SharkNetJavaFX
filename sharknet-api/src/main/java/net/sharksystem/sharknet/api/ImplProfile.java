package net.sharksystem.sharknet.api;

import java.util.LinkedList;
import java.util.List;



/**
 * Created by timol on 16.05.2016.
 */
public class ImplProfile implements Profile {

	Contact c;
	String password ="";
	Setting setting;
	Blacklist blacklist;

	/**
	 * Constructor for new Profiles which are going to be saved
	 * @param c
     */
	public ImplProfile(Contact c){
		this.c = c;
	}

	public ImplProfile(Contact c, String password, Setting setting){
		this.c = c;
		this.password = password;
		this.setting = setting;
	}


	@Override
	public Contact getContact() {
		return c;
	}

	@Override
	public void setContact(Contact c) {
		this.c = c;
	}

	@Override
	public Setting getSettings() {
		return null;
	}

	@Override
	public void setSettings() {

	}

	@Override
	public void delete() {

		//ToDo: Shark - delete the Profile in the KB
		//Implementation of DummyDB
		DummyDB.getInstance().removeProfile(this);
	}

	@Override
	public void save() {

		//ToDo: Shark - saveProfile in the KB
		//Implementation of DummyDB
		DummyDB.getInstance().addProfile(this);
	}

	@Override
	public void update() {
		//ToDo: Shark - update Profile in the KB
	}

	@Override
	public boolean login(String password) {
		if(this.password.equals(password)){
			return true;
		}
		else return false;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	//ToDo: Implement - Interest
	//ToDo: Implement - Settings

	@Override
	public boolean isEqual(Profile p){
		if(p.getContact().isEqual(c)) return true;
		else return false;
	}

	@Override
	public Blacklist getBlacklist() {
		if(blacklist == null){
			blacklist = new ImplBlacklist(this);
		}
		return blacklist;
	}

}
