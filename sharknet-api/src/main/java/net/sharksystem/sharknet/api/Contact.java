package net.sharksystem.sharknet.api;

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

	/**
	 * returns the UID of the contact
	 * @return
     */
    public String getUID();

	/**
	 * Returns the profilepicture of a contact
	 * @return
     */
	//ToDo: MimeType
    public String getPicture();

	/**
	 * returns the PublicKey of the contact
	 * @return
     */
    public String getPublicKey();


}
