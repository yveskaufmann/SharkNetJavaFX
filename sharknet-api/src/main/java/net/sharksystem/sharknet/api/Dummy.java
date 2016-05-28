package net.sharksystem.sharknet.api;

import java.util.List;

public class Dummy {

	//ToDo: Dummy Daten anlegen


	public static void main(String[] args) {
		ImplSharkNet s = new ImplSharkNet();
		s.fillWithDummyData();







	}

	public void fillWithDummyData(SharkNet s){

		//Anlegen von contacts
		Contact bob = s.newContact("bob", "bob@htw-berlin.de", "foomanchu");
		Contact alice  = s.newContact("alice", "alice@htw-berlin.de", "foomanchu");

		//Anlegen von profilen

		Profile p1 = s.newProfile(alice);
		Profile p2 =s.newProfile(bob);

		//Anlegen von Chats
		Chat chat1 = s.newChat(alice);
		Chat chat2  = s.newChat(bob);

		//Senden von Nachrichten





		Chat chat = s.newChat(s.newContact("Bob", "Bob@shark.de", "foola"));
		chat.sendMessage("foo");
		List<Message> messages = chat.getMessages();
		System.out.println(messages);

		boolean loggedIn = p1.login("");
		System.out.println(loggedIn);

		p1.setPassword("foo");
		System.out.println(p1.login("bla"));
		System.out.println(p1.login("foo"));



	}



}

