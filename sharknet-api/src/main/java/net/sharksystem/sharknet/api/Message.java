package net.sharksystem.sharknet.api;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by timol on 12.05.2016.
 * Interface for the Messages used in Chats
 */

public interface Message extends Timeable, ContainsContent {
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
    public List<Contact> getRecipients();

	/**
	 * returns the content of a Message
	 * @return
     */
    public Content getContent();


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

	/**
	 * marks the Message as is disliked. Shark will collect dislikes and after an special amount it will inform the author
	 * @param isDisliked
	 */
	public void setDisliked(boolean isDisliked);

	/**
	 * Returns if the Comment is isDisliked
	 */
	public boolean isdisliked();

	/**
	 * returns if the Message is sent by the user
	 * @return
     */
	public boolean isMine();

	/**
	 * Returns if the Message is verified (which means the signature is valid)
	 */
	public boolean isVerified();

	/**
	 * Sets if Message is verified
	 * @param verified
     */
	public void setVerified(boolean verified);

	/**
	 * Returns the Chat of the Message or constructs a new
	 * @return
     */
	public Chat getChat();

	/**
	 * Returns if the Message is already read
	 * @return
     */
	public boolean isRead();

	/**
	 * Sets boolean if message is read
	 * @param read
     */
	public void setRead(boolean read);


	/**
	 * Set the Status of the Message as Signed
	 * @param signed
     */
	public void setSigned(boolean signed);

	/**
	 * Set the Status of the  Message as Encrypted
	 * @param encrypted
     */
	public void setEncrypted(boolean encrypted);

	/**
	 * Returns if the Message was recived through direct contact with sender
	 * @return
     */
	public boolean isDierectRecived();

	/**
	 * Sets if Message was recived through direct contact with sender
	 * @param dierectRecived
     */
	public void setDierectRecived(boolean dierectRecived);
	}
