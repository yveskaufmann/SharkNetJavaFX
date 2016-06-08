package net.sharksystem.sharknet.javafx.controller.chat;

import net.sharksystem.sharknet.api.Contact;

import java.util.List;

/**
 * Created by Benni on 01.06.2016.
 */
public interface ChatListener {

	public void onContactListChanged(List<Contact> c);
	public void onEmojiChoose(String emojiClass);
}
