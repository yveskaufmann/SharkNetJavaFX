package net.sharksystem.sharknet.api;

//ToDo: Implement - Methods for InterestMgmt


import java.util.LinkedList;
import java.util.List;

/**
 * Created by timol on 18.05.2016.
 */
public class ImpInterest implements Interest {

	String name, uri;
	List<Interest> child_list = new LinkedList<>();
	List<Interest> parent_list = new LinkedList<>();

	public ImpInterest(String name, String uri, List<Interest> childs, List<Interest> parents){
		this.name = name;
		this.uri = uri;
		this.child_list = childs;
		this.parent_list = parents;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getURI() {
		return uri;
	}

	@Override
	public List<Interest> getChilds() {
		//ToDo: Shark - find Childs in KB
		return child_list;
	}

	@Override
	public List<Interest> getParents() {
		//ToDo: Shark - find Parent in KB
		//ToDo: Clearify - more than one Parent possible
		return parent_list;
	}

	@Override
	public void safeInKB() {
		//ToDo: Shark - safe Interest in KB
	}


	@Override
	public void deleteInterest() {
		//ToDo: Shark - delete Interest from KB
	}
}
