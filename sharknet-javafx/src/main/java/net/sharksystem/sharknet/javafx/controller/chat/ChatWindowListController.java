package net.sharksystem.sharknet.javafx.controller.chat;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import net.sharksystem.sharknet.api.Message;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCellController;

import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by Benni on 04.06.2016.
 */
public class ChatWindowListController extends MediaListCellController<Message> {

	@FXML
	private Label labelMessage;
	@FXML
	private Label labelTime;
	@FXML
	private ImageView imageViewEncrypted;
	@FXML
	private ImageView imageViewSigned;
	@FXML
	private HBox hboxMessage;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm");

	public ChatWindowListController(MediaListCell<Message> chatHistoryListCell) {
		super(App.class.getResource("views/chat/chatWindowEntry.fxml"), chatHistoryListCell);

	}

	@Override
	protected void onItemChanged(Message message) {
		if (message == null) {
			return;
		}

		labelMessage.setText("<" + message.getSender().getNickname() + ">" + " " + message.getContent().getMessage());
		java.sql.Timestamp timestamp = message.getTimestamp();
		labelTime.setText(dateFormat.format(timestamp));

		if (!message.isEncrypted()) {
			imageViewEncrypted.setOpacity(0.25);
		}
		if (!message.isSigned()) {
			imageViewSigned.setOpacity(0.25);
		}

		//hboxMessage.setAlignment(Pos.TOP_RIGHT);

	}

	@Override
	protected void onFxmlLoaded() {

	}
}
