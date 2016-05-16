package net.sharksystem.sharknet.api;

//ToDo: Messages for Groupchats

/**
 * Created by timol on 12.05.2016.
 * Interface for the Messages used in Chats
 */

public interface Message {
	/**
	 * Returns the Date, Time when a message was created
	 * @return
     */
    public String getTimestamp();

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
	//ToDo: Mime Type integrieren
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
