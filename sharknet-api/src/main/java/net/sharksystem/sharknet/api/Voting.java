package net.sharksystem.sharknet.api;

import java.util.HashMap;
import java.util.List;

/**
 * Created by timol on 27.06.2016.
 */
public interface Voting {

	/**
	 * Returns the Question of the Voting
	 * @return
     */
	public String getQuestion();

	/**
	 * Returns if the Voting is Singleqoice (only one Answer per Person possible)
	 * No evaluation, this has to be done in the gui
	 * @return
     */
	public boolean isSingleqoice();

	/**
	 * Adds the List of Answers to the voting
	 * @param answers
     */
	public void addAnswers(List<String> answers);

	/**
	 * Returns the Answers including the votes as a hashmap
	 * @return
     */
	public HashMap<String, List<Contact>> getVotings();

	/**
	 * Returns the Answers in a HashMap, Contact can be added to the answers to vote
	 * @return
     */
	public HashMap<String, Contact> getAnswers();

	/**
	 * add the contact to the answers they voted for
	 * @param votes
     */
	public boolean vote(HashMap<String, Contact> votes);

	/**
	 * Evaluates if the Contact already has voted
	 * @param c
	 * @return
     */
	public boolean alreadyVoted(Contact c);

	/**
	 * Saves the Voting in the Database
	 */
	public void save();

	/**
	 * Delets the Voting from the Database
	 */
	public void delete();


}
