package net.sharksystem.sharknet.javafx.controller.inbox;

import com.google.inject.Inject;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.text.TextFlow;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import net.sharkfw.knowledgeBase.TXSemanticTag;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.context.ApplicationContext;
import net.sharksystem.sharknet.javafx.context.Context;
import net.sharksystem.sharknet.javafx.controller.contactlist.ContactController;
import net.sharksystem.sharknet.javafx.controls.*;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCellController;
import net.sharksystem.sharknet.javafx.i18n.I18N;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;
import net.sharksystem.sharknet.javafx.utils.NodeUtils;
import net.sharksystem.sharknet.javafx.utils.TimeUtils;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import org.apache.commons.lang3.NotImplementedException;
import org.controlsfx.control.textfield.TextFields;

import java.util.List;

import static net.sharksystem.sharknet.javafx.i18n.I18N.getString;

public class InboxEntryController extends MediaListCellController<Feed> {

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	@Inject private SharkNet sharkNet;
	@Inject private ImageManager imageManager;
	private FontIcon unlikeIcon = new FontIcon(FontAwesomeIcon.THUMBS_DOWN);
	private FontIcon unlikedIcon = new FontIcon(FontAwesomeIcon.THUMBS_O_DOWN);
	private FontIcon commentsIcon = new FontIcon(FontAwesomeIcon.COMMENT_O);
	private FontIcon commentedIcon = new FontIcon(FontAwesomeIcon.COMMENT);
	private FontIcon interestTagIcon = new FontIcon(FontAwesomeIcon.TAGS);
	private ContextMenu contextMenu;
	private AnchorPane commentsEditor;



	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	@FXML private RoundImageView profileImage;
	@FXML private Label feedSenderName;
	@FXML private Label feedReceiveDate;
	@FXML private Button feedMenuButton;
	@FXML private Label unlikeButton;
	@FXML private Label commentButton;
	@FXML private Label feedInterstsButton;
	@FXML private Text feedContent;
	@FXML private AnchorPane feedAnchorPane;
	@FXML private VBox commentsList;
	@FXML private FlowPane feedInterests;


	/******************************************************************************
	 *                                                                             
	 * Construction & Initialization
	 *                                                                              
	 ******************************************************************************/
	
	public InboxEntryController(MediaListCell<Feed> inboxListCell) {
		super(App.class.getResource("views/inbox/inboxEntry.fxml"), inboxListCell);
	}

	@Override
	protected void onFxmlLoaded() {

		contextMenu = createContextMenu();
		feedMenuButton.setText(FontAwesomeIcon.ANGLE_DOWN.getText());
		feedMenuButton.getStyleClass().addAll("icon-label", "text-dark");
		feedMenuButton.setOnAction((e) -> contextMenu.show(feedMenuButton, Side.BOTTOM, 0, 0));

		commentButton.setStyle("-fx-font-weight: bold");
		commentButton.setCursor(Cursor.HAND);

		unlikeButton.setStyle("-fx-font-weight: bold");
		unlikeButton.setCursor(Cursor.HAND);

		feedInterstsButton.setStyle("-fx-font-weight: bold");
		feedInterstsButton.setCursor(Cursor.HAND);

		commentButton.setOnMouseClicked(this::onCommentsToggle);
		unlikeButton.setOnMouseClicked(this::onFeedDislikeClicked);
		feedSenderName.setOnMouseClicked(this::onSenderClicked);
		feedInterstsButton.setOnMouseClicked(this::onInterestTagsClicked);

		feedInterests.getParent().visibleProperty().bind(feedInterests.visibleProperty());
		feedInterests.getParent().managedProperty().bind(feedInterests.managedProperty());
		feedInterests.managedProperty().bind(feedInterests.visibleProperty());
	}

