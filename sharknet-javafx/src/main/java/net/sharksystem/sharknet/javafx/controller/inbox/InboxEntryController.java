package net.sharksystem.sharknet.javafx.controller.inbox;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.api.Feed;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.utils.AbstractController;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;

import java.text.SimpleDateFormat;

public class InboxEntryController extends AbstractController {

	private  InboxListCell listCell;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat();

	@FXML
	private GridPane container;

	@FXML
	private RoundImageView contactImage;

	@FXML
	private Text contactName;

	@FXML
	private Text receiveDate;

	@FXML
	private Text feedContent;

	@FXML
	private Button verificationButton;

	private ObjectProperty<Feed> feed;


	public InboxEntryController(InboxListCell inboxListCell) {
		super(App.class.getResource("views/inbox/inboxEntry.fxml"));
		this.listCell = inboxListCell;
	}

	@Override
	protected void onFxmlLoaded() {
		verificationButton.setText(FontAwesomeIcon.KEY.getText());
		listCell.widthProperty().addListener((observable, oldValue, newValue) -> {
            getRoot().prefWidth(newValue.doubleValue());
        });

	}

	public ObjectProperty<Feed> feedProperty() {
		if (feed == null) {
			feed = new SimpleObjectProperty<Feed>(this, "feed") {
				@Override
				protected void invalidated() {
					updateView();
				}
			};
		}
		return feed;
	}

	public Feed getFeed() {
		return feed != null ? feed.get() : null;
	}

	public void setFeed(Feed feed) {
		feedProperty().set(feed);
	}

	private void updateView() {
		contactImage.setImage(new Image(App.class.getResource("images/profile-placeholder.jpg").toExternalForm()));
		contactName.setText("");
		receiveDate.setText("");
		feedContent.setText("");

		Feed feed = feedProperty().get();
		if (feed == null) return;

		contactName.setText(feed.getSender().getNickname());
		feedContent.setText(feed.getContent());

		java.sql.Timestamp timestamp = feed.getTimestamp();
		receiveDate.setText(dateFormat.format(timestamp));
	}

}
