package net.sharksystem.sharknet.api;

import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.TXSemanticTag;
import net.sharkfw.knowledgeBase.Taxonomy;

import java.util.Enumeration;
import java.util.List;

/**
 * Created by timol on 17.05.2016.
 */
public interface Interest {
	public Taxonomy getInterests();
	public TXSemanticTag addInterest(String name, String ui);
	public SemanticTag getTopicAsSemanticTag(String ui);
	public void deleteInterest(SemanticTag i);
	public void moveInterest(TXSemanticTag parent, TXSemanticTag child);
	public List<TXSemanticTag> getAllTopics();
	public void save();
	public void delete();


}
