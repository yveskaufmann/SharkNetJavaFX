package net.sharksystem.sharknet.api;

import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplProfile implements Profile {

	Contact c;
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
	public List<String> getInterests() {
		return null;
	}
	//ToDo: Implement - Interest
	//ToDo: Implement - Settings
}
