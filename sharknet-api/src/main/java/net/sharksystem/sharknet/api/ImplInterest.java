package net.sharksystem.sharknet.api;


import net.sharkfw.knowledgeBase.*;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timol on 18.05.2016.
 */

public class ImplInterest implements Interest {

	SharkKB kb = null;
	Taxonomy tx = null;

	public ImplInterest(Contact owner){
		kb = new InMemoSharkKB();

		//ToDo: Adress nessecariy

		kb.setOwner(InMemoSharkKB.createInMemoPeerSemanticTag(owner.getName(),
			owner.getUID(),
			""));
		try {
			tx = kb.getTopicsAsTaxonomy();
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
	}


	@Override
	public Taxonomy getInterests() {
		return tx;
	}

	@Override
	public TXSemanticTag addInterest(String name, String si) {
		TXSemanticTag newtopic = null;
		try {
			newtopic = tx.createTXSemanticTag(name, si);

		} catch (SharkKBException e) {
			e.printStackTrace();
		}
		return newtopic;
	}

	@Override
	public void addInterest(TXSemanticTag interest) {
		try {
			tx.merge(interest);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public TXSemanticTag getTopicAsSemanticTag(String si) {
		TXSemanticTag topic = null;
		try {
			topic = tx.getSemanticTag(si);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}

		return topic;
	}

	@Override
	public void deleteInterest(TXSemanticTag i) {
		try {
			tx.removeSemanticTag(i);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void moveInterest(TXSemanticTag parent, TXSemanticTag child) {
		try {
			tx.move(parent, child);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<TXSemanticTag> getAllTopics() {

		Enumeration<TXSemanticTag> enum_Tag = null;
		try {
			enum_Tag = tx.rootTags();
		} catch (SharkKBException e) {
			e.printStackTrace();
		}

		List<TXSemanticTag> subtag_list = new LinkedList<>();
		while (enum_Tag.hasMoreElements()){
			TXSemanticTag tag = enum_Tag.nextElement();
			subtag_list.add(tag);
			List<TXSemanticTag> subsubtag_list = getSubTags(tag);
			if(!subsubtag_list.isEmpty()){
				subtag_list.addAll(subsubtag_list);
			}
		}
		return subtag_list;
	}

	private List<TXSemanticTag> getSubTags(TXSemanticTag root){
		Enumeration<TXSemanticTag> subtagenum =root.getSubTags();
		List <TXSemanticTag> subtag_List = new LinkedList<>();
		if(subtagenum == null){
			return subtag_List;
		}
		while(subtagenum.hasMoreElements()){
			TXSemanticTag txtag = subtagenum.nextElement();
			subtag_List.add(txtag);
			List<TXSemanticTag> subsubtag_list = getSubTags(txtag);
			if(!subsubtag_list.isEmpty()){
				subtag_List.addAll(subsubtag_list);
			}
		}
		return subtag_List;
	}

	@Override
	public void save() {
		//ToDo: Shark - safe Interest in KB
	}

	@Override
	public void delete() {
		//ToDo: Shark - delete Interest from KB
	}

	//ToDo: Implemenmt "is private"
	//ToDo: Implement direction
}
