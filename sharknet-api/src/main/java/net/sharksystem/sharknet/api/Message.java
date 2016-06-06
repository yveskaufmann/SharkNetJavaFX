package net.sharksystem.sharknet.api;

import java.sql.Time;
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
	 * marks the Message as disliked. Shark will collect dislikes and after an special amount it will inform the author
	 */
	public void dislike();

	/**
	 * Returns if the Comment is disliked
	 */
	public boolean isdisliked();

	/**
	 * returns if the Message is sent by the user
	 * @return
     */
	public boolean isMine();

}
