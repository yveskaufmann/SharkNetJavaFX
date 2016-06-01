package net.sharksystem.sharknet.javafx.controller.inbox;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.api.Feed;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCellController;
import net.sharksystem.sharknet.javafx.utils.AbstractController;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;

import java.text.SimpleDateFormat;

public class InboxEntryController extends MediaListCellController<Feed> {

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

	public InboxEntryController(MediaListCell<Feed> inboxListCell) {
		super(App.class.getResource("views/inbox/inboxEntry.fxml"), inboxListCell);
	}

	@Override
	protected void onFxmlLoaded() {
		verificationButton.setText(FontAwesomeIcon.KEY.getText());
		cell.widthProperty().addListener((observable, oldValue, newValue) -> {
            getRoot().prefWidth(newValue.doubleValue());
        });

	}

	@Override
	protected void onItemChanged(Feed feed) {
		contactImage.setImage(new Image(App.class.getResource("images/profile-placeholder.jpg").toExternalForm()));
		contactName.setText("");
		receiveDate.setText("");
		feedContent.setText("");

		if (feed == null) return;

		contactName.setText(feed.getSender().getNickname());
		feedContent.setText(feed.getContent());

		java.sql.Timestamp timestamp = feed.getTimestamp();
		receiveDate.setText(dateFormat.format(timestamp));
	}
}