	private void onInterestTagsClicked(MouseEvent mouseEvent) {
		final Feed feed = getItem();
		if (feedInterests.isVisible() || feed == null) {
			feedInterests.getChildren().clear();
			feedInterests.setVisible(false);
		} else {
			final Interest interest = feed.getInterest();
			for (TXSemanticTag interestTag : interest.getAllTopics()) {
				Label tag = new Label(interestTag.getName());
				tag.setContentDisplay(ContentDisplay.RIGHT);
				tag.getStyleClass().add("interest-tag");
				tag.setCursor(Cursor.HAND);
				tag.setOnMouseClicked((e) -> {
					String si[] = interestTag.getSI();
					if (si.length > 0) {
						ApplicationContext.get()
							.getApplication().getHostServices()
							.showDocument(si[0]);
					}
				});
				feedInterests.getChildren().add(tag);
			}
			feedInterests.setVisible(true);
		}
	}

	private void reloadCommentsFeed(Feed feed) {
		List<Comment> comments = feed.getComments(true);
		commentsList.getChildren().remove(1, commentsList.getChildren().size());

		if (!comments.isEmpty()) {
			Separator separator = new Separator(Orientation.HORIZONTAL);
			commentsEditor.getChildren().add(separator);
			AnchorPane.setLeftAnchor(separator, 0.0);
			AnchorPane.setRightAnchor(separator, 0.0);
			AnchorPane.setBottomAnchor(separator, -8.0);
		}

		for(int i =0 ; i < comments.size(); i++) {
            Comment comment = comments.get(i);
			Contact sender = comment.getSender();
			final RoundImageView commentSenderImage = new RoundImageView();
            commentSenderImage.setFitWidth(40);
            commentSenderImage.setFitHeight(40);

			final AnchorPane commentsEntry = new AnchorPane();
			final Label userNameLabel = new Label(sender.getNickname());
			userNameLabel.getStyleClass().add("sender-name");
			userNameLabel.setUserData(sender);
			userNameLabel.setOnMouseClicked(this::onSenderClicked);


			final Label timeLabel = new Label(TimeUtils.formatTimeAgo(comment.getTimestamp()));
			final VBox nameTimeWrapper = new VBox(5, userNameLabel, timeLabel);
			final Text commentMessage = new Text(comment.getContent().getMessage());
			final TextFlow textFlow = new TextFlow(nameTimeWrapper, commentMessage);

			// Ensures that the text is wrapped at the end of the comment entry box
			textFlow.maxWidthProperty().bind(commentsList.widthProperty().subtract(commentSenderImage.fitWidthProperty()).subtract(10));

			AnchorPane.setTopAnchor(commentSenderImage, 5.0);
			AnchorPane.setLeftAnchor(commentSenderImage, 5.0);

			AnchorPane.setTopAnchor(textFlow, 5.0);
			AnchorPane.setLeftAnchor(textFlow, commentSenderImage.getFitWidth() + 5);

			commentsEntry.getChildren().addAll(commentSenderImage,textFlow);


			if (i < comments.size() - 1) {
				final Separator separator = new Separator(Orientation.HORIZONTAL);
				AnchorPane.setLeftAnchor(separator, 0.0);
				AnchorPane.setRightAnchor(separator, 0.0);
				AnchorPane.setBottomAnchor(separator, -8.0);
				commentsEntry.getChildren().add(separator);
            }

            imageManager.readImageFrom(comment.getSender().getPicture())
                .ifPresent(commentSenderImage::setImage);

            commentsList.getChildren().add(commentsEntry);
        }
	}

	private ContextMenu createContextMenu() {
		MenuItem hideMenu = new MenuItem(getString("%feed.deletion"));
		MenuItem showFeedInfo = new MenuItem(getString("%feed.info"));

		hideMenu.setOnAction((e) -> {
			Feed feed = getItem();
			if (feed != null) {
				feed.delete();
				if (cell.getListView() != null) {
					cell.getListView().getItems().remove(feed);
				}
			}
		});

		showFeedInfo.setOnAction((e) -> {
			throw new NotImplementedException("not yet implemented");
		});

		return new ContextMenu(
			hideMenu,
			showFeedInfo
		);
	}


	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	@Override
	protected void onItemChanged(Feed feed) {
		profileImage.setImage(null);
		feedSenderName.setText("");
		feedSenderName.setUserData(null);
		feedReceiveDate.setText("");
		feedContent.setText("");
		unlikeButton.setGraphic(null);
		commentButton.setGraphic(null);
		feedInterests.getChildren().clear();
		feedInterests.setVisible(false);

		if (feed == null) return;

		final Contact sender = feed.getSender();
		imageManager.readImageFrom(sender.getPicture()).ifPresent(profileImage::setImage);
		feedSenderName.setText(sender.getNickname());
		feedSenderName.setUserData(sender);
		feedContent.setText(feed.getContent().getMessage());
		feedReceiveDate.setText(TimeUtils.formatTimeAgo(feed.getTimestamp()));

		double textSize = NodeUtils.computeTextHeight(feedContent.getFont(), feedContent.getText(), feedContent.getWrappingWidth(), TextBoundsType.VISUAL);
		feedAnchorPane.setPrefHeight(textSize + feedAnchorPane.getMinHeight());

		updateLikeButton(feed);
		updateCommentsButton(feed);
		feedInterstsButton.setGraphic(interestTagIcon);


	}

