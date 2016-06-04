package net.sharksystem.sharknet.api;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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

		List<Contact> recipients3 = new ArrayList<>();
		recipients3.add(bob);
		recipients3.add(alice);

		Chat chat1 = s.newChat(recipients1, bob);
		Chat chat2  = s.newChat(recipients2, alice);
		Chat chat3  = s.newChat(recipients3, bob);

		chat1.save();
		chat2.save();
		chat3.save();

		//Senden von Nachrichten

		chat1.sendMessage(new ImplContent("lorem ipsum"));
		chat1.sendMessage(new ImplContent("bla bla bla"));
		chat1.sendMessage(new ImplContent("fooo"));


		java.util.Date fiveMinAgo = new Date(System.currentTimeMillis()-5*60*1000);
		Timestamp time = new java.sql.Timestamp(fiveMinAgo.getTime());
		Message m1 = new ImplMessage(new ImplContent("answer 1"), time, bob, recipients1, false, false);
		DummyDB.getInstance().addMessage(m1, chat1);



//		ImplMessage(String message, Timestamp time, Contact sender, List<Contact> recipient_list, boolean isSigned, boolean isEncrypted)

		chat2.sendMessage(new ImplContent("bla bla bla"));
		chat2.sendMessage(new ImplContent("arg"));
		chat2.sendMessage(new ImplContent("lorem ipsum"));

		chat3.sendMessage(new ImplContent("this is a group message"));

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

