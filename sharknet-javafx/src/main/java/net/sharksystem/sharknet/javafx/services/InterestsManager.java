package net.sharksystem.sharknet.javafx.services;

import javafx.scene.control.TreeItem;
import javafx.util.Pair;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TXSemanticTag;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Interest;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.javafx.utils.Enumerations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Helper class for loading interests.
 *
 * @Author Yves Kaufmann
 * @since 14.07.2016
 */
public class InterestsManager {

	public static Pair<TreeItem<TXSemanticTag>, Integer> loadInterestsAsTreeItem(Profile profile, Supplier<TreeItem<TXSemanticTag>> treeItemCreator) {
		final Contact contact = profile.getContact();
		final Interest interest = contact.getInterests();
		final AtomicInteger interestCount = new AtomicInteger(0);
		final TreeItem<TXSemanticTag> rootItem = treeItemCreator.get();
		try {
			List<TXSemanticTag> rootTags = getRootTags(interest);

			for (TXSemanticTag rootTag : rootTags) {
				interestCount.incrementAndGet();
				TreeItem<TXSemanticTag> treeItem = treeItemCreator.get();
				treeItem.setValue(rootTag);
				loadInterests(rootTag, treeItem, treeItemCreator, interestCount);
				rootItem.getChildren().add(treeItem);
			}

			return new Pair<>(rootItem, interestCount.get());
		} catch (SharkKBException e) {
			throw new RuntimeException("Failed to load interests from shark", e);
		}
	}

	private static void loadInterests(TXSemanticTag rootTag, TreeItem<TXSemanticTag> treeItem, Supplier<TreeItem<TXSemanticTag>> treeItemCreator, AtomicInteger interestCount) {

		Enumeration<TXSemanticTag> txSemanticTagEnumeration = rootTag.getSubTags();

		// abort no enumeration => no child interests
		if (txSemanticTagEnumeration == null) return;

		StreamSupport
			.stream(Enumerations.asIterable(txSemanticTagEnumeration).spliterator(), false)
			.forEach((txSemanticTag -> {
				interestCount.incrementAndGet();
				TreeItem<TXSemanticTag> item = treeItemCreator.get();
				item.setValue(txSemanticTag);
				treeItem.getChildren().add(item);
				loadInterests(txSemanticTag, item, treeItemCreator, interestCount);
			}));
	}

	public static List<TXSemanticTag> getRootTags(Interest interest) throws SharkKBException {
		Enumeration<TXSemanticTag> enumeration = interest.getInterests().rootTags();
		Iterable<TXSemanticTag> rootTagsIterator = rootTagsIterator = Enumerations.asIterable(enumeration);
		return StreamSupport
			.stream(rootTagsIterator.spliterator(), true)
			.collect(Collectors.toList());
	}

}
