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
import net.sharksystem.sharknet.javafx.i18n.I18N;
import org.apache.commons.lang3.NotImplementedException;

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
		Profile myProfile = sharkNet.getMyProfile();
		Contact contact = myProfile.getContact();
		String key = contact.getPublicKey();
		Date keyExpiration = contact.getPublicKeyExpiration();

		// TODO: request public key and date
		if (key == null || "".equals(key)) key = "5b:6b:4a:b9:18:f7:bd:03:ef:e4:d0:93:6b:10:cc:99";
		if (keyExpiration == null) keyExpiration = new Date();

		publicKeyField.setText(key);
		expirationField.setText(getString("profile.publicKey.expiration", keyExpiration));

		generateNewPublicKey.setOnAction((e) -> onGenerateNewKeyClicked(myProfile));
	}



	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void onGenerateNewKeyClicked(Profile myProfile) {
		Alert shouldPublicKeyGenerated = new Alert(Alert.AlertType.INFORMATION);
		shouldPublicKeyGenerated.setContentText(getString("%profile.publicKey.generate.dlg.confirmation"));
		shouldPublicKeyGenerated.getDialogPane().getStyleClass().add("theme-preset");
		shouldPublicKeyGenerated.initOwner(generateNewPublicKey.getScene().getWindow());
		shouldPublicKeyGenerated.showAndWait().ifPresent((buttonType) -> {
			if (ButtonType.OK.equals(buttonType)) {
				// TODO: add a progress bar but first the api must provide a specific interface
				myProfile.renewKeys();
			}
		});
	}
}
