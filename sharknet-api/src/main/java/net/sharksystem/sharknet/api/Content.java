package net.sharksystem.sharknet.api;

import java.io.InputStream;

/**
 * Created by timol on 01.06.2016.
 */
public interface Content {
	public String getFileExtension();
	public InputStream getFile();
	public String getMessage();
	public void setMessage(String message);

}
