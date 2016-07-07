package net.sharksystem.sharknet.javafx.interests;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import net.sharkfw.knowledgeBase.TXSemanticTag;
import net.sharkfw.knowledgeBase.Taxonomy;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Interest;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.modules.SharkNetModule;
import net.sharksystem.sharknet.javafx.utils.Enumerations;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertNotNull;

/**
 * @Author Yves Kaufmann
 * @since 05.07.2016
 */
@GuiceModules(SharkNetModule.class)
@RunWith(GuiceTestRunner.class)
public class InterestsTraversingTest {

	@Inject private SharkNet sharkNet;

	@Test
	public void readInterestsAsFlatList() throws Exception {
		Profile profile = sharkNet.getMyProfile();
		assertNotNull(profile);
		Contact contact = profile.getContact();
		assertNotNull(contact);

		Interest interest = contact.getInterests();
		assertNotNull(interest);

		Taxonomy taxonomy = interest.getInterests();
		assertNotNull(taxonomy);

		TXSemanticTag rootTag = taxonomy.createTXSemanticTag("root", "papa");
		for (int i = 0; i < 10; i++) {
			interest.addInterest("c " + i, "c" + i);
		}

		// Root Tags returns only these nodes which have childs
		Iterable<TXSemanticTag> rootTagsIt = Enumerations.asIterable(taxonomy.rootTags());

		List<TXSemanticTag> tags = StreamSupport
			.stream(rootTagsIt.spliterator(), true)
			.sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
			.flatMap((tag) -> {
				List <TXSemanticTag> txTags = new ArrayList<>();
				txTags.add(tag);
				return StreamSupport
					.stream(Enumerations.asIterable(tag.getSubTags()).spliterator(), true)
					.collect(Collectors.toCollection(() -> txTags)).stream();
			}).collect(Collectors.toList());


		for (TXSemanticTag tag : tags) {
			System.out.println("Name " + tag.getName());
		}

	}
}
