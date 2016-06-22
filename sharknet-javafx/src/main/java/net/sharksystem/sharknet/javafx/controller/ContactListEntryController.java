package net.sharksystem.sharknet.javafx.controller;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCell;
import net.sharksystem.sharknet.javafx.controls.medialist.MediaListCellController;
import net.sharksystem.sharknet.javafx.services.ImageManager;


public class ContactListEntryController extends MediaListCellController<Contact> {

	@Inject
	private ImageManager imageManager;

	@FXML
	private GridPane container;

	@FXML
	private RoundImageView contactImage;

	@FXML
	private Text contactName;

	public ContactListEntryController(MediaListCell<Contact> contactListCell) {
		super(App.class.getResource("views/contactListEntry.fxml"), contactListCell);
	}

	@Override
	protected void onFxmlLoaded() {
		cell.widthProperty().addListener((observable, oldValue, newValue) -> {
			getRoot().prefWidth(newValue.doubleValue());
		});
	}

	@Override
	protected void onItemChanged(Contact contact) {
		contactImage.setImage(null);
		contactName.setText("");

		if (contact == null) return;

		imageManager.readImageFrom(contact.getPicture()).ifPresent(contactImage::setImage);
		contactName.setText(contact.getNickname());
	}
}
