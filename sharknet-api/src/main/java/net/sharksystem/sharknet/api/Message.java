package net.sharksystem.sharknet.api;

//ToDo: Clearify - Messages for Groupchats how to Implement

import java.sql.Timestamp;

/**
 * Created by timol on 12.05.2016.
 * Interface for the Messages used in Chats
 */

public interface Message {
	/**
	 * Returns the Date, Time when a message was created
	 * @return
     */
    public Timestamp getTimestamp();

	/**
	 * returns the Author of a message
	 * @return
     */
    public Contact getSender();
	/**
	 *returns the recipient of a message
	 */
    public Contact getRecipient();

	/**
	 * returns the content of a Message
	 * @return
     */
	//ToDo: Implement - File - Mime Type integrieren
    public String getContent();

	/**
	 * valuates if a Message was signed
	 * @return
     */
    public boolean isSigned();

	/**
	 * valuates if a message was encrypted
	 * @return
     */
    public boolean isEncrypted();

	/**
	 * deletes the message from a chat
	 */
	public void deleteMessage();
}
