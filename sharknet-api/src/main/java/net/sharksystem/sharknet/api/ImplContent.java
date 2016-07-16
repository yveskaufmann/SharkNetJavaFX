package net.sharksystem.sharknet.api;

import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoInformation;
import java.io.*;

/**
 * Created by timol on 01.06.2016.
 */
public class ImplContent implements Content {

	Profile owner;
	String message;

	ImplVoting voting;
	InMemoInformation sharkFile;
	Reminder reminder;



	/**
	 * Konstruktor for the Class
	 * @param message
	 * @param owner
     */
	public ImplContent (String message, Profile owner){
		this.owner = owner;
		this.message = message;
	}

	public ImplContent(InMemoInformation file, Profile owner){
		this.owner = owner;
		this.sharkFile = file;
	}

	public ImplContent(Profile owner){
		this.owner = owner;
	}

	@Override
	public boolean setInputstream(InputStream is){
		int isSize = 0;
		try {
				isSize = getLengthOfIS(is);
				if(isSize <= (owner.getSettings().getMaxFileSize()*1024 *1024)){
					sharkFile = new InMemoInformation();
					sharkFile.setContent(is, getLengthOfIS(is));
					return true;
				}else return false;
			} catch (IOException e) {
				return false;
			}

	}

	@Override
	public boolean setFile(File f){
		FileInputStream fileInputStream=null;
		byte[] bFile = new byte[(int) f.length()];
		try {
			fileInputStream = new FileInputStream(f);
			fileInputStream.read(bFile);
			fileInputStream.close();

		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		if(bFile.length <= (owner.getSettings().getMaxFileSize()*1024 *1024)){
			sharkFile = new InMemoInformation(bFile);
			setFilename(f.getName());
			return true;
		}
		else return false;
	}

	@Override
	public OutputStream getOutputstream(){
		try {
			return sharkFile.getOutputStream();
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getMimeType(){
		return sharkFile.getContentType();
	}

	@Override
	public void setMimeType(String mimeType){
		if(sharkFile == null){
			sharkFile = new InMemoInformation();
		}
		sharkFile.setContentType(mimeType);
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getFileName(){
		if(sharkFile == null) sharkFile = new InMemoInformation();
		return  sharkFile.getName();
	}

	@Override
	public void setFilename(String filename){

		if(sharkFile == null) sharkFile = new InMemoInformation();
		try {
			sharkFile.setName(filename);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ImplVoting addVoting(String question, boolean singleqoice){
		if(voting == null){
			voting = new ImplVoting(question, singleqoice);
		}
		return voting;
	}

	@Override
	public ImplVoting getVoting(){
		if(voting == null) return null;
		else return voting;
	}

	@Override
	public Reminder getReminder() {
		return reminder;
	}

	@Override
	public void setReminder(Reminder reminder) {
		this.reminder = reminder;
	}

	@Override
	public InMemoInformation getInformationFile(){
		return sharkFile;
	}
	@Override
	public void setInformationFile(InMemoInformation file) {
		sharkFile = file;
	}

	@Override
	public InputStream getInputstream() {

		/*
		   JarInputStream are don't supports marks in this
		   case we need another solution.
		 */
		/*
		if (! file.markSupported()) {
			return new ResetOnCloseInputStream(new BufferedInputStream(file));
		}
		return new ResetOnCloseInputStream(file);
		*/
		try {
			return sharkFile.getInputStream();
		} catch (SharkKBException e) {
			return null;
		}

	}

	/**
	 * Returns the Lenght of an Inputstream by copying it to a byte array
	 * @param is
	 * @return
	 * @throws IOException
     */
	private int getLengthOfIS(InputStream is) throws IOException {

		int len;
		int size = 1024;
		byte[] buf;

		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1)
				bos.write(buf, 0, len);
			buf = bos.toByteArray();
		}

		return buf.length;
	}


	@Deprecated
	public ImplContent (String message){
		this.message = message;
	}

	@Deprecated
	public ImplContent(InputStream file, String fileExtension, String filename){
		setInputstream(file);
		sharkFile.setContentType(fileExtension);
		setFilename(filename);
		this.message = null;
	}

	@Deprecated
	public ImplContent(InputStream file, String fileExtension, String filename, String message){
		setInputstream(file);
		sharkFile.setContentType(fileExtension);
		setFilename(filename);
		this.message = message;
	}

}


