package net.sharksystem.sharknet.api;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */

public class ImplContact implements Contact {

	//ToDo: Implement - Generate KeyPairs



	String nickname;
	String uid;
	String publickey;
	List<Interest> interest_list = new LinkedList<>();
	Profile owner;
	Content picture;


	/**
	 * Constructor to add new Contact and Safe it to the Database
	 * @param nickname
	 * @param uid
	 * @param publickey
     */
	public ImplContact(String nickname, String uid, String publickey, Profile owner){
		this.nickname = nickname;
		this.uid = uid;
		this.publickey = publickey;
		this.owner = owner;
		save();

	}

	/**
	 * Contructor for the Objects from the Database which are not going to be saved
	 */

	public ImplContact(String nickname, String uid, String publickey, Profile owner, Content pic, List<Interest> interest_list){

		this.nickname = nickname;
		this.uid = uid;
		this.publickey = publickey;
		this.interest_list = interest_list;
		this.owner = owner;
		this.picture = pic;
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
	public boolean isEqual(Contact c) {

		if(c.getNickname().equals(nickname) && c.getUID().equals(uid) && c.getPublicKey().equals(publickey)){
			return true;
		}
		else return false;
	}

	@Override
	public Profile getOwner() {
		return owner;
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
	public Content getPicture() {
		return picture;
	}

	@Override
	public void setPicture(Content pic) {
		this.picture = pic;
	}

	@Override
	public String getPublicKey() {
		//ToDo: Public key getter nur fingerprint bzw readable

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

	/**
	 * Save the Contact to the Database
	 */

	private void save(){
		//ToDo: Shark - Safe Contact in KB
		//Implementation of DummyDB
		DummyDB.getInstance().addContact(this);

	}

	/**
	 * This Method is just used to make the Contact for a Profile
	 * @param p
     */
	public void setOwner(Profile p){
		this.owner = p;
	}


}
