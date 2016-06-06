package net.sharksystem.sharknet.api;

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
	public List<Interest> getInterests();

	/**
	 * Method to evaluate is a Contact is equal to another
	 * @return
     */
	public boolean isEqual(Contact c);

	/**
	 * Method returns the Owner of the Contact
	 */
	public Profile getOwner();

}
