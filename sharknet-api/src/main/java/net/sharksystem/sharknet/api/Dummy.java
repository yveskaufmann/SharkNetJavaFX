package net.sharksystem.sharknet.api;

import net.sharkfw.knowledgeBase.TXSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoInformation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;


public class Dummy {


	public static void main(String[] args) {
		ImplSharkNet s = new ImplSharkNet();
		s.fillWithDummyData();
	}

	public void fillWithDummyData(ImplSharkNet s){

		//Variables for MimeType
		String jpg = "image/jpeg";


		//Timestamps erzeugen um Messages und Feeds mit von der aktuellen Uhrzeit abweichend zu erzeugen
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

		java.util.Date oneDayAgo = new Date(System.currentTimeMillis() - 24*60*60*1000);
		Timestamp timeOneDayAgo = new java.sql.Timestamp(oneDayAgo.getTime());

		java.util.Date twoDayAgo = new Date(System.currentTimeMillis() - 24*60*60*1000*2);
		Timestamp timeTwoDayAgo = new java.sql.Timestamp(twoDayAgo.getTime());

		java.util.Date now = new Date(System.currentTimeMillis());
		Timestamp timenow = new java.sql.Timestamp(now.getTime());

		//Anlegen von Profilen
		Profile bob_p = s.newProfile("bob", "bobsDevice");
		Profile alice_p  = s.newProfile("alice", "alicesDevice");

		//Aktives Profil ist Alice
		s.setProfile(alice_p, "");

		//Kontakte von Alice und Bob laden
		Contact alice = alice_p.getContact();
		Contact bob = bob_p.getContact();


		//Profilbilder von Alice und Bob setzen

		File filealice = new File("sharknet-api\\src\\main\\resources\\Alice.jpg");
		Content alicecon = new ImplContent(alice_p);
		alicecon.setFile(filealice);
		alicecon.setMimeType(jpg);
		alice.setPicture(alicecon);

		File filebob = new File("sharknet-api\\src\\main\\resources\\Bob.jpg");
		Content bobcon = new ImplContent(bob_p);

		bobcon.setFile(filebob);
		bobcon.setMimeType(jpg);
		bob.setPicture(bobcon);


		//Kontakte von Alice setzen
		Contact alice_bob = s.newContact(bob.getNickname(), bob.getUID(), bob.getPublicKey());
		Contact alice_charles = s.newContact("charles", "charlesuid", "charlespublickey");
		Contact alice_dean = s.newContact("dean", "deanuid", "deanpublickey");
		Contact alice_erica = s.newContact("erica", "ericauid", "ericapublickey");
		Contact alice_frank = s.newContact("frank", "frankuid", "frankpublickey");

		//Letzter Wifi-Kontakt mit Charles setzen
		alice_charles.setLastWifiContact(timenow);

		//Profilbild von Charles und Dean und Bob
		Content bobpic = new ImplContent(alice_p);
		bobpic.setFile(filebob);
		bobpic.setMimeType(jpg);
		alice_bob.setPicture(bobpic);

		Content charlespic = new ImplContent(alice_p);
		charlespic.setFile(filebob);
		charlespic.setMimeType(jpg);
		alice_charles.setPicture(charlespic);

		Content deanpic = new ImplContent(alice_p);
		deanpic.setFile(filebob);
		deanpic.setMimeType(jpg);
		alice_dean.setPicture(deanpic);


		//Anlegen von Chats
		//1. Kontaktlisten anlegen
		List<Contact> recipients1 = new ArrayList<>();
		recipients1.add(bob);
		recipients1.add(alice_charles);
		recipients1.add(alice_dean);
		recipients1.add(alice_erica);
		recipients1.add(alice_frank);
		List<Contact> recipients2 = new ArrayList<>();
		recipients2.add(alice_charles);
		List<Contact> recipients3 = new ArrayList<>();
		recipients3.add(bob);
		recipients3.add(alice_charles);

		//2.Chats mit Kontaktlisten anelgen
		Chat chat1 = s.newChat(recipients1);
		Chat chat2  = s.newChat(recipients2);
		Chat chat3  = s.newChat(recipients3);

		//3. Senden von Nachrichten
		chat1.sendMessage(new ImplContent("lorem ipsum", alice_p));
		chat1.sendMessage(new ImplContent("bla bla bla", alice_p));
		chat1.sendMessage(new ImplContent("fooo", alice_p));

		//4. Nachrichten hinzufügen die Alice bekommen hat
		Message m1 = new ImplMessage(new ImplContent("answer 3", alice_p), time5ago, bob, s.getMyProfile(), recipients1, false, false);
		DummyDB.getInstance().addMessage(m1, chat1);
		Message m2 = new ImplMessage(new ImplContent("answer 2", alice_p), timeOneDayAgo, bob, s.getMyProfile(), recipients1, false, false);
		DummyDB.getInstance().addMessage(m2, chat1);
		Message m3 = new ImplMessage(new ImplContent("answer 1", alice_p), timeTwoDayAgo, bob, s.getMyProfile(), recipients1, false, false);
		DummyDB.getInstance().addMessage(m3, chat1);

		// vote
		Content content = new ImplContent("", alice_p);
		Voting votedummy = content.addVoting("Wann soll die nächste WG-Party stattfinden?", false);
		List<String> answersdummy = Arrays.asList(
			"Montag",
			"Dienstag",
			"Mittwoch",
			"Donnerstag",
			"Freitag",
			"Samstag",
			"Sonntag"
		);
		votedummy.addAnswers(answersdummy);

		HashMap<String, Contact> answersmap = votedummy.getAnswers();
		answersmap.put(answersdummy.get(4), alice_p.getContact());
		answersmap.put(answersdummy.get(5), alice_dean);
		answersmap.put(answersdummy.get(6), alice_frank);
		votedummy.vote(answersmap);

		answersmap = votedummy.getAnswers();
		answersmap.put(answersdummy.get(4), alice_bob);
		answersmap.put(answersdummy.get(5), alice_erica);
		votedummy.vote(answersmap);

		answersmap = votedummy.getAnswers();
		answersmap.put(answersdummy.get(4), alice_charles);
		votedummy.vote(answersmap);

		chat1.sendMessage(content);

		//Senden von Nachrichten aus dem Chat 2 und 3
		chat2.sendMessage(new ImplContent("bla bla bla", alice_p));
		chat2.sendMessage(new ImplContent("arg", alice_p));
		chat2.sendMessage(new ImplContent("lorem ipsum", alice_p));
		chat3.sendMessage(new ImplContent("this is a group message", alice_p));


		//Setzen von Encryted und Signiert usw auf True
		List<Message> chat1_m = chat1.getMessages(true);
		for(Message m : chat1_m){
			m.setEncrypted(true);
			m.setSigned(true);
			m.setDierectRecived(true);
			m.setVerified(true);
		}



		// Interessen anlegen, Fussball wird unter Sport eingeordnet
		Interest i1 = new ImplInterest(alice);
		TXSemanticTag si1 = i1.addInterest("sport", "www.sport.de");
		TXSemanticTag si2 = i1.addInterest("fußball", "www.fußball.de");
		i1.moveInterest(si1, si2);

		//Bob hat nur das interesse shark
		Interest i2 = new ImplInterest(bob);
		TXSemanticTag si3 = i2.addInterest("shark", "www.sharknet.de");

		//Feeds anlegen
		Feed f1 = s.newFeed(new ImplContent("this is the fist feed of sharkNet", alice_p), i2, bob);
		Feed f2 = s.newFeed(new ImplContent("sth about football", alice_p), i1, alice);
		Feed f3 = s.newFeed(new ImplContent("football sucks", alice_p), i1, alice);

		//Comments an die Fees anhängen
		f1.newComment(new ImplContent("this is amazing", alice_p), alice);
		f1.newComment(new ImplContent("i know", alice_p), bob);

		// Dislike eines Comments
		f1.getComments(true).get(0).setDisliked(true);

		//Blacklist anlegen, Alice hat ihren Exfreund auf der Liste
		alice_p.getBlacklist().add(new ImplContact("alice exboyfriend", "hotboy@elitepartner.com", "", alice_p));


//Bobs stuff
		s.setProfile(bob_p, "");
		Contact peter = s.newContact("peter", "dagobert@entenhausen.de", "foo");
		List<Contact> recipients = new LinkedList<>();
		recipients.add(peter);
		Chat bob_peter = s.newChat(recipients);
		Message m_peter_bob = new ImplMessage(new ImplContent("hallo bob", bob_p), time5ago, peter, s.getMyProfile(), recipients1, false, false);
		DummyDB.getInstance().addMessage(m_peter_bob, bob_peter);
		bob_peter.sendMessage(new ImplContent("hallo peter", bob_p));


		Feed bob_feed1 = s.newFeed(new ImplContent("bob thinks shark net is amazing", bob_p), i2, bob);

		bob_feed1.newComment(new ImplContent("Peter thinks so too", bob_p), peter);
		s.getFeeds(5, 10, true);

		bob_p.getBlacklist().add(new ImplContact("bad hacker", "bad@hacker.com", "", bob_p));



		time7ago = java.sql.Timestamp.valueOf("2012-04-06 09:01:10");
		time7after = java.sql.Timestamp.valueOf("2017-04-06 09:01:10");


		//Generate a Content and add a Voting
		Content c_test = new ImplContent("foo", bob_p);
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

		Message mlistener1 = new ImplMessage(new ImplContent("received through listener - bob to alice", bob_p), time5ago, bob, s.getMyProfile(), recipients1, false, false);
		Message mlistener2 = new ImplMessage(new ImplContent("received through listener - alice to bob", bob_p), time5ago, alice, s.getMyProfile(), recipients2, false, false);


		s.informMessage(mlistener2);
		s.informMessage(mlistener1);
		s.exchangeContactNFC();


		//Set Default Profile to alice

		s.setProfile(alice_p, "");


	}





}

