package net.sharksystem.sharknet.javafx.controller.inbox;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TXSemanticTag;
import net.sharkfw.knowledgeBase.Taxonomy;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.controls.FontIcon;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.model.StringConverters;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.services.InterestsManager;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import org.controlsfx.control.CheckModel;
import org.controlsfx.control.CheckTreeView;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller( title = "%inbox.title")
public class InboxController extends AbstractController  {

	private static final int LOADED_FEEDS_PER_PAGE = 100;
	private static final Logger Log = LoggerFactory.getLogger(InboxController.class);

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	@Inject private SharkNet sharkNet;
	@Inject private ImageManager imageManager;
	private FrontController frontController;

	/**
	 * Sort order of the feeds false means
	 * that is is ascending and true means descending.
	 */
	private boolean feedSortOrder = true;
	private int currentPage;
	private int latestFeedIndex;
	private GetEvents feedHandler;
	private Set<TXSemanticTag> tagsForNewFeed = FXCollections.observableSet();
	private Interest filterInterestObject;

	/**
	 * Determines if interest should be loaded
	 */
	private boolean isTagListDirty = true;

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	@FXML private CheckTreeView<TXSemanticTag> interestTreeTable;
	@FXML private CheckBox sortOrderCheckbox;
	@FXML private AnchorPane messageComposerPane;
	@FXML private InboxList inboxListView;
	@FXML private JFXButton sendButton;
	@FXML private TextArea feedMessageArea;
	@FXML private RoundImageView feedSenderProfile;
	@FXML private TextField interestTextField;
	@FXML private JFXButton addInterestTagButton;
	@FXML private FlowPane tagContainer;
	private AutoCompletionBinding<TXSemanticTag> autoCompletionBinding;
	private ListChangeListener<TreeItem<TXSemanticTag>> filterCheckListener;


