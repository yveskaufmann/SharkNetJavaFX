package net.sharksystem.sharknet.api;

import java.util.List;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents the Feed-Functionality (Timeline)
 */
public interface Feed {
	/**
	 * Returns the name of the interest the feed references to
	 * @return
     */
    public String getInterest();

	/**
	 * Returns Date and Time when the feed was created
	 * @return
     */
    public String getTimestamp();
	/**
	 * returns the content of a Message
	 * @return
	 */
	//ToDo: Mime Type integrieren
    public String getContent();
	/**
	 * returns the Author of a Feed
	 * @return
	 */
    public String getSender();

	/**
	 * Returns a List of comments referencing the feed
	 * @param Anzahl
	 * @return
     */
    public List<Comment> getComments(int Anzahl);
	/**
	 * adds and safes a comment to a feed
	 * @param comment
	 */
	public void newComment(String comment);
}
