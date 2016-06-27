package net.sharksystem.sharknet.api;

/**
 * Created by timol on 27.06.2016.
 */
public class TestListener  implements GetEvents{
	@Override
	public void receivedMessage(Message m) {
		System.out.println(m.getContent().getMessage());
	}

	@Override
	public void receivedFeed(Feed f) {
		System.out.println(f.getContent().getMessage());
	}

	@Override
	public void receivedComment(Comment c) {
		System.out.println(c.getContent().getMessage());
	}
}
