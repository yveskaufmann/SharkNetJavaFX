package net.sharksystem.sharknet.javafx.controller.profile;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.i18n.I18N;
import org.controlsfx.validation.ValidationSupport;

import javax.inject.Inject;
import java.util.Objects;

/**
 * @Author Yves Kaufmann
 * @since 05.07.2016
 */
public class ChangePasswordController  {

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	@FXML private PasswordField oldPasswordField;
	@FXML private PasswordField newPasswordField;
	@FXML private PasswordField confirmPasswordField;
	@FXML private JFXButton changePasswordButton;

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/
	@Inject SharkNet sharkNet;
	private ValidationSupport validationSupport;

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	public void initialize() {

		final Profile profile = sharkNet.getMyProfile();
		final ChangeListener disableChangeButtonIfPasswordNotMatch  = (observable, oldValue, newValue) -> {
			changePasswordButton.setDisable(!isNewPasswordConfirmed());
		};

		newPasswordField.textProperty().addListener(disableChangeButtonIfPasswordNotMatch);
		confirmPasswordField.textProperty().addListener(disableChangeButtonIfPasswordNotMatch);
		changePasswordButton.setOnAction((e) -> {
			String newPassword = newPasswordField.getText();
			String oldPassword = oldPasswordField.getText();
			String confirmPassword = confirmPasswordField.getText();

			if (! profile.login(oldPassword)) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle(I18N.getString("profile.changePassword.title.failed"));
				alert.setContentText(I18N.getString("profile.changePassword.oldPasswordNotMatch"));
				alert.showAndWait();
				return;
			}

			if (! isNewPasswordConfirmed()) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle(I18N.getString("profile.changePassword.title.failed"));
				alert.setContentText(I18N.getString("profile.changePassword.passwordNotConfirmed"));
				alert.showAndWait();
				return;
			}

			profile.setPassword(newPassword);
			profile.update();

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle(I18N.getString("profile.changePassword.title.successful"));
			alert.setContentText(I18N.getString("profile.changePassword.successful"));
			alert.showAndWait();

		});
		resetFields();

	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void resetFields() {
		oldPasswordField.setText("");
		newPasswordField.setText("");
		resetConfirmField();
	}

	private void resetConfirmField() {
		confirmPasswordField.setText("");
	}

	private boolean isNewPasswordConfirmed() {
		String newPassword = newPasswordField.getText();
		String confirmPassword = confirmPasswordField.getText();
		return Objects.equals(newPassword, confirmPassword);
	}
}
