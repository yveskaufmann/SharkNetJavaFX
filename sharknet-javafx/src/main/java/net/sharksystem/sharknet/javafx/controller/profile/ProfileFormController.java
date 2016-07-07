package net.sharksystem.sharknet.javafx.controller.profile;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.context.ApplicationContext;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.regex.Pattern;

/**
 * @Author Yves Kaufmann
 * @since 05.07.2016
 */
public class ProfileFormController  {

	/******************************************************************************
	 *
	 * Constants
	 *
	 ******************************************************************************/
	private final static Logger Log = LoggerFactory.getLogger(ProfileController.class);
	private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	@FXML private TextField nicknameTextfield;
	@FXML private TextField realnameTextfield;
	@FXML private TextField emailTextfield;
	@FXML private TextArea userInfoTextfield;
	@FXML private Button cancelButton;
	@FXML private Button saveButton;

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	@Inject private SharkNet sharkNet;
	@Inject private ImageManager imageManager;
	private ValidationSupport profileFormValidation;

	/******************************************************************************
	 *
	 * Constructor
	 *
	 ******************************************************************************/

	public ProfileFormController() {
		// ApplicationContext.get().getInjector().injectMembers(this);
	}

	public void initialize() {
		profileFormValidation = new ValidationSupport();
		profileFormValidation.registerValidator(nicknameTextfield, Validator.createEmptyValidator("Ein Nickname ist erforderlich"));
		profileFormValidation.registerValidator(emailTextfield, Validator.createRegexValidator("", Pattern.compile(EMAIL_PATTERN), Severity.ERROR));

		profileFormValidation.validationResultProperty().addListener((observable, oldValue, newValue) -> {
			saveButton.setDisable(profileFormValidation.isInvalid());
		});

		loadData();
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	public void loadData() {
		Profile profile = sharkNet.getMyProfile();
		Contact contact = profile.getContact();

		nicknameTextfield.setText(contact.getNickname());
		realnameTextfield.setText(contact.getName());
		emailTextfield.setText(contact.getEmail());
		userInfoTextfield.setText(contact.getNote());
	}

	/******************************************************************************
	 *
	 * Event handlers
	 *
	 ******************************************************************************/

	@FXML
	void onSaveProfile(ActionEvent event) {
		if(!profileFormValidation.isInvalid()) {
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
	void onResetProfile(ActionEvent event) {
		Log.debug("Reset Profile");
		loadData();
	}
}
