package net.sharksystem.sharknet.javafx.controller.inbox;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.context.Context;
import net.sharksystem.sharknet.javafx.controls.*;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCellController;
import net.sharksystem.sharknet.javafx.i18n.I18N;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;
import net.sharksystem.sharknet.javafx.utils.NodeUtils;
import net.sharksystem.sharknet.javafx.utils.TimeUtils;
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

		unlikeButton.setOnMouseClicked((e) -> {
			Feed feed = getItem();
			if (feed != null) {
				feed.setDisliked(!feed.isDisliked());
				updateLikeButton(feed);
			}
		});
	}

	private AnchorPane commentsEditor;
	private void onCommentsToggle(MouseEvent mouseEvent) {
		Feed feed = getItem();
		List<Comment> comments = feed.getComments(true);
		boolean hasComments = comments.size() > 0;
		// TODO: refactor me
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
			System.out.println(comment.getContent().getMessage());
			RoundImageView commentSenderImage = new RoundImageView();
            commentSenderImage.setFitWidth(40);
            commentSenderImage.setFitHeight(40);
            VBox commentsEntry = new VBox();

            commentsEntry.getChildren().add(
                new HBox(5, commentSenderImage, new Text(comment.getContent().getMessage())));

            if (i < comments.size() - 1) {
                commentsEntry.getChildren().add(new Separator(Orientation.HORIZONTAL));
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
		feedReceiveDate.setText("");
		feedContent.setText("");
		unlikeButton.setGraphic(null);
		commentButton.setGraphic(null);

		if (feed == null) return;

		imageManager.readImageFrom(feed.getSender().getPicture()).ifPresent(profileImage::setImage);
		feedSenderName.setText(feed.getSender().getNickname());
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
}
