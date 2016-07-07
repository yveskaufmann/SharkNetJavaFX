package net.sharksystem.sharknet.api;

import net.sharkfw.knowledgeBase.Taxonomy;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents a Contact (a Person) in SharkNet
 */
public interface Contact {



	/**
	 * returns the Nickname of the contact
	 * @return
     */
    public String getNickname();
	public void setNickname(String nickname);


	/**
	 * returns the UID of the contact
	 * @return
     */
    public String getUID();
	public void setUID(String uid);

	/**
	 * Returns the profilepicture of a contact
	 * @return
     */

    public Content getPicture();
	public void setPicture(Content pic);

	/**
	 * returns the PublicKey of the contact
	 * @return
     */
    public String getPublicKey();
	public void setPublicKey(String publicKey);
	public Timestamp getPublicKeyExpiration();
	public void deleteKey();

	/**
	 * Deletes the Contact from the Database
	 */
	public void delete();

	/**
	 * updates a Contact in the Database
	 */
	public void update();


	/**
	 * returns a List of all Interests the profile is interested in
	 * @return
	 */
	public Interest getInterests();

	/**
	 * Method to evaluate is a Contact is equal to another
	 * @return
     */
	public boolean isEqual(Contact c);

	/**
	 * Method returns the Owner of the Contact
	 */
	public Profile getOwner();

	/**
	 * add a real name to the contact
	 * @param name
     */
	public void  addName(String name);

	/**
	 * Returns the real name of the contact
	 * @return
     */
	public String getName();

	/**
	 * spelling suggestion- addTelephoneNumber()
	 * add a Telephonnumber to the Contact. The Contact has a List which can include more phone numbers (no validation included)
	 * @param telephonnumber
     */
	public void addTelephonnumber(String telephonnumber);

	/**
	 * Returns the List of Phone Numbers
	 * @return
     */
	public List<String> getTelephonnumber();

	/**
	 * Adds/Or overwrites the Note to a contact
	 * @param note
     */
	public void addNote(String note);

	/**
	 * Returns Notes to a contact
	 * @return
     */
	public String getNote();

	/**
	 * sets the e-mail of a contact (no validation included)
	 * @param email
     */
	public void setEmail(String email);

	/**
	 * returns e-mail of the contact
	 * @return
     */
	public String getEmail();


}
