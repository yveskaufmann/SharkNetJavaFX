package net.sharksystem.sharknet.javafx.controller.profile;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TXSemanticTag;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Interest;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.utils.Enumerations;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @Author Yves Kaufmann
 * @since 05.07.2016
 */
public class InterestsController {

	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	@FXML private TreeTableView<TXSemanticTag> interestsTreeTable;
	@FXML private InterestEntryController interestEntryController;
	@FXML private InterestFilterController interestFilterController;

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	@Inject private SharkNet sharkNet;
	private TreeItem<TXSemanticTag> rootItem;
	private Interest interest;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public void initialize() {
		System.out.println(interestFilterController);
		rootItem = new TreeItem<>(null);
		interestsTreeTable.setRoot(rootItem);
		interestsTreeTable.setShowRoot(false);

		ContextMenu contextMenu = createContextMenu();

		TreeTableColumn<TXSemanticTag, String> nameColumn = new TreeTableColumn<>("Name");
		nameColumn.setComparator(String::compareToIgnoreCase);
		nameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getName()));
		nameColumn.setEditable(true);
		nameColumn.prefWidthProperty().bind(interestsTreeTable.widthProperty().multiply(0.8));

		interestsTreeTable.setContextMenu(contextMenu);
		interestsTreeTable.setRowFactory(this::createDragAndDropRowFactory);
		interestsTreeTable.getColumns().add(nameColumn);
		interestsTreeTable.setTableMenuButtonVisible(false);

		interestsTreeTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<TXSemanticTag>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<TXSemanticTag>> observable, TreeItem<TXSemanticTag> oldValue, TreeItem<TXSemanticTag> newValue) {
				if (newValue != null && newValue.getValue() != null) {
					interestEntryController.setInterestTag(newValue.getValue());
				}
			}
		});

		loadInterests();

	}

	private ContextMenu createContextMenu() {
		MenuItem newInterestMenuItem = new MenuItem("New");
		newInterestMenuItem.setMnemonicParsing(true);
		newInterestMenuItem.setOnAction((e) -> loadInterests());

		return new ContextMenu(newInterestMenuItem);
	}

	private TreeTableRow<TXSemanticTag> createDragAndDropRowFactory(final TreeTableView<TXSemanticTag> treeTableView) {
		final TreeTableRow<TXSemanticTag> row = new TreeTableRow<>();

		row.setOnDragDetected((e) -> {
			if (row.isEmpty()) return;

			Dragboard dragboard = row.startDragAndDrop(TransferMode.MOVE);
			dragboard.setDragView(row.snapshot(null, null));
			ClipboardContent cc = new ClipboardContent();
			cc.put(SERIALIZED_MIME_TYPE, row.getIndex());
			dragboard.setContent(cc);
			e.consume();

		});

		row.setOnDragOver((e) -> {
			Dragboard dragboard = e.getDragboard();
			if (isDropAcceptable(dragboard, row, treeTableView)) {
				e.acceptTransferModes(TransferMode.MOVE);
				e.consume();
			}
		});

		row.setOnDragDropped((e) -> {
			Dragboard dragboard = e.getDragboard();
			if (isDropAcceptable(dragboard, row, treeTableView)) {
				int index = (Integer) dragboard.getContent(SERIALIZED_MIME_TYPE);
				TreeItem<TXSemanticTag> item = treeTableView.getTreeItem(index);
				TreeItem<TXSemanticTag> target = getTarget(row, treeTableView);

				item.getParent().getChildren().remove(item);
				target.getChildren().add(item);

				// And now move the tag to its new parent target
				item.getValue().move(target.getValue());
				interest.save();
				e.setDropCompleted(true);
				treeTableView.getSelectionModel().select(item);
				e.consume();
			}
		});

		row.setOnDragEntered((e) -> {
			Dragboard dragboard = e.getDragboard();
			if (isDropAcceptable(dragboard, row, treeTableView)) {
				row.setStyle("-fx-border-color: #b2ebf2; -fx-border-width: 1px;");
				e.consume();
			}
		});

		row.setOnDragExited((e) -> {
			Dragboard dragboard = e.getDragboard();
			if (isDropAcceptable(dragboard, row, treeTableView)) {
				row.setStyle(null);
				e.consume();
			}
		});

		return row;
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private boolean isDropAcceptable(Dragboard dragboard, TreeTableRow<TXSemanticTag> row, TreeTableView<TXSemanticTag> treeTableView) {
		boolean isAcceptable = false;
		if (dragboard.hasContent(SERIALIZED_MIME_TYPE)) {
			int srcIndex = (int) dragboard.getContent(SERIALIZED_MIME_TYPE);
			if (srcIndex != row.getIndex()) {
				TreeItem<TXSemanticTag> srcItem = treeTableView.getTreeItem(srcIndex);
				TreeItem<TXSemanticTag> target = getTarget(row, treeTableView);
				isAcceptable = !isParent(srcItem, target);
			}
		}
		return isAcceptable;
	}

	private TreeItem<TXSemanticTag> getTarget(TreeTableRow<TXSemanticTag> row, TreeTableView<TXSemanticTag> treeTableView) {
		TreeItem target = treeTableView.getRoot();
		if (!row.isEmpty()) {
			target = row.getTreeItem();
		}
		return target;
	}

	private boolean isParent(TreeItem parent, TreeItem child) {
		boolean result = false;
		while (!result && child != null) {
			result = child.getParent() == parent;
			child = child.getParent();
		}
		return result;
	}


	/**
	 * Load the TreeItems from the interests taxonomy.
	 */
	public void loadInterests() {



		Profile profile = sharkNet.getMyProfile();
		Contact contact = profile.getContact();
		interest = contact.getInterests();


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

		for (int i = 0; i < dummyInterests.length; i++) {
			TXSemanticTag parentTag = interest.addInterest(dummyInterests[i][0], dummyInterests[i][1]);
			if (i == 0) {
				for(String[] child : sportsTopics) {
					interest.addInterest(child[0], child[1]).move(parentTag);
				}
			}
		}

		try {
			Iterable<TXSemanticTag> rootTagsIterator = null;
			rootTagsIterator = Enumerations.asIterable(interest.getInterests().rootTags());
			List<TXSemanticTag> rootTags = StreamSupport
				.stream(rootTagsIterator.spliterator(), true)
				.collect(Collectors.toList());

			List<TreeItem<TXSemanticTag>> treeItems = new ArrayList<>();

			for (TXSemanticTag root : rootTags) {
				TreeItem<TXSemanticTag> treeItem = new TreeItem<>(root);
				loadInterests(root, treeItem);
				treeItems.add(treeItem);
			}

			rootItem.getChildren().setAll(treeItems);
			interestsTreeTable.getSelectionModel().selectFirst();
		} catch (SharkKBException e) {
			throw new RuntimeException("Failed to load interests from shark", e);
		}
	}

	private void loadInterests(TXSemanticTag rootTags, TreeItem<TXSemanticTag> treeItem) {

		Enumeration<TXSemanticTag> txSemanticTagEnumeration = rootTags.getSubTags();

		// abort no enumeration => no child interests
		if (txSemanticTagEnumeration == null) return;

		StreamSupport
			.stream(Enumerations.asIterable(txSemanticTagEnumeration).spliterator(), false)
			.forEach((txSemanticTag -> {
				TreeItem<TXSemanticTag> item = new TreeItem<>(txSemanticTag);
				treeItem.getChildren().add(item);
				loadInterests(txSemanticTag, item);
			}));
	}
}

