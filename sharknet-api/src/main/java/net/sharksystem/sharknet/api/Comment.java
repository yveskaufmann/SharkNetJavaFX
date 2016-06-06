package net.sharksystem.sharknet.api;

import java.sql.Timestamp;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents the comments belonging to feeds
 */
public interface Comment extends Timeable, ContainsContent {

	/**
	 * Returns the author of a comment
	 * @return
     */
    public Contact getSender();

	/**
	 * Returns the Date and Time a comment was created
	 * @return
     */
    public Timestamp getTimestamp();

	/**
	 * Returns the Feed the comment is referencing
	 * @return
     */
    public Feed getRefFeed();
	/**
	 * returns the content of a comment
	 * @return
	 */

    public Content getContent();

	/**
	 * Deletes Comment from DB
	 */
	public void delete();

	/**
	 * marks the Comment as disliked. Shark will collect dislikes and after an special amount it will inform the author
	 */
	public void dislike();

	/**
	 * Returns if the Comment is disliked
	 */
	public boolean isdisliked();

}

