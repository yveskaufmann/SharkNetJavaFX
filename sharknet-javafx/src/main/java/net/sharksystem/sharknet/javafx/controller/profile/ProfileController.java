package net.sharksystem.sharknet.javafx.controller.profile;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.controller.interest.InterestsController;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.controls.dialogs.ImageChooserDialog;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class ProfileController extends AbstractController {

	private final static Logger Log = LoggerFactory.getLogger(ProfileController.class);

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	/* Profile image fields */
	@FXML private RoundImageView profileImageView;
	@FXML private Label nameLabel;
	@FXML private Label nicknameLabel;

	@FXML private ProfileFormController profileFormController;
	@FXML private ChangePasswordController changePasswordController;
	@FXML private PublicKeyController publicKeyController;
	@FXML private InterestsController interestsController;


	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	@Inject private ImageManager imageManager;
	@Inject private SharkNet sharkNet;

	private FrontController frontController;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public ProfileController() {
		super(App.class.getResource("views/profile/profileTabPane.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		loadData();
	}


	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void loadData() {
		Profile profile = getCurrentProfile();
		Contact contact = profile.getContact();
		imageManager.readImageFrom(contact.getPicture()).ifPresent(profileImageView::setImage);
	}

	private Profile getCurrentProfile() {
		return sharkNet.getMyProfile();
	}

	private Contact getCurrentContact() {
		return getCurrentProfile().getContact();
	}


	/******************************************************************************
	 *
	 * Event Handling & Key Handling
	 *
	 ******************************************************************************/

	private void onImageChange(MouseEvent e) {
		ImageChooserDialog imageChooserDialog = new ImageChooserDialog(null);
		Optional<Image> newImage = imageChooserDialog.showAndWait();
		if (newImage.isPresent()) {
			profileImageView.setImage(newImage.get());
			// TODO: wait for save image to content
			Log.info("change profile image of {0}", getCurrentContact().getName());
		}
		e.consume();
	}

}
