package net.sharksystem.sharknet.api;

import net.sharksystem.sharknet.api.utils.ResetOnCloseInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by timol on 01.06.2016.
 */
public class ImplContent implements Content {

	String fileExtension, message, filename;
	InputStream file;

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

	@Override
	public String getFileExtension() {
		return fileExtension;
	}

	@Override
	public InputStream getFile() {
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

		int read = 0;
		byte[] bytes = new byte[8192];

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while ((read = file.read(bytes)) != -1)
                bos.write(bytes,0,read);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] ba = bos.toByteArray();
		return new ByteArrayInputStream(ba);
	}

	//ToDo: Shark - How to save file in Shark
}
