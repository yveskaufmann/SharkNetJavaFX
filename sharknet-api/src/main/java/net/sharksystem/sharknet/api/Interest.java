package net.sharksystem.sharknet.api;

import java.util.List;

/**
 * Created by timol on 17.05.2016.
 */
public interface Interest {

	public String getName();
	public String getURI();
	public List<Interest> getChilds();
	public List<Interest> getParents();
	public void safeInKB();
	public void deleteInterest();


}
