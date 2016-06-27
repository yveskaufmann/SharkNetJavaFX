package net.sharksystem.sharknet.api;

/**
 * Created by timol on 27.06.2016.
 */


/**
 * This Interface have to be implemented to receive Notifications when sth is received
 */
public interface GetEvents {
	public void receivedMessage(Message m);
	public void receivedFeed(Feed f);
	public void receivedComment(Comment c);


}
