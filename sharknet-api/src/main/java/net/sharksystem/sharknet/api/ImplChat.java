package net.sharksystem.sharknet.api;

import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplChat implements Chat {

	Contact c;


	public ImplChat(Contact c){
		this.c = c;
	}

	@Override
	public void sendMessage(String message) {
		Message m = new ImplMessage(message, c);
	}

	@Override
	public void deleteChat() {

	}

	@Override
	public List<Message> getMessages() {
		return null;
	}
}
