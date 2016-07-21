package net.sharksystem.sharknet.javafx.controller.profile;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import net.sharksystem.sharknet.api.Contact;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.i18n.I18N;
import org.apache.commons.lang3.NotImplementedException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static net.sharksystem.sharknet.javafx.i18n.I18N.getString;

/**
 * @Author Yves Kaufmann
 * @since 05.07.2016
 */
public class PublicKeyController {

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	@FXML private Label publicKeyField;
	@FXML private Label expirationField;
	@FXML private Button generateNewPublicKey;

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	@Inject private SharkNet sharkNet;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public void initialize() {
		generateNewPublicKey.setOnAction((e) -> onGenerateNewKeyClicked());
		refreshData();
	}

	private void refreshData() {
		Profile myProfile = sharkNet.getMyProfile();
		Contact contact = myProfile.getContact();
		String key = contact.getPublicKeyFingerprint();
		Date keyExpiration = contact.getPublicKeyExpiration();
		publicKeyField.setText(key);
		expirationField.setText(getString("profile.publicKey.expiration", keyExpiration));
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void onGenerateNewKeyClicked() {

		Alert shouldPublicKeyGenerated = new Alert(Alert.AlertType.CONFIRMATION);
		shouldPublicKeyGenerated.setHeaderText(getString("profile.publicKey.generate.dlg.title"));
		shouldPublicKeyGenerated.setContentText(getString("profile.publicKey.generate.dlg.confirmation"));
		shouldPublicKeyGenerated.initOwner(generateNewPublicKey.getScene().getWindow());

		shouldPublicKeyGenerated.getDialogPane().getStylesheets().add(App.getAppStyleSheet());
		shouldPublicKeyGenerated.getDialogPane().getStyleClass().add("theme-presets");
		shouldPublicKeyGenerated.getDialogPane().getScene().getWindow().sizeToScene();
		shouldPublicKeyGenerated.showAndWait().ifPresent((buttonType) -> {
			if (ButtonType.OK.equals(buttonType)) {
				Profile myProfile = sharkNet.getMyProfile();
				myProfile.renewKeys();
				refreshData();
			}
		});
	}
}
