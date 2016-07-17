package net.sharksystem.sharknet.javafx.controller.contactlist;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ContactListEntryController extends MediaListCellController<Contact> {

	private static final Logger Log = LoggerFactory.getLogger(ContactListEntryController.class);

	@Inject
	private ImageManager imageManager;
	@FXML
	private GridPane container;
	@FXML
	private RoundImageView contactImage;
	@FXML
	private Text contactName;



	public ContactListEntryController(MediaListCell<Contact> contactListCell) {
		super(App.class.getResource("views/contactlist/contactListEntry.fxml"), contactListCell);
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
