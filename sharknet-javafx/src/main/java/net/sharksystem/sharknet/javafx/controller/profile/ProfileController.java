package net.sharksystem.sharknet.javafx.controller.profile;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.controls.dialogs.ImageChooserDialog;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;
import java.util.regex.Pattern;

public class ProfileController extends AbstractController {

	private final static Logger Log = LoggerFactory.getLogger(ProfileController.class);

	private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Inject
	private ImageManager imageManager;

	@Inject
	private SharkNet sharkNet;

	@FXML
	private RoundImageView profileImageView;

	@FXML
	private Label nameLabel;

	@FXML
	private Label nicknameLabel;

	@FXML
	private TextField nicknameTextfield;

	@FXML
	private TextField realnameTextfield;

	@FXML
	private TextField emailTextfield;

	@FXML
	private TextArea userInfoTextfield;

	@FXML
	private Button cancelButton;

	@FXML
	private Button saveButton;

	private FrontController frontController;
	private ValidationSupport validationSupport;

	public ProfileController() {
		super(App.class.getResource("views/profile/profileTabPane.fxml"));
		this.frontController = Controllers.getInstance().get(FrontController.class);;
	}

	/**
	 * Called when the fxml file was loaded
	 */
	@Override
	protected void onFxmlLoaded() {
		profileImageView.setOnMouseClicked(this::onImageChange);

		validationSupport = new ValidationSupport();
		validationSupport.registerValidator(nicknameTextfield, Validator.createEmptyValidator("Ein Nickname ist erforderlich"));
		validationSupport.registerValidator(emailTextfield, Validator.createRegexValidator("", Pattern.compile(EMAIL_PATTERN), Severity.ERROR));

		validationSupport.validationResultProperty().addListener((observable, oldValue, newValue) -> {
			saveButton.setDisable(validationSupport.isInvalid());
		});


		nameLabel.textProperty().bind(realnameTextfield.textProperty());
		nicknameLabel.textProperty().bind(nicknameTextfield.textProperty());

		loadData();
	}

	private void loadData() {
		Profile profile = sharkNet.getMyProfile();
		Contact contact = profile.getContact();

		imageManager.readImageFrom(contact.getPicture()).ifPresent(profileImageView::setImage);
		nicknameTextfield.setText(contact.getNickname());
		realnameTextfield.setText(contact.getName());
		emailTextfield.setText(contact.getEmail());
		userInfoTextfield.setText(contact.getNote());
	}

	private void onImageChange(MouseEvent e) {
		ImageChooserDialog imageChooserDialog = new ImageChooserDialog(null);
		Optional<Image> newImage = imageChooserDialog.showAndWait();
		if (newImage.isPresent()) {
			profileImageView.setImage(newImage.get());
			// TODO: save image into profile
		}
		e.consume();
	}

	@FXML
	void onSave(ActionEvent event) {
		if(!validationSupport.isInvalid()) {
			Log.info("Update Profile");
			Profile profile = sharkNet.getMyProfile();
			Contact contact = profile.getContact();
			contact.setNickname(nicknameTextfield.getText());
			contact.setEmail(emailTextfield.getText());
			contact.addName(realnameTextfield.getText());
			contact.addNote(userInfoTextfield.getText());
			contact.update();
		}
	}

	@FXML
	void onCancel(ActionEvent event) {
		Log.debug("Reset Profile");
		loadData();
	}

}