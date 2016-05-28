package net.sharksystem.sharknet.api;

import java.sql.Timestamp;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplComment implements Comment{

	//ToDo: Implement - File - Mine Type

	String comment;
	Feed reffeed;
	Timestamp datetime;
	Contact sender;

	/**
	 * This constructor is used to construct new Feeds writen by a user
	 * @param comment
	 * @param sender
	 * @param reffeed
     */
	public ImplComment(String comment, Contact sender, Feed reffeed){
		this.comment = comment;
		this.sender = sender;
		this.reffeed = reffeed;
		java.util.Date date= new java.util.Date();
		datetime.setTime(date.getTime());
	}

	/**
	 * This constructor is used to construct Feeds which are already in the KB and are NOT going to be saved in the Database and sended
	 * @param comment
	 * @param sender
	 * @param datetime
     * @param reffeed
     */
	public ImplComment(String comment, Contact sender, Timestamp datetime, Feed reffeed){
		this.comment = comment;
		this.sender = sender;
		this.datetime = datetime;
		this.reffeed = reffeed;
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
	}

	@Override
	public void save(){
		//ToDo: Shark - Safe Comment in KB and send it
	}
}
