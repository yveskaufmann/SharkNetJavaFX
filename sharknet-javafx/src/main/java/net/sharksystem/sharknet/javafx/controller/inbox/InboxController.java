package net.sharksystem.sharknet.javafx.controller.inbox;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Callback;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TXSemanticTag;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.controls.FontIcon;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.model.StringConverters;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.Collection;
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

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	@FXML private AnchorPane messageComposerPane;
	@FXML private InboxList inboxListView;
	@FXML private JFXButton sendButton;
	@FXML private JFXButton interestFilterButton;
	@FXML private TextArea feedMessageArea;
	@FXML private RoundImageView feedSenderProfile;
	@FXML private TextField interestTextField;
	@FXML private JFXButton addInterestTagButton;
	@FXML private FlowPane tagContainer;
	private AutoCompletionBinding<TXSemanticTag> autoCompletionBinding;


	public InboxController() {
		super(App.class.getResource("views/inbox/inboxView.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {

		autoCompletionBinding = TextFields.bindAutoCompletion(
			interestTextField,
			getTagSuggestionRequestCollectionCallback(),
			StringConverters.forTXSemanticTag()
		);

		messageComposerPane.prefHeightProperty()
			.bind(tagContainer.heightProperty().add(messageComposerPane.getMinHeight()).add(5));

		registerListeners();
		loadEntries();
	}

	private void registerListeners() {
		sendButton.setOnAction(this::onSendButtonClicked);
		interestFilterButton.setOnAction(this::onInterestFilterButton);
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
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	@Override
	public void onResume() {
		inboxListView.resumeUpdate();
	}

	@Override
	public void onPause() {
		inboxListView.pauseUpdatee();
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
		for(Feed feed : sharkNet.getFeeds(0, 200, feedSortOrder)) {
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
	private boolean isTagListDirty = true;
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

	private void onInterestFilterButton(ActionEvent event) {

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

}

