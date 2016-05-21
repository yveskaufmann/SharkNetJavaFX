package net.sharksystem.sharknet.api;

public class Dummy {


	public static void main(String[] args) {
		ImplSharkNet s = new ImplSharkNet();
		Profile p = s.newProfile(s.newContact("Alcie", "Alice@shark.de", "foo"));
		Chat chat = s.newChat(s.newContact("Bob", "Bob@shark.de", "foola"));
		chat.sendMessage("foo");


	}

}

