package net.sharksystem.sharknet.javafx.controller.interest;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;
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

import static net.sharksystem.sharknet.javafx.i18n.I18N.getString;

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

	@FXML private StackPane interestsRoot;
	@FXML private TreeTableView<TXSemanticTag> interestsTreeTable;
	@FXML private InterestEntryController interestEntryController;
	@FXML private Button addInterestButton;
	@FXML private TextField interestFilter;

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
		rootItem = new TreeItem<>(null);


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

		interestsTreeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && newValue.getValue() != null) {
                interestEntryController.setInterestTag(newValue);
            }
        });
		interestsTreeTable.setShowRoot(false);
		interestsTreeTable.setRoot(rootItem);
		interestsTreeTable.getRoot().addEventHandler(TreeItem.childrenModificationEvent(), event -> {
			interestEntryController.setVisible(! interestsTreeTable.getRoot().getChildren().isEmpty());
        });

		addInterestButton.setOnAction(this::onCreateInterestButtonClicked);
		loadInterests();
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void onCreateInterestButtonClicked(ActionEvent event) {
		InterestCreationDialog dialog = new InterestCreationDialog();
		dialog.initOwner(addInterestButton.getScene().getWindow());
		dialog.showAndWait().ifPresent(param -> createInterest(param, event.getSource().equals(addInterestButton)));
	}

	private void onDeleteInterestButtonClicked(ActionEvent e) {
		TreeTableView.TreeTableViewSelectionModel<TXSemanticTag> selectionModel = interestsTreeTable.getSelectionModel();
		TreeItem<TXSemanticTag> selectedTreeItem = selectionModel.getSelectedItem();

		if (selectedTreeItem != null && selectedTreeItem != rootItem) {
			TXSemanticTag tagToDelete = selectedTreeItem.getValue();
			ButtonType deleteButtonType = new ButtonType(getString("%interest.deletion.dlg.btn"), ButtonBar.ButtonData.YES);
			Alert deleteDialog = new Alert(
				Alert.AlertType.WARNING,
				getString("%interest.deletion.dlg.header", tagToDelete.getName()),
				ButtonType.CANCEL, deleteButtonType
			);
			deleteDialog.getDialogPane().getStyleClass().add("theme-presets");
			deleteDialog.setHeaderText(getString("%interest.deletion.dlg.title"));
			deleteDialog.initOwner(interestsRoot.getScene().getWindow());
			deleteDialog.showAndWait().ifPresent((buttonType -> {
				if (buttonType == deleteButtonType) deleteInterest(selectedTreeItem);
			}));
		}
	}

	private void createInterest(Pair<String, String> interestParams, boolean addToFirstLayer) {
		interest = retrieveInterestTaxonomy();

		// Create our new interest
		TXSemanticTag txSemanticTag = interest.addInterest(interestParams.getKey(), interestParams.getValue());
		TreeItem<TXSemanticTag> interestTreeItem = new TreeItem<>(txSemanticTag);

		// Add the new item to the current selected node
		TreeTableView.TreeTableViewSelectionModel<TXSemanticTag> selectionModel = interestsTreeTable.getSelectionModel();
		TreeItem<TXSemanticTag> parentTreeItem = selectionModel.getSelectedItem();

		if (!addToFirstLayer && parentTreeItem != null && parentTreeItem.getValue() != null) {
			txSemanticTag.move(parentTreeItem.getValue());
			parentTreeItem.getChildren().add(interestTreeItem);
		} else {
			rootItem.getChildren().add(interestTreeItem);
		}

		interestTreeItem.getParent().setExpanded(true);
		selectionModel.select(interestTreeItem);

	}

	private void deleteInterest(TreeItem<TXSemanticTag> treeItemToDelete) {

		assert treeItemToDelete != rootItem;

		try {
			TXSemanticTag tag = treeItemToDelete.getValue();
			interest.getInterests().removeSubTree(tag);
			treeItemToDelete.getParent().getChildren().remove(treeItemToDelete);
		} catch (SharkKBException e) {
			e.printStackTrace();
		}
	}

	boolean isEmpty = true;
	/**
	 * Load the TreeItems from the interests taxonomy.
	 */
	public void loadInterests() {
		interest = retrieveInterestTaxonomy();

		if (isEmpty) {

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
					for (String[] child : sportsTopics) {
						interest.addInterest(child[0], child[1]).move(parentTag);
					}
				}
			}
			isEmpty = false;
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

	private Interest retrieveInterestTaxonomy() {
		Profile profile = sharkNet.getMyProfile();
		Contact contact = profile.getContact();
		return contact.getInterests();
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

	private ContextMenu createContextMenu() {
		MenuItem newInterestMenuItem = new MenuItem("Neues Interesse");
		newInterestMenuItem.setMnemonicParsing(true);
		newInterestMenuItem.setMnemonicParsing(true);
		newInterestMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		newInterestMenuItem.setOnAction(this::onCreateInterestButtonClicked);

		MenuItem refreshInterests = new MenuItem("Neuladen");
		refreshInterests.setAccelerator(new KeyCodeCombination(KeyCode.F5));
		refreshInterests.setOnAction((e) -> loadInterests());

		MenuItem deleteInterest = new MenuItem("Löschen");
		deleteInterest.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
		deleteInterest.setOnAction(this::onDeleteInterestButtonClicked);

		ContextMenu contextMenu = new ContextMenu(
			newInterestMenuItem,
			new SeparatorMenuItem(),
			refreshInterests,
			deleteInterest
		);

		return contextMenu;
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
				TreeItem<TXSemanticTag> newParent = getTarget(row, treeTableView);

				item.getParent().getChildren().remove(item);
				newParent.getChildren().add(item);

				/**
				 *  Remove this child from its Parent,
				 *
				 */
				interest.removeFromParent(item.getValue());
				try {
					item.setValue(interest.getInterests().getSemanticTag(item.getValue().getSI()));
				} catch (SharkKBException e1) {
					e1.printStackTrace();
				}
				item.getValue().move(newParent.getValue());
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

	private boolean isDropAcceptable(Dragboard dragboard, TreeTableRow<TXSemanticTag> row, TreeTableView<TXSemanticTag> treeTableView) {
		boolean isAcceptable = false;
		if (dragboard.hasContent(SERIALIZED_MIME_TYPE)) {
			int srcIndex = (int) dragboard.getContent(SERIALIZED_MIME_TYPE);
			if (srcIndex != row.getIndex()) {
				TreeItem<TXSemanticTag> srcItem = treeTableView.getTreeItem(srcIndex);
				TreeItem<TXSemanticTag> target = getTarget(row, treeTableView);
				isAcceptable = !isParent(srcItem, target) && srcItem.getParent() != target ;
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
}

