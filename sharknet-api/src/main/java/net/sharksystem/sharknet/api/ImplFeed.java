package net.sharksystem.sharknet.api;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplFeed implements Feed {

	Content content;
	Profile owner;
	Timestamp datetime;
	Interest interest;
	Contact sender;
	Boolean disliked = false;


	/**
	 * This constructor is used to construct new Feeds which are going to be saved in the Database and sended
	 * @param content
	 * @param interest
	 * @param sender
     */
	public ImplFeed(Content content, Interest interest, Contact sender, Profile owner){
		this.content = content;
		this.interest = interest;
		this.sender = sender;
		this. owner = owner;
		datetime = new Timestamp(new Date().getTime());
		save();
	}

	/**
	 * This Constructor is used to construct the Feeds already in the Database, it will not send or safe them
	 * @param content
	 * @param interest
	 * @param sender
     * @param datetime
     */
	public ImplFeed(Content content, Interest interest, Contact sender, Timestamp datetime, Profile owner){
		this.sender = sender;
		this.interest = interest;
		this.content = content;
		this.datetime = datetime;
		this.owner = owner;
	}

	@Override
	public Interest getInterest() {
		return interest;
	}

	@Override
	public Timestamp getTimestamp() {
		return datetime;
	}

	@Override
	public Content getContent() {
		return content;
	}

	@Override
	public Contact getSender() {
		return sender;
	}

	@Override
	public List<Comment> getComments() {
		//ToDo: Shark - search for comments construct the objects and fill the list - sorted by time
		List<Comment> commentlist = DummyDB.getInstance().getComments(this);
		return commentlist;
	}

	@Override
	public List<Comment> getComments(int startIndex, int stopIndex) {
		//ToDo: Shark - search for comments within the intervall - sorted by time
		List<Comment> commentlist = DummyDB.getInstance().getComments(this, startIndex, stopIndex);
		return commentlist;
	}

	@Override
	public List<Comment> getComments(Timestamp start, Timestamp stop) {
		//ToDo: Shark - search for comments within the timerange  - sorted by time
		List<Comment> commentlist = DummyDB.getInstance().getComments(this, start, stop);
		return commentlist;
	}

	@Override
	public List<Comment> getComments(Timestamp start, Timestamp stop, int startIndex, int stopIndex) {
		//ToDo: Shark - search for comments within the timerange and intervall - sorted by time
		List<Comment> commentlist = DummyDB.getInstance().getComments(this, startIndex, stopIndex, start, stop);
		return commentlist;
	}

	@Override
	public List<Comment> getComments(String search, int startIndex, int stopIndex) {
		//ToDo: Shark - search for comments within the intervall containing the searchstring - sorted by time
		List<Comment> commentlist = DummyDB.getInstance().getComments(this, search,  startIndex, stopIndex);
		return commentlist;
	}


	@Override
	public void newComment(Content comment, Contact author) {
		Comment c = new ImplComment(comment, author, this, owner);
		DummyDB.getInstance().addComment(c, this);
	}

	/**
	 * Saves the Feed in the DB
	 */

	private void save() {
		//ToDo: Shark - safe Feed in KB and sends it
		//Implementation of DummyDB
		DummyDB.getInstance().addfeed(this);
	}

	@Override
	public void delete() {
		//ToDo: Shark - delte Feed in KB
		//Implementation of DummyDB
		DummyDB.getInstance().removefeed(this);
	}

	@Override
	public void dislike() {
		disliked = true;
		//ToDo: Shark - safe that the message was disliked
	}

	@Override
	public Profile getOwner() {
		return owner;
	}

	@Override
	public boolean isdisliked() {
		return disliked;
	}
}

