package net.sharksystem.sharknet.api;

import net.sharkfw.knowledgeBase.SemanticTag;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents the Feed-Functionality (Timeline)
 */
public interface Feed extends Timeable, ContainsContent{
	/**
	 * Returns the name of the interest the feed references to
	 * @return
     */
    public Interest getInterest();

	/**
	 * Returns Date and Time when the feed was created
	 * @return
     */
    public Timestamp getTimestamp();
	/**
	 * returns the content of a Message
	 * @return
	 */

    public Content getContent();
	/**
	 * returns the Author of a Feed
	 * @return
	 */
    public Contact getSender();

	/**
	 * Returns a List of comments referencing the feed
	 * @return
     */
    public List<Comment> getComments(boolean descending);
	public List<Comment> getComments(int startIndex, int stopIndex, boolean descending);
	public List<Comment> getComments(Timestamp start, Timestamp stop, boolean descending);
	public List<Comment> getComments(Timestamp start, Timestamp stop, int startIndex, int stopIndex, boolean descending);
	public List<Comment> getComments(String search, int startIndex, int stopIndex, boolean descending);


	/**
	 * adds and safes a comment to a feed
	 * @param comment
	 */
	public void newComment(Content comment, Contact author);

	/**
	 * Deletes the Feed from the Database
	 */
	public void delete();

	/**
	 * marks the Feed as disliked. Shark will collect dislikes and after an special amount it will inform the author
	 */
	public void dislike();

	/**
	 * Returns if the Comment is disliked
	 */
	public boolean isdisliked();

	/**
	 * Returns the Owner of a Feed
	 * @return
     */
	public Profile getOwner();


}
