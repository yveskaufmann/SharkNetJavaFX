package net.sharksystem.sharknet.javafx.controller;

import com.google.inject.Inject;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controller.chat.ChatController;
import net.sharksystem.sharknet.javafx.controller.inbox.InboxController;
import net.sharksystem.sharknet.javafx.controls.Navigation;
import net.sharksystem.sharknet.javafx.controls.dialogs.ImageChooserDialog;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import java.io.IOException;

/**
 * This controller is responsible to provide a
 * menu which leads to the timeline, the chat,
 * the contact list, the profile and so on.
 *
 **/
public class SidebarController extends AbstractController {

	@FXML
	private RoundImageView profileImage;

	@FXML
	private Label profileUsername;

	@FXML
	private Label profileEmail;

	@FXML
	private VBox sidebar;

	@FXML
	private Navigation navigation;

	@Inject
	private SharkNet sharkNet;

	@Inject
	private ImageManager imageManager;


	private FrontController frontController;


	public SidebarController(FrontController frontController) {
		super(App.class.getResource("views/sidebarView.fxml"));
		this.frontController = frontController;

	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		navigation.setOnAction((actionEntry -> {
			switch (actionEntry.getId()) {
				case "homework":   frontController.goToView(HomeworkController.class); break;
				case "inbox":   frontController.goToView(InboxController.class); break;
				case "contacts":   frontController.goToView(ContactController.class); break;
				case "chats":   frontController.goToView(ChatController.class); break;
				case "radar":   frontController.goToView(RadarController.class); break;
				case "settings":   frontController.goToView(SettingsController.class); break;
			}
		}));

		profileImage.setOnMouseClicked(this::goToProfileHandler);
		profileUsername.setOnMouseClicked(this::goToProfileHandler);
		profileEmail.setOnMouseClicked(this::goToProfileHandler);
		readProfileInformation();
	}

	private void goToProfileHandler(MouseEvent e) {
		frontController.goToView(ProfileController.class);
	}

	private void changeProfileImage(Image image) {
		// sidebarProfileImage.setImage(image);
		// TODO: save in profile
	}

	private void readProfileInformation() {
		Profile profile = sharkNet.getMyProfile();
		Contact contact = profile.getContact();
		profileUsername.setText(contact.getNickname());
		profileEmail.setText(contact.getEmail());
		imageManager.readImageFrom(profile.getContact().getPicture()).ifPresent((image -> profileImage.setImage(image)));
	}
}
