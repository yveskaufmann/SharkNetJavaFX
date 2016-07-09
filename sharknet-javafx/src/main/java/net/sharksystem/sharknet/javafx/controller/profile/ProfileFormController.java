package net.sharksystem.sharknet.javafx.controller.profile;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.context.ApplicationContext;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.controller.Controllers;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.regex.Pattern;

import static net.sharksystem.sharknet.javafx.i18n.I18N.getString;

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
	@FXML private JFXButton removeProfileButton;

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

	}

	public void initialize() {
		profileFormValidation = new ValidationSupport();
		profileFormValidation.registerValidator(nicknameTextfield, Validator.createEmptyValidator("Ein Nickname ist erforderlich"));
		profileFormValidation.registerValidator(emailTextfield, Validator.createRegexValidator("", Pattern.compile(EMAIL_PATTERN), Severity.ERROR));

		profileFormValidation.validationResultProperty().addListener((observable, oldValue, newValue) -> {
			saveButton.setDisable(profileFormValidation.isInvalid());
		});

		removeProfileButton.setOnAction(this::onRemoveProfileClicked);

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

	private void onRemoveProfileClicked(ActionEvent event) {
		Alert deletionDialog = new Alert(Alert.AlertType.CONFIRMATION);
		deletionDialog.getDialogPane().getStyleClass().add("theme-presets");
		deletionDialog.initOwner(cancelButton.getScene().getWindow());
		deletionDialog.setHeaderText(getString("%profile.deletion.dlg.header"));
		deletionDialog.setContentText(getString("%profile.deletion.dlg.content"));
		deletionDialog.showAndWait().ifPresent((buttonType) ->  {
			if (ButtonType.OK.equals(buttonType)) {
				Log.info("Delete profile " + sharkNet.getMyProfile().getContact().getName());
				sharkNet.getMyProfile().delete();
				((App)ApplicationContext.get().getApplication()).logout();
			}
		});

	}
}
