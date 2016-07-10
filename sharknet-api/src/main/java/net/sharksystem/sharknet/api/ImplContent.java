package net.sharksystem.sharknet.api;

import net.sharksystem.sharknet.api.utils.ResetOnCloseInputStream;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;

/**
 * Created by timol on 01.06.2016.
 */
public class ImplContent implements Content {

	String fileExtension, message, filename;
	InputStream file;
	private byte[] bytesOfFile;
	ImplVoting voting;


	public ImplContent (String message){
		this.message = message;
		fileExtension = null;
		file = null;
		filename = null;
	}

	public ImplContent(InputStream file, String fileExtension, String filename){
		this.file = file;
		this.fileExtension = fileExtension;
		this.message = null;
		this.filename = filename;
	}

	public ImplContent(InputStream file, String fileExtension, String filename, String message){
		this.file = file;
		this.fileExtension = fileExtension;
		this.message = message;
		this.filename = filename;

	}
/*
	public String getMimeType(File f){
		final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
		return fileTypeMap.getContentType(f);
	}

*/

	@Override
	public String getFileExtension() {
		return fileExtension;
	}

	@Override
	public InputStream getFile() {
		/*
		   JarInputStream are don't supports marks in this
		   case we need another solution.
		 */
		if (! file.markSupported()) {
			return new ResetOnCloseInputStream(new BufferedInputStream(file));
		}
		return new ResetOnCloseInputStream(file);
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getFileName(){
		return filename;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns a Copy of the IO Stream. This is because once the IOStream was read it will be closed.
	 * With shark implementation it could be possible to not just copy the io stream but make a new one
 	 * @return
     */
	private InputStream swapFile()  {
		/***
		 * Funktioniert leider nicht, da der Stream mehrfach gelesen wird
		 * nach dem ersten Lesen befindet sich der interne Pointer bereits am Ende
		 * der Datei oder des Buffers.
		 *
		 * Würde dir folgende alternativen Vorschlagen:
		 *
		 * 1. Ähnlich deiner SwapFile Implementierung nur,
		 * dass nur einmal gelesen wird und die bytes in einer Member
		 * Variable gespeichert werden. Ich denke das war deine Intention
		 * dahinter :)
		 *
		 *
		 * 2. Du stellst sicher, dass der zurück gegebene Stream beim Aufruf von {@link InputStream#close()}
		 * zurück gesetzt wird. Ich habe dir einen entsprechenden proxy InputStream gebaut
		 * und den exemplarisch in {@link #getFile()} eingebaut.
		 */

		byte[] readBuffer = new byte[8192];
		if (bytesOfFile == null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				int read = 0;
				while ((read = file.read(readBuffer)) != -1) {
					bos.write(readBuffer, 0, read);
				}
				bytesOfFile = bos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (file != null) {
					try {
						file.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return new ByteArrayInputStream(bytesOfFile);
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


	//ToDo: Shark - How to save file in Shark
}
