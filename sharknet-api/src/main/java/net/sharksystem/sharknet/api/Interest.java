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
	/**
	 * Returns Taxonomy of Interests (a Taxonomy is a loss collection of the SemanticTags)
	 * @return
     */
	public Taxonomy getInterests();

	/**
	 * Adds Interest to the Taxonomy with a Name and the subjectidentifier
	 * @param name
	 * @param si
     * @return
     */
	public TXSemanticTag addInterest(String name, String si);

	/**
	 * Add Interest by a TXSemanticTag (used for adding interest in feed)
	 * @param interest
     */
	public void addInterest(TXSemanticTag interest);

	/**
	 * Returns the TXSemanticTag of a Topic identified by the subject identifier
	 * @param si
	 * @return
     */
	public TXSemanticTag getTopicAsSemanticTag(String si);

	/**
	 * Removes the Interest from the Taxonomy
	 * @param i
     */
	public void deleteInterest(TXSemanticTag i);

	/**
	 * Moves a child interest under a parent interest
	 * @param parent
	 * @param child
     */
	public void moveInterest(TXSemanticTag parent, TXSemanticTag child);

	/**
	 * Evaluates if the Interest contains at least all Interests of i
	 * @param i
	 * @return
     */
	public boolean contains(Interest i);

	/**
	 * Returns a List of All Topics as a List within the Taxonomy
	 * @return
     */
	public List<TXSemanticTag> getAllTopics();

	/**
	 * Writes the Taxonomy specified in the Interest in the DB
	 */
	public void save();

	/**
	 * Deletes the Taxonomy specified in the Interest from the DB
	 */
	public void delete();

	/**
	 * Removes a TXSemanticTag from Parents and takes childs with him
	 * @param child
     */
	public void removeFromParent(TXSemanticTag child);

}
