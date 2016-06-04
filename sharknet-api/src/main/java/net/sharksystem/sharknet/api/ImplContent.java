package net.sharksystem.sharknet.api;

import java.io.InputStream;

/**
 * Created by timol on 01.06.2016.
 */
public class ImplContent implements Content {
	String fileExtension, message;
	InputStream file;

	public ImplContent (String message){
		this.message = message;
		fileExtension = null;
		file = null;
	}

	public ImplContent(InputStream file, String fileExtension){
		this.file = file;
		this.fileExtension = fileExtension;
		this.message = null;
	}

	public ImplContent(InputStream file, String fileExtension, String message){
		this.file = file;
		this.fileExtension = fileExtension;
		this.message = message;
	}

	@Override
	public String getFileExtension() {
		return fileExtension;
	}

	@Override
	public InputStream getFile() {
		return file;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;

	}
}
