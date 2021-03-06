package net.sharksystem.sharknet.api;

import java.util.*;

/**
 * Created by timol on 22.06.2016.
 */
public class ImplVoting implements Voting{

	String question;
	HashMap<String, List<Contact>> answers = new HashMap<>();
	boolean singleqoice;

	public ImplVoting(String question, HashMap<String, List<Contact>> answers, boolean singleqoice) {
		this.question = question;
		this.answers = answers;
		this.singleqoice = singleqoice;
	}

	public ImplVoting(String question, boolean singleqoice){
		this.question = question;
		this.singleqoice = singleqoice;
	}

	@Override
	public String getQuestion() {
		return question;
	}

	@Override
	public boolean isSingleqoice() {
		return singleqoice;
	}

	@Override
	public void addAnswers(List<String> answers){
		for(String s : answers){
			this.answers.put(s, new LinkedList<Contact>());
		}
	}

	@Override
	public HashMap<String, List<Contact>> getVotings(){
		return answers;
	}

	@Override
	public HashMap<String, Contact> getAnswers(){
		HashMap<String, Contact> emptyvotes = new HashMap<>();
		Iterator it = answers.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			emptyvotes.put((String)pair.getKey(), null);
		}

		return emptyvotes;
	}

	@Override
	public boolean vote(HashMap<String, Contact> votes){
		if(isSingleqoice()){
			Collection<Contact> contactColl = votes.values();
			if(contactColl.size() > 1 ){
				return false;
			}
			if(contactColl.toArray()[0] != null){
				if(alreadyVoted((Contact)contactColl.toArray()[0])){
					return false;
				}
			}
		}
		Iterator it = votes.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			Contact c = (Contact)pair.getValue();
			String a = (String)pair.getKey();
			if(c != null) {
				if (this.answers.get(a).contains(c)) {
					return false;
				} else {
					this.answers.get(a).add(c);
				}
			}

		}
		return true;
	}

	@Override
	public boolean alreadyVoted(Contact c){
		Iterator it = answers.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			List<Contact> voted =  (List<Contact>)pair.getValue();

			if(voted != null && voted.contains(c)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void save(){
		//ToDo: Shark - Save Voting in the DB
	}

	@Override
	public void delete(){
		//ToDo: Shark - Delete Voting from DB
	}

}
