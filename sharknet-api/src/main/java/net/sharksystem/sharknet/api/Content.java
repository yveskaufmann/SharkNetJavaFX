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

}
