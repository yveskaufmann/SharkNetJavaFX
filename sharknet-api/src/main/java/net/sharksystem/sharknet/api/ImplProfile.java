package net.sharksystem.sharknet.api;

import java.util.LinkedList;
import java.util.List;



/**
 * Created by timol on 16.05.2016.
 */
public class ImplProfile implements Profile {

	Contact c;
	List<Interest> interest_list = new LinkedList<>();
	String password;

	public ImplProfile(Contact c){
		this.c = c;
	}

	@Override
	public Contact getContact() {
		return c;
	}

	@Override
	public Setting getSettings() {
		return null;
	}

	@Override
	public List<Interest> getInterests() {
		//ToDo: Shark - search for interessts and fill list
		return interest_list;
	}

	@Override
	public void deleteProfile() {
		//ToDo: Shark - delete the Profile in the KB
	}

	@Override
	public void safeProfile() {
		//ToDo: Shark - safe or update Profile in the KB
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
	//ToDo: Implement - Generate KeyPairs
	//ToDo: Clearify - How Notifications for the GUI Work (Action Listener)

}
