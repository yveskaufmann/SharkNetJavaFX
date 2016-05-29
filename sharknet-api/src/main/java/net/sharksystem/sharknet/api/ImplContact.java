package net.sharksystem.sharknet.api;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */

//ToDo: Clearify - Groups as own Interface/ImplClass
//ToDo: Implement - publicKey can be null
//ToDo: Implement - Contactpicture

public class ImplContact implements Contact {

	String nickname;
	String uid;
	String publickey;
	List<Interest> interest_list = new LinkedList<>();


	/**
	 * Constructor to add new Contact and Safe it to the Database
	 * @param nickname
	 * @param uid
	 * @param publickey
     */
	public ImplContact(String nickname, String uid, String publickey){
		this.nickname = nickname;
		this.uid = uid;
		this.publickey = publickey;
		//ToDo: Clearify - public key exchange

	}

	@Override
	public String getNickname() {
		return nickname;
	}

	@Override
	public void setNickname(String nickname) {
		this.nickname = nickname;

	}

	@Override
	public List<Interest> getInterests() {
		//ToDo: Shark - search for interessts and fill list
		return interest_list;
	}


	@Override
	public String getUID() {
		return uid;
	}

	@Override
	public void setUID(String uid) {
		this.uid = uid;

	}

	@Override
	public String getPicture() {

		//ToDo: Implement - Profilepictures
		return null;
	}

	@Override
	public void setPicture(String pic) {

	}

	@Override
	public String getPublicKey() {
		return publickey;
	}

	@Override
	public void setPublicKey(String publicKey) {
		this.publickey = publicKey;

		//ToDo: Implement Method: PublicKeyExchange for NFC exchange

	}

	@Override
	public void delete() {
		//ToDo: Shark - Delete Contact from the Database
		//Implementation of DummyDB
		DummyDB.getInstance().removeContact(this);
	}

	@Override
	public void update() {
		//ToDo: Shark - Update the Contact in the KB

	}

	@Override
	public void save(){
		//ToDo: Shark - Safe Contact in KB
		//Implementation of DummyDB
		DummyDB.getInstance().addContact(this);

	}
}
