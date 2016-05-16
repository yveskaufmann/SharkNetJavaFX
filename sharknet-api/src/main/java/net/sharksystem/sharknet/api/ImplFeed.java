package net.sharksystem.sharknet.api;

import java.util.List;

/**
 * Created by timol on 16.05.2016.
 */
public class ImplFeed implements Feed {

	public ImplFeed(String content, String interest){

	}
	@Override
	public String getInterest() {
		return null;
	}

	@Override
	public String getTimestamp() {
		return null;
	}

	@Override
	public String getContent() {
		return null;
	}

	@Override
	public String getSender() {
		return null;
	}

	@Override
	public List<Comment> getComments(int Anzahl) {
		return null;
	}

	@Override
	public void newComment(String comment) {
		Comment c = new ImplComment(comment);

	}
}