	public InboxController() {
		super(App.class.getResource("views/inbox/inboxView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {

		Callback<TreeItem<TXSemanticTag>, ObservableValue<Boolean>> getSelectedProperty =
			item -> {
				if (item instanceof CheckBoxTreeItem<?>) {
					return ((CheckBoxTreeItem<?>)item).selectedProperty();
				}
				return null;
			};

		interestTreeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		interestTreeTable.setCellFactory(CheckBoxTreeCell.forTreeView(getSelectedProperty, new StringConverter<TreeItem<TXSemanticTag>>() {
			@Override
			public String toString(TreeItem<TXSemanticTag> object) {
				return object.getValue().getName();
			}

			@Override
			public TreeItem<TXSemanticTag> fromString(String string) {
				return null;
			}
		}));

		autoCompletionBinding = TextFields.bindAutoCompletion(
			interestTextField,
			getTagSuggestionRequestCollectionCallback(),
			StringConverters.forTXSemanticTag()
		);

		messageComposerPane.prefHeightProperty()
			.bind(tagContainer.heightProperty().add(messageComposerPane.getMinHeight()).add(5));

		registerListeners();

	}

	private void registerListeners() {
		sendButton.setOnAction(this::onSendButtonClicked);
		interestTextField.setOnKeyReleased((e) -> {
			if (KeyCode.ENTER.equals(e.getCode())) {
				addInterestTagButton.fire();
			}
		});

		feedMessageArea.setOnKeyPressed((e) -> {
			if (KeyCode.ENTER.equals(e.getCode()) && e.isControlDown()) {
				sendButton.fire();
			}
		});
		addInterestTagButton.setOnAction(this::onTagAddClicked);
		autoCompletionBinding.setOnAutoCompleted(event -> {
			TXSemanticTag completedTag = event.getCompletion();
			if (completedTag != null) {
				tagsForNewFeed.add(completedTag);
				event.consume();
			}
		});

		Log.debug("Register feed listener");
		sharkNet.addListener(sharkNet.getMyProfile(), feedHandler = new GetEventsAdapter() {
			@Override
			public void receivedFeed(Feed feed) {
				onNewReceivedFeed(feed);
			}
		});

		sortOrderCheckbox.setOnAction((e) -> loadEntries());
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	@Override
	public void onResume() {
		isTagListDirty = true;
		loadEntries();
		loadInterests();
		inboxListView.resumeUpdate();
	}

	@Override
	public void onPause() {
		inboxListView.pauseUpdate();
	}

	@PreDestroy
	protected  void onShutdown() {
		Log.debug("Unregister feed listener");
		sharkNet.removeListener(sharkNet.getMyProfile(), feedHandler);
		inboxListView.stopUpdate();
	}

	private void loadEntries() {

		imageManager.readImageFrom(sharkNet.getMyProfile().getContact().getPicture()).ifPresent(feedSenderProfile::setImage);
		inboxListView.getItems().clear();

		boolean sortOrder = sortOrderCheckbox.isSelected();
		List<Feed> feeds = null;

		// TODO: paging
		if (filterInterestObject == null) {
			feeds = sharkNet.getFeeds(0, 200, sortOrder);
		} else {
			feeds = sharkNet.getFeeds(filterInterestObject, 0, 200, sortOrder);
		}

		for(Feed feed : feeds) {
			inboxListView.getItems().add(feed);
		}
	}

	private Content createMessageContent(String message) {
		Content content = new ImplContent(message);
		return content;
	}

	private Label createTagNode(String interestToAdd) {

		FontIcon removeIcon = new FontIcon(FontAwesomeIcon.REMOVE);
		Label tag = new Label(interestToAdd);
		tag.setContentDisplay(ContentDisplay.RIGHT);
		tag.setGraphic(removeIcon);
		tag.getStyleClass().add("interest-tag");

		removeIcon.setUserData(tag);
		removeIcon.setOnMouseClicked(this::onTagRemoveClicked);

		return tag;
	}

	/**
	 * Controls if tags should be reloaded,
	 * this is the case when new tags are created by the user.
	 */

	private ObservableList<TXSemanticTag> loadedTagList = FXCollections.observableArrayList();
	private Callback<AutoCompletionBinding.ISuggestionRequest, Collection<TXSemanticTag>> getTagSuggestionRequestCollectionCallback() {
		return param -> {

			if (isTagListDirty) {
				loadedTagList.clear();
				loadedTagList.addAll(sharkNet.getMyProfile().getContact().getInterests().getAllTopics());
				isTagListDirty = false;
			}

			return loadedTagList;
		};
	}

	private void updateFilterInterestObject() {
		final CheckModel<TreeItem<TXSemanticTag>> checkModel = interestTreeTable.getCheckModel();
		// Lets keep it simple, build a interest object from the selected
		// Interests.
		if (filterInterestObject != null) {
			filterInterestObject.delete();
		}
		// This owner is for nobody its only a filter
		filterInterestObject = new ImplInterest(null);
		// Merge our interests into a new taxonomy
		final Taxonomy taxonomy = filterInterestObject.getInterests();
		for (TreeItem<TXSemanticTag> checkedTreeItem : checkModel.getCheckedItems()) {
			final TXSemanticTag semanticTag = checkedTreeItem.getValue();
			try {
				taxonomy.merge(taxonomy);
			} catch (SharkKBException e) {
				Log.error("Failed to build filterTaxonomy", e);
				return;
			}
		}
	}

	/******************************************************************************
	 *
	 * Event - Handling
	 *
	 ******************************************************************************/

	private void onSendButtonClicked(ActionEvent e) {
		String message = feedMessageArea.getText();
		if (!message.trim().isEmpty()) {
			Profile myProfile =  sharkNet.getMyProfile();
			Contact contact = myProfile.getContact();
			Content content = createMessageContent(message);

			// Build interest object which contains all interests
			Interest interest = new ImplInterest(contact);
			for (TXSemanticTag tag : tagsForNewFeed) {
				try {
					interest.getInterests().merge(tag);
				} catch (SharkKBException e1) {
					Log.error("Failed to assign feed interest tag: " + tag.getName(), e1);
				}
			}
			interest.save();
			sharkNet.newFeed(content, interest, contact);

			// reset the composer to it start state
			feedMessageArea.clear();
			tagsForNewFeed.clear();
			tagContainer.getChildren().clear();

			loadEntries();
		}
		e.consume();
	}

	private void onNewReceivedFeed(Feed feed) {
		// should be triggered
		Log.info("Received new feed from" + feed.getOwner().getContact().getName());
		loadEntries();
	}

	private void onTagAddClicked(ActionEvent e) {
		String interestToAdd = interestTextField.getText().trim();
		interestTextField.clear();
		if (!"".equals(interestToAdd)) {
			Label tag = createTagNode(interestToAdd);
			tagContainer.getChildren().add(tag);
			messageComposerPane.requestLayout();
		}
	}


	private void onTagRemoveClicked(MouseEvent mouseEvent) {
		if (mouseEvent.getSource() != null) {
			Label source = (Label) mouseEvent.getSource();
			Label tagToRemove = (Label) source.getUserData();

			if (tagToRemove != null) {
				if( tagToRemove.getUserData() instanceof TXSemanticTag) {
					/* Remove the concrete TXSemanticTag instance */
					tagsForNewFeed.remove(tagToRemove.getUserData());
				}
				tagContainer.getChildren().remove(tagToRemove);
				messageComposerPane.requestLayout();
			}
		}
	}

	private void loadInterests() {
		CheckModel<TreeItem<TXSemanticTag>> treeItemCheckModel = interestTreeTable.getCheckModel();
		if (treeItemCheckModel != null)  {
			if (filterCheckListener  != null) {
				treeItemCheckModel.getCheckedItems().removeListener(filterCheckListener);
			}
			treeItemCheckModel.getCheckedItems().addListener(filterCheckListener = new ListChangeListener<TreeItem<TXSemanticTag>>() {
				@Override
				public void onChanged(Change<? extends TreeItem<TXSemanticTag>> c) {
					System.out.println("yeah");
				}
			});
		}

		CheckBoxTreeItem<TXSemanticTag> root = (CheckBoxTreeItem<TXSemanticTag>) InterestsManager.loadInterestsAsTreeItem(sharkNet.getMyProfile(), () -> new CheckBoxTreeItem<TXSemanticTag>());
		interestTreeTable.setShowRoot(false);
		interestTreeTable.setRoot(root);
		interestTreeTable.getCheckModel().checkAll();
	}

}

