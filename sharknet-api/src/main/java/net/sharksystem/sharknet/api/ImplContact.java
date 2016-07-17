package net.sharksystem.sharknet.api;

import javafx.util.Pair;
import net.sharksystem.sharknet.api.utils.Resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */

public class ImplContact implements Contact, StudentContact {

	String nickname;
	String name;
	String email;
	String notes;
	String uid;
	Interest interest;
	List<String> telephonnumber_list = new LinkedList<>();
	Profile owner;
	Content picture;
	Timestamp lastWifiContact = null;
	String grade, classSpecification;


	String publickey;
	Timestamp keyExpiration;
	String publicKeyFingerPrint;


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
		this.interest = new ImplInterest(this);

		// Dummy public key generation
		DummyKeyPairHelper.createNewKeyForContact(this);

		setDefaultPicture();
		save();

	}

	/**
	 * Contructor for the Objects from the Database which are not going to be saved
	 */

	public ImplContact(String nickname, String uid, String publickey, Profile owner, Content pic, Interest interest){

		this.nickname = nickname;
		this.uid = uid;
		this.publickey = publickey;
		this.owner = owner;
		this.picture = pic;

		// Dummy public key generation
		DummyKeyPairHelper.createNewKeyForContact(this);

		if(interest == null) {
			this.interest = new ImplInterest(this);
		}else this.interest = interest;

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
	public Interest getInterests() {
		//ToDo: Shark - search for interessts and fill list
		return interest;
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
	public void addName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addTelephonnumber(String telephonnumber) {
		telephonnumber_list.add(telephonnumber);

	}

	@Override
	public List<String> getTelephonnumber() {
		return telephonnumber_list;
	}

	@Override
	public void addNote(String note) {
		this.notes = note;
	}

	@Override
	public String getNote() {
		return notes;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getEmail() {
		return email;
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
		return publickey;
	}

	@Override
	public void setPublicKey(String publicKey) {
		this.publickey = publicKey;

	}

	@Override
	public Timestamp getPublicKeyExpiration() {
		return keyExpiration;
		//ToDo: Shark - get Expiration of Key
	}

	@Override
	public String getPublicKeyFingerprint() {
		return publicKeyFingerPrint;
	}

	@Override
	public void deleteKey() {
		this.publickey = null;
		//ToDo: Shark - delete Key
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

	@Override
	public Timestamp getLastWifiContact() {
		return lastWifiContact;
	}

	@Override
	public void setLastWifiContact(Timestamp lastWifiContact) {
		this.lastWifiContact = lastWifiContact;
	}

	private void setDefaultPicture(){
		File personpic= Resources.get("person.png");
		Content piccon = new ImplContent(owner);
		piccon.setFile(personpic);
		try {
			String mimeType = Files.probeContentType(personpic.toPath());
			piccon.setMimeType(mimeType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setPicture(piccon);
	}

	@Override
	public String getGrade() {
		return grade;
	}

	@Override
	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Override
	public String getClassSpecification() {
		return classSpecification;
	}

	@Override
	public void setClassSpecification(String classSpecification) {
		this.classSpecification = classSpecification;
	}


}
