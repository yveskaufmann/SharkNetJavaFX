package net.sharksystem.sharknet.api;

import java.util.ArrayList;
import java.util.List;

public class Dummy {

	//ToDo: Dummy Daten anlegen


	public static void main(String[] args) {
		ImplSharkNet s = new ImplSharkNet();
		s.fillWithDummyData();
	}

	public void fillWithDummyData(ImplSharkNet s){

		//Anlegen von contacts
		Contact bob = s.newContact("bob", "bob@htw-berlin.de", "foomanchu");
		Contact alice  = s.newContact("alice", "alice@htw-berlin.de", "foomanchu");

		//Anlegen von profilen

		Profile p1 = s.newProfile(alice);
		Profile p2 =s.newProfile(bob);
		p1.save();
		p2.save();

		//Anlegen von Chats
		List<Contact> recipients1 = new ArrayList<>();
		recipients1.add(alice);
		List<Contact> recipients2 = new ArrayList<>();
		recipients2.add(bob);
		Chat chat1 = s.newChat(recipients1);
		Chat chat2  = s.newChat(recipients2);
		chat1.save();
		chat2.save();

		//Senden von Nachrichten
		chat1.sendMessage("lorem ipsum");
		chat1.sendMessage("bla bla bla");
		chat1.sendMessage("fooo");

		chat2.sendMessage("bla bla bla");
		chat2.sendMessage("arg");
		chat2.sendMessage("lorem ipsum");

		//ToDo: Dummy - empfangene nachrichten hinzufügen

		Interest i1 = new ImpInterest("sport", "www.sport,de", null, null);
		Interest i2 = new ImpInterest("shark", "www.shark,de", null, null);
		i1.save();
		i2.save();

		Feed f1 = s.newFeed("this is the fist feed of sharkNet", i2, bob);
		Feed f2 = s.newFeed("sth about football", i1, alice);
		f1.save();
		f2.save();

		//Add Comments

		f1.newComment("this is amazing", alice);
		f1.newComment("i know", bob);

		List<Feed> feedlist = s.getFeeds(10);
		System.out.println(f1.getContent());
		System.out.println(feedlist.get(0).getContent());

		System.out.println(feedlist.get(1).getComments(2));
	}



}

