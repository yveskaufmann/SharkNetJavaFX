package net.sharksystem.sharknet.api;

import net.sharkfw.knowledgeBase.TXSemanticTag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @Author Yves Kaufmann
 * @since 07.07.2016
 */
public class ImplInterestTest {

	private static Comparator<? super TXSemanticTag> TX_SEMANTIC_TAG_COMPARATOR = new Comparator<TXSemanticTag>() {
		@Override
		public int compare(TXSemanticTag o1, TXSemanticTag o2) {
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	};

	private ImplSharkNet emptySharkNet;

	@Before
	public void loadSharkNet() {
		emptySharkNet = new ImplSharkNet();
		Profile piggeldy = 	emptySharkNet.newProfile("Piggeldy", "Frederick");
		piggeldy.setPassword("");
		emptySharkNet.setProfile(piggeldy, "");
	}

	@Test
	public void getAllTopics_OnEmptyInterest() throws Exception {
		Contact contact = emptySharkNet.getMyProfile().getContact();
		Interest interest = contact.getInterests();

		List<TXSemanticTag> topicList = interest.getAllTopics();
		Assert.assertNotNull(topicList);
		Assert.assertEquals(0, topicList.size());
	}

	@Test
	public void getAllTopics_OnExampleInterests() throws Exception {
		Contact contact = emptySharkNet.getMyProfile().getContact();
		Interest interest = contact.getInterests();

		String[][] dummyInterests = {
			{"Sport", "https://de.wikipedia.org/wiki/Sport"},
			{"Musik", "https://de.wikipedia.org/wiki/Musik"},
			{"Literatur", "https://de.wikipedia.org/wiki/Literatur"},
		};

		String[][] sportsTopics = {
			{"Fußball", "https://de.wikipedia.org/wiki/Fußball"},
			{"Handball", "https://de.wikipedia.org/wiki/Handball"},
			{"Turmspringen", "https://de.wikipedia.org/wiki/Turmspringen"},
		};

		List<TXSemanticTag> expectedFlatTagList = new ArrayList<>();
		for (int i = 0; i < dummyInterests.length; i++) {
			TXSemanticTag parentTag = interest.addInterest(dummyInterests[i][0], dummyInterests[i][1]);
			expectedFlatTagList.add(parentTag);
			if (i == 0) {
				for (String[] child : sportsTopics) {
					TXSemanticTag childTag = interest.addInterest(child[0], child[1]);
					childTag.move(parentTag);
					expectedFlatTagList.add(childTag);
				}
			}
		}

		List<TXSemanticTag> actualTagList = interest.getAllTopics();
		Assert.assertNotNull(actualTagList);

		//  tags are not returned in order they are created
		Collections.sort(actualTagList, TX_SEMANTIC_TAG_COMPARATOR);
		Collections.sort(expectedFlatTagList, TX_SEMANTIC_TAG_COMPARATOR);
		assertThat(actualTagList, is(expectedFlatTagList));


	}

}
