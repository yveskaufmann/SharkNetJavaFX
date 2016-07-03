package net.sharksystem.sharknet.javafx.controller.chat;

import net.sharksystem.sharknet.api.Chat;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Content;

import java.util.List;

/**
 * Created by Benni on 01.06.2016.
 */
public interface ChatListener {

	public void onContactListChanged(List<Contact> c);
	public void onEmojiChoose(String emojiClass);
	public void onVoteAdded(Content vote);
	public void onChatDeleted(Chat chat);
}
