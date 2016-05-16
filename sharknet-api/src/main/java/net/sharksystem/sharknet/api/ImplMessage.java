package net.sharksystem.sharknet.api;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplMessage implements Message {

	public ImplMessage(String message, Contact c){

	}
	@Override
	public String getTimestamp() {
		return null;
	}
	@Override
	public Contact getSender() {
		return null;
	}

	@Override
	public Contact getRecipient() {
		return null;
	}

	@Override
	public String getContent() {
		return null;
	}

	@Override
	public boolean isSigned() {
		return false;
	}

	@Override
	public boolean isEncrypted() {
		return false;
	}

	@Override
	public void deleteMessage() {

	}
}
