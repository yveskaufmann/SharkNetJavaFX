package net.sharksystem.sharknet.javafx.controller.inbox;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.api.Feed;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCellController;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;
import net.sharksystem.sharknet.javafx.utils.TimeUtils;

public class InboxEntryController extends MediaListCellController<Feed> {

	@Inject
	private ImageManager imageManager;

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
		contactImage.setImage(null);
		contactName.setText("");
		receiveDate.setText("");
		feedContent.setText("");

		if (feed == null) return;
		imageManager.readImageFrom(feed.getSender().getPicture()).ifPresent(contactImage::setImage);
		contactName.setText(feed.getSender().getNickname());
		feedContent.setText(feed.getContent().getMessage());
		receiveDate.setText(TimeUtils.formatTimeAgo(feed.getTimestamp()));
	}
}
