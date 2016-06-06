package net.sharksystem.sharknet.api;

import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;



public class Dummy {


	public static void main(String[] args) {
		ImplSharkNet s = new ImplSharkNet();
		s.fillWithDummyData();
	}

	public void fillWithDummyData(ImplSharkNet s){

		//Anlegen von Profilen

		Profile bob_p = s.newProfile("bob", "bob@htw-berlin.de", "foomanchu");
		Profile alice_p  = s.newProfile("alice", "alice@htw-berlin.de", "foomanchu");

		bob_p.save();
		alice_p.save();

		s.setProfile(alice_p, "");

		Contact alice = alice_p.getContact();
		Contact bob = bob_p.getContact();

		//Set Profilepictures
		InputStream in = null;
		in = ClassLoader.getSystemClassLoader().getResourceAsStream("Alice.jpg");
		Content alicepic = new ImplContent(in, "jpg");
		alice.setPicture(alicepic);

		in = ClassLoader.getSystemClassLoader().getResourceAsStream("Bob.jpg");
		Content bobpic = new ImplContent(in, "jpg");
		bob.setPicture(bobpic);

 //Copy File with the ContentObject
/*		OutputStream out = null;
		try {
			out = new FileOutputStream("C:/tmp/picture.jpg");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int c;

		try {
			while ((c = alice.getPicture().getFile().read()) != -1) {
                out.write(c);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}

*/
		Contact alice_bob = s.newContact(bob.getNickname(), bob.getUID(), bob.getPublicKey());
		alice_bob.save();

		//Anlegen von Chats
		List<Contact> recipients1 = new ArrayList<>();
		recipients1.add(alice);
		List<Contact> recipients2 = new ArrayList<>();
		recipients2.add(bob);

		List<Contact> recipients3 = new ArrayList<>();
		recipients3.add(bob);
		recipients3.add(alice);

		Chat chat1 = s.newChat(recipients1);
		Chat chat2  = s.newChat(recipients2);
		Chat chat3  = s.newChat(recipients3);

		chat1.save();
		chat2.save();
		chat3.save();

		//Senden von Nachrichten

		chat1.sendMessage(new ImplContent("lorem ipsum"));
		chat1.sendMessage(new ImplContent("bla bla bla"));
		chat1.sendMessage(new ImplContent("fooo"));


		java.util.Date fiveMinAgo = new Date(System.currentTimeMillis()-5*60*1000);
		Timestamp time = new java.sql.Timestamp(fiveMinAgo.getTime());
		Message m1 = new ImplMessage(new ImplContent("answer 1"), time, bob, s.getMyProfile(), recipients1, false, false);
		DummyDB.getInstance().addMessage(m1, chat1);



//		ImplMessage(String message, Timestamp time, Contact sender, List<Contact> recipient_list, boolean isSigned, boolean isEncrypted)

		chat2.sendMessage(new ImplContent("bla bla bla"));
		chat2.sendMessage(new ImplContent("arg"));
		chat2.sendMessage(new ImplContent("lorem ipsum"));

		chat3.sendMessage(new ImplContent("this is a group message"));

		Interest i1 = new ImpInterest("sport", "www.sport,de", null, null);
		Interest i2 = new ImpInterest("shark", "www.shark,de", null, null);
		i1.save();
		i2.save();

		Feed f1 = s.newFeed(new ImplContent("this is the fist feed of sharkNet"), i2, bob);
		Feed f2 = s.newFeed(new ImplContent("sth about football"), i1, alice);
		f1.save();
		f2.save();

		//Add Comments

		f1.newComment(new ImplContent("this is amazing"), alice);
		f1.newComment(new ImplContent("i know"), bob);

		List<Feed> feedlist = s.getFeeds(10);
		System.out.println(f1.getContent());
		System.out.println(feedlist.get(0).getContent());
		f1.getComments(10).get(0).dislike();

		System.out.println(feedlist.get(1).getComments(2));



		s.setProfile(bob_p, "");
		Contact peter = s.newContact("peter", "dagobert@entenhausen.de", "foo");
		peter.save();
		List<Contact> recipients = new LinkedList<>();
		recipients.add(peter);
		Chat bob_peter = s.newChat(recipients);
		bob_peter.save();
		bob_peter.sendMessage(new ImplContent("hallo peter"));
		Message m_peter_bob = new ImplMessage(new ImplContent("hallo bob"), time, peter, s.getMyProfile(), recipients1, false, false);
		DummyDB.getInstance().addMessage(m_peter_bob, bob_peter);

		Feed bob_feed1 = s.newFeed(new ImplContent("bob thinks shark net is amazing"), i2, bob);
		bob_feed1.save();

		bob_feed1.newComment(new ImplContent("Peter thinks so too"), peter);
		s.getFeeds(5);

		s.setProfile(alice_p, "");




	}





}