	private void updateCommentsButton(Feed feed) {
		if (feed.getComments(false).isEmpty()) {
			commentButton.setGraphic(commentsIcon);
		} else {
			commentButton.setGraphic(commentedIcon);
		}
	}

	private void updateLikeButton(Feed feed) {
		if (feed.isDisliked()) {
			unlikeButton.setGraphic(unlikeIcon);
		} else {
			unlikeButton.setGraphic(unlikedIcon);
		}
	}

	/******************************************************************************
	 *
	 * Events Handling
	 *
	 ******************************************************************************/

	private void onSenderClicked(MouseEvent e) {
		final Controllers controllers = Controllers.getInstance();
		final ContactController contactController = controllers.get(ContactController.class);
		final Node node = (Node) e.getSource();

		final Object senderContact = node.getUserData();
		if (senderContact instanceof Contact) {
			contactController.showContact((Contact) senderContact, this::onResume);
		}
	}

	private void onFeedDislikeClicked(MouseEvent e) {
		Feed feed = getItem();
		if (feed != null) {
			feed.setDisliked(!feed.isDisliked());
			updateLikeButton(feed);
		}
	}

	private void onCommentsToggle(MouseEvent mouseEvent) {
		Feed feed = getItem();
		List<Comment> comments = feed.getComments(true);
		boolean hasComments = comments.size() > 0;
		commentsList.setVisible(! commentsList.isVisible());
		commentsList.setManaged(commentsList.isVisible());
		if (commentsList.isVisible()) {
			commentsList.setMaxWidth(feedAnchorPane.getWidth() * 0.9);
			commentsList.getChildren().clear();

			TextField messageTextfield = TextFields.createClearableTextField();
			messageTextfield.setPromptText("Kommentieren ...");
			RoundImageView commentsComposerImage = new RoundImageView();
			commentsComposerImage.setFitWidth(30);
			commentsComposerImage.setFitHeight(30);
			Content profileImage = sharkNet.getMyProfile().getContact().getPicture();

			commentsEditor = new AnchorPane(
				commentsComposerImage,
				messageTextfield
			);

			commentsEditor.setPadding(new Insets(0, 0, 10, 0));
			AnchorPane.setTopAnchor(commentsComposerImage, 12.0);
			AnchorPane.setLeftAnchor(commentsComposerImage, 12.0);
			AnchorPane.setTopAnchor(messageTextfield, 12.0);
			AnchorPane.setLeftAnchor(messageTextfield, 12.0 + commentsComposerImage.getFitWidth() + 5);
			AnchorPane.setRightAnchor(messageTextfield, 12.0);

			messageTextfield.setOnKeyPressed((e) -> {
				if (KeyCode.ENTER.equals(e.getCode())) {
					String comment = messageTextfield.getText().trim();
					if (!"".equals(comment)) {
						messageTextfield.clear();
						Profile profile = sharkNet.getMyProfile();
						feed.newComment(new ImplContent(comment, profile), profile.getContact());
						reloadCommentsFeed(feed);
						updateCommentsButton(feed);
					}
				}
			});

			imageManager.readImageFrom(profileImage).ifPresent(commentsComposerImage::setImage);
			commentsList.getChildren().add(commentsEditor);
			reloadCommentsFeed(feed);
		}

	}
}
