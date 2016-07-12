package net.sharksystem.sharknet.api;

/**
 * @Author Yves Kaufmann
 * @since 09.07.2016
 */
public abstract class GetEventsAdapter implements GetEvents {
	@Override
	public void receivedMessage(Message m) {}

	@Override
	public void receivedFeed(Feed f) {}

	@Override
	public void receivedComment(Comment c) {

	}
	@Override
	public void receivedContact(Contact c) {

	}
}
