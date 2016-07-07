package net.sharksystem.sharknet.api;


import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.TXSemanticTag;
import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class Dummy {


	public static void main(String[] args) {
		ImplSharkNet s = new ImplSharkNet();
		s.fillWithDummyData();
	}

	public void fillWithDummyData(ImplSharkNet s){

		//Anlegen von Profilen

		Profile bob_p = s.newProfile("bob", "bobsDevice");
		Profile alice_p  = s.newProfile("alice", "alicesDevice");

		s.setProfile(alice_p, "");

		Contact alice = alice_p.getContact();
		Contact bob = bob_p.getContact();


		//Set Profilepictures
		InputStream in = null;
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		in = cl.getResourceAsStream("Alice.jpg");
		Content alicepic = new ImplContent(in, "jpg", "Alice profile picture");
		alice.setPicture(alicepic);

		in = cl.getResourceAsStream("Bob.jpg");
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
		Contact alice_charles = s.newContact("charles", "charlesuid", "charlespublickey");
		Contact alice_dean = s.newContact("dean", "deanuid", "deanpublickey");
		//Anlegen von Chats
		List<Contact> recipients1 = new ArrayList<>();
		recipients1.add(bob);
		List<Contact> recipients2 = new ArrayList<>();
		recipients2.add(alice_charles);

		List<Contact> recipients3 = new ArrayList<>();
		recipients3.add(bob);
		recipients3.add(alice_charles);

		in = cl.getResourceAsStream("Bob.jpg");
		Content charlespic = new ImplContent(in, "jpg", "Charles profile picture");
		alice_charles.setPicture(charlespic);

		in = cl.getResourceAsStream("Bob.jpg");
		Content deanpic = new ImplContent(in, "jpg", "Deans profile picture");
		alice_dean.setPicture(deanpic);

		Chat chat1 = s.newChat(recipients1);
		Chat chat2  = s.newChat(recipients2);
		Chat chat3  = s.newChat(recipients3);


		//Senden von Nachrichten

		chat1.sendMessage(new ImplContent("lorem ipsum"));
		chat1.sendMessage(new ImplContent("bla bla bla"));
		chat1.sendMessage(new ImplContent("fooo"));


		java.util.Date fiveMinAgo = new Date(System.currentTimeMillis()-5*60*1000);
		Timestamp time5ago = new java.sql.Timestamp(fiveMinAgo.getTime());

		java.util.Date oneDayAgo = new Date(System.currentTimeMillis() - 24*60*60*1000);
		Timestamp timeOneDayAgo = new java.sql.Timestamp(oneDayAgo.getTime());

		java.util.Date twoDayAgo = new Date(System.currentTimeMillis() - 24*60*60*1000*2);
		Timestamp timeTwoDayAgo = new java.sql.Timestamp(twoDayAgo.getTime());

		java.util.Date fiveMinAfter = new Date(System.currentTimeMillis()+5*60*1000);
		Timestamp time5after = new java.sql.Timestamp(fiveMinAfter.getTime());

		java.util.Date sevenMinAgo = new Date(System.currentTimeMillis()-100*60*1000);
		Timestamp time7ago = new java.sql.Timestamp(fiveMinAgo.getTime());

		java.util.Date sevenMinAfter = new Date(System.currentTimeMillis()+100*60*1000);
		Timestamp time7after = new java.sql.Timestamp(fiveMinAfter.getTime());


		java.util.Date now = new Date(System.currentTimeMillis());
		Timestamp timenow = new java.sql.Timestamp(now.getTime());


		Message m1 = new ImplMessage(new ImplContent("answer 3"), time5ago, bob, s.getMyProfile(), recipients1, false, false);
		DummyDB.getInstance().addMessage(m1, chat1);
		Message m2 = new ImplMessage(new ImplContent("answer 2"), timeOneDayAgo, bob, s.getMyProfile(), recipients1, false, false);
		DummyDB.getInstance().addMessage(m2, chat1);
		Message m3 = new ImplMessage(new ImplContent("answer 1"), timeTwoDayAgo, bob, s.getMyProfile(), recipients1, false, false);
		DummyDB.getInstance().addMessage(m3, chat1);

//		ImplMessage(String message, Timestamp time, Contact sender, List<Contact> recipient_list, boolean isSigned, boolean isEncrypted)

		chat2.sendMessage(new ImplContent("bla bla bla"));
		chat2.sendMessage(new ImplContent("arg"));
		chat2.sendMessage(new ImplContent("lorem ipsum"));

		chat3.sendMessage(new ImplContent("this is a group message"));

// Interest Managemnt
		Interest i1 = new ImplInterest(alice);
		TXSemanticTag si1 = i1.addInterest("sport", "www.sport.de");
		TXSemanticTag si2 = i1.addInterest("fußball", "www.fußball.de");
		i1.moveInterest(si1, si2);

		Interest i2 = new ImplInterest(bob);
		TXSemanticTag si3 = i2.addInterest("shark", "www.sharknet.de");

		i1.save();
		i2.save();

		Feed f1 = s.newFeed(new ImplContent("this is the fist feed of sharkNet"), i2, bob);
		Feed f2 = s.newFeed(new ImplContent("sth about football"), i1, alice);
		Feed f3 = s.newFeed(new ImplContent("football sucks"), i1, alice);

		//Add Comments

		f1.newComment(new ImplContent("this is amazing"), alice);
		f1.newComment(new ImplContent("i know"), bob);


		alice_p.getBlacklist().add(new ImplContact("alice exboyfriend", "hotboy@elitepartner.com", "", alice_p));



		List<Feed> feedlist = s.getFeeds(0, 15, true);
		// System.out.println(f1.getContent());
		// System.out.println(feedlist.get(0).getContent());
		f1.getComments(true).get(0).dislike();

		// System.out.println(feedlist.get(0).getComments());


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
		s.getFeeds(5, 10, true);

		bob_p.getBlacklist().add(new ImplContact("bad hacker", "bad@hacker.com", "", bob_p));



		time7ago = java.sql.Timestamp.valueOf("2012-04-06 09:01:10");
		time7after = java.sql.Timestamp.valueOf("2017-04-06 09:01:10");


		//Generate a Content and add a Voting
		Content c_test = new ImplContent("foo");
		ImplVoting vote = c_test.addVoting("what is foo", false);

		//Generate a list of Answers
		List<String> testanswers = new LinkedList<>();
		testanswers.add("fooans1");
		testanswers.add("fooans2");
		testanswers.add("fooans3");

		//Add the Answers to the voting
		vote.addAnswers(testanswers);


		//Get a HashMap to add the contact to the voting
		HashMap<String, Contact> answers = vote.getAnswers();
		answers.put(testanswers.get(2), alice);

		//Return the Hasmap with the Voting
		vote.vote(answers);

		//Get a Hash Map with Answers an List of Content to See the Voting
		HashMap<String, List<Contact>> voting_finished = vote.getVotings();


		TestListener foolistener = new TestListener();
		s.addListener(alice_p, foolistener);

		Message mlistener1 = new ImplMessage(new ImplContent("received through listener - bob to alice"), time5ago, bob, s.getMyProfile(), recipients1, false, false);
		Message mlistener2 = new ImplMessage(new ImplContent("received through listener - alice to bob"), time5ago, alice, s.getMyProfile(), recipients2, false, false);


		s.informMessage(mlistener2);
		s.informMessage(mlistener1);
		s.exchangeContactNFC();


		s.setProfile(alice_p, "");


	}





}

