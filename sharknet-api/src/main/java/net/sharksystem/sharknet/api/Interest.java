package net.sharksystem.sharknet.api;

import java.util.List;

/**
 * Created by timol on 17.05.2016.
 */
public interface Interest {

	public String getName();
	public void setName(String name);
	public String getURI();
	public void setURI(String uri);
	public List<Interest> getChilds();
	public List<Interest> getParents();
	public void save();
	public void delete();


}
