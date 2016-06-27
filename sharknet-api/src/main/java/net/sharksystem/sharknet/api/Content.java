package net.sharksystem.sharknet.api;

import java.io.InputStream;

/**
 * Created by timol on 01.06.2016.
 */
public interface Content {

	/**
	 * Returns the Fileextension as a String
	 * @return
     */
	public String getFileExtension();

	/**
	 *Returns the File as IO Stream
	 */
	public InputStream getFile();

	/**
	 * Returns the Message as String
	 * @return
     */
	public String getMessage();

	/**
	 * Returns Filename as String
	 * @return
     */
	public String getFileName();

	/**
	 * Setter for the Message
	 */
	public void setMessage(String message);

	/**
	 * Adds a Voting to the Content if not already done and returns the Object
	 * @param question
	 * @param singleqoice
     * @return
     */
	public ImplVoting addVoting(String question, boolean singleqoice);

	/**
	 * Returns the Voting, if it`s not there returns null
	 * @return
     */
	public ImplVoting getVoting();

}
