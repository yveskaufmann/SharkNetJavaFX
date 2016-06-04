package net.sharksystem.sharknet.api;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplComment implements Comment{

	//ToDo: Implement - File - Mine Type

	String comment;
	Feed reffeed;
	Timestamp datetime;
	Contact sender;
	Boolean disliked = false;
	Profile owner;


	/**
	 * This constructor is used to construct new Feeds writen by a user
	 * @param comment
	 * @param sender
	 * @param reffeed
     */
	public ImplComment(String comment, Contact sender, Feed reffeed, Profile owner){
		this.comment = comment;
		this.sender = sender;
		this.reffeed = reffeed;
		this.owner = owner;
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		datetime = new java.sql.Timestamp(now.getTime());

	}

	/**
	 * This constructor is used to construct Feeds which are already in the KB and are NOT going to be saved in the Database and sended
	 * @param comment
	 * @param sender
	 * @param datetime
     * @param reffeed
     */
	public ImplComment(String comment, Contact sender, Timestamp datetime, Feed reffeed, Profile owner){
		this.comment = comment;
		this.sender = sender;
		this.datetime = datetime;
		this.reffeed = reffeed;
		this.owner = owner;
	}

	@Override
	public Contact getSender() {
		return sender;
	}

	@Override
	public Timestamp getTimestamp() {
		return datetime;
	}

	@Override
	public Feed getRefFeed() {
		return reffeed;
	}

	@Override
	public String getContent() {
		return comment;
	}

	@Override
	public void delete() {
		reffeed.getComments(0).remove(this);
		//ToDo: Shark - delete Comment from Database
		//Implementation of DummyDB
		DummyDB.getInstance().removeComment(this, reffeed);
	}

	@Override
	public void save(){
		//ToDo: Shark - Safe Comment in KB and send it
		//Implementation of DummyDB
		DummyDB.getInstance().addComment(this, reffeed);
	}

	@Override
	public void dislike() {
		disliked = true;
		//ToDo: Shark - safe that the message was disliked
	}
}
