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
		Content alicepic = new ImplContent(in, "jpg", "Alice profile picture");
		alice.setPicture(alicepic);

		in = ClassLoader.getSystemClassLoader().getResourceAsStream("Bob.jpg");
		Content bobpic = new ImplContent(in, "jpg", "Bob profile picture");
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


		//Senden von Nachrichten

		chat1.sendMessage(new ImplContent("lorem ipsum"));
		chat1.sendMessage(new ImplContent("bla bla bla"));
		chat1.sendMessage(new ImplContent("fooo"));


		java.util.Date fiveMinAgo = new Date(System.currentTimeMillis()-5*60*1000);
		Timestamp time5ago = new java.sql.Timestamp(fiveMinAgo.getTime());
			;
		java.util.Date fiveMinAfter = new Date(System.currentTimeMillis()+5*60*1000);
		Timestamp time5after = new java.sql.Timestamp(fiveMinAfter.getTime());

		java.util.Date sevenMinAgo = new Date(System.currentTimeMillis()-100*60*1000);
		Timestamp time7ago = new java.sql.Timestamp(fiveMinAgo.getTime());
		;
		java.util.Date sevenMinAfter = new Date(System.currentTimeMillis()+100*60*1000);
		Timestamp time7after = new java.sql.Timestamp(fiveMinAfter.getTime());


		java.util.Date now = new Date(System.currentTimeMillis());
		Timestamp timenow = new java.sql.Timestamp(now.getTime());


		Message m1 = new ImplMessage(new ImplContent("answer 1"), time5ago, bob, s.getMyProfile(), recipients1, false, false);
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

		Feed fold = new ImplFeed((new ImplContent("this is the start")), i2, alice, time5ago, alice_p);
		Feed fnew = new ImplFeed((new ImplContent("this is the end")), i2, alice, time5after, alice_p);
		DummyDB.getInstance().addfeed(fold);
		DummyDB.getInstance().addfeed(fnew);
		Feed f1 = s.newFeed(new ImplContent("this is the fist feed of sharkNet"), i2, bob);
		Feed f2 = s.newFeed(new ImplContent("sth about football"), i1, alice);
		Feed f3 = s.newFeed(new ImplContent("football sucks"), i1, alice);

		//Add Comments

		f1.newComment(new ImplContent("this is amazing"), alice);
		f1.newComment(new ImplContent("i know"), bob);


		alice_p.getBlacklist().add(new ImplContact("alice exboyfriend", "hotboy@elitepartner.com", "", alice_p));



		List<Feed> feedlist = s.getFeeds(0, 15);
		System.out.println(f1.getContent());
		System.out.println(feedlist.get(0).getContent());
		f1.getComments().get(0).dislike();

		System.out.println(feedlist.get(0).getComments());


//Bobs stuff
		s.setProfile(bob_p, "");
		Contact peter = s.newContact("peter", "dagobert@entenhausen.de", "foo");
		List<Contact> recipients = new LinkedList<>();
		recipients.add(peter);
		Chat bob_peter = s.newChat(recipients);
		Message m_peter_bob = new ImplMessage(new ImplContent("hallo bob"), time5ago, peter, s.getMyProfile(), recipients1, false, false);
		DummyDB.getInstance().addMessage(m_peter_bob, bob_peter);
		bob_peter.sendMessage(new ImplContent("hallo peter"));


		Feed bob_feed1 = s.newFeed(new ImplContent("bob thinks shark net is amazing"), i2, bob);

		bob_feed1.newComment(new ImplContent("Peter thinks so too"), peter);
		s.getFeeds(5, 10);

		bob_p.getBlacklist().add(new ImplContact("bad hacker", "bad@hacker.com", "", bob_p));



		time7ago = java.sql.Timestamp.valueOf("2012-04-06 09:01:10");
		time7after = java.sql.Timestamp.valueOf("2017-04-06 09:01:10");

		s.setProfile(alice_p, "");
		List <Feed> foo = s.getFeeds("about", 0, 5);
		List <Feed> foo1 = s.getFeeds(timenow, time7after, 0, 10);

		List <Feed> foo2 = s.getFeeds(time7ago, timenow, 0, 10);
		List <Feed> foo3 = s.getFeeds(time7ago, time7after, 0, 10);
	}





}

