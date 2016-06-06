package net.sharksystem.sharknet.api;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplComment implements Comment{


	Content comment;
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
	public ImplComment(Content comment, Contact sender, Feed reffeed, Profile owner){
		this.comment = comment;
		this.sender = sender;
		this.reffeed = reffeed;
		this.owner = owner;
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		datetime = new java.sql.Timestamp(now.getTime());
		save();

	}

	/**
	 * This constructor is used to construct Feeds which are already in the KB and are NOT going to be saved in the Database and sended
	 * @param comment
	 * @param sender
	 * @param datetime
     * @param reffeed
     */
	public ImplComment(Content comment, Contact sender, Timestamp datetime, Feed reffeed, Profile owner){
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
	public Content getContent() {
		return comment;
	}

	@Override
	public void delete() {
		//ToDo: Shark - delete Comment from Database
		//Implementation of DummyDB
		DummyDB.getInstance().removeComment(this, reffeed);
	}

	private void save(){
		//ToDo: Shark - Safe Comment in KB and send it
		//Implementation of DummyDB
		DummyDB.getInstance().addComment(this, reffeed);
	}

	@Override
	public void dislike() {
		disliked = true;
		//ToDo: Shark - safe that the message was disliked
	}

	@Override
	public boolean isdisliked() {
		return disliked;
	}
}
