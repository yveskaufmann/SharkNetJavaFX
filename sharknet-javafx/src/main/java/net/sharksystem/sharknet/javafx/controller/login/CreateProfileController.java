package net.sharksystem.sharknet.javafx.controller.login;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Content;
import net.sharksystem.sharknet.api.ImplContent;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import static net.sharksystem.sharknet.javafx.i18n.I18N.getString;

import java.io.*;


/**
 * Created by Benni on 27.06.2016.
 */
public class CreateProfileController extends AbstractController{

	@Inject
	private SharkNet sharkNetModel;

	@FXML
	private Button buttonSave;
	@FXML
	private TextField textFieldNick;
	@FXML
	private TextField textFieldName;
	@FXML
	private TextField textFieldMail;
	@FXML
	private TextField textFieldTel;
	@FXML
	private PasswordField passwordFieldPass;
	@FXML
	private PasswordField passwordFieldPassRepeat;
	@FXML
	private RoundImageView imageViewPicture;

	private Stage stage;
	private File profileFile;

	private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public CreateProfileController() {
		super(App.class.getResource("views/createProfileView.fxml"));
		profileFile = null;
		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle(getString("login.profile.title"));
		stage.setScene(new Scene(root, 552, 346));
		stage.getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		InputStream in = App.class.getResourceAsStream("images/shark-icon256x256.png");
		if (in != null) {
			stage.getIcons().add(new Image(in));
		}
		stage.show();
	}

	@Override
	protected void onFxmlLoaded() {
		buttonSave.setText(getString("login.profile.button.save"));
		buttonSave.setOnMouseClicked(event -> {
			onSaveClick();
			event.consume();
		});
		imageViewPicture.setOnMouseClicked(event -> {
			onPictureClick();
			event.consume();
		});
	}

	private void onPictureClick() {
		// setup and open filechooser
		FileChooser fileChooser = new FileChooser();
		// create extension filter
		FileChooser.ExtensionFilter extFilter =	new FileChooser.ExtensionFilter("Picture files", "*.jpg", "*.bmp", "*.png", "*.gif");
		fileChooser.getExtensionFilters().add(extFilter);
		Stage stage = new Stage();
		fileChooser.setTitle(getString("login.profile.picture"));
		File file = fileChooser.showOpenDialog(stage);
		// if a file is selected
		if (file != null) {
			profileFile = file;
			try {
				InputStream in = new BufferedInputStream(new FileInputStream(profileFile.getAbsolutePath()));
				imageViewPicture.setImage(new Image(in));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void onSaveClick() {
		if (textFieldNick.getText().length() == 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(getString("login.profile.error"));
			alert.setContentText(getString("login.profile.error.nick"));
			alert.setHeaderText("");
			alert.showAndWait();
			return;
		}
		// if user wants to use password, check if they are identical
		if (passwordFieldPass.getText().length() > 0 && passwordFieldPassRepeat.getText().length() > 0) {
			if (!passwordFieldPass.getText().equals(passwordFieldPassRepeat.getText())) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(getString("login.profile.error"));
				alert.setContentText(getString("login.profile.error.password"));
				alert.setHeaderText("");
				alert.showAndWait();
				return;
			}
		}
		// create new profile
		Profile p = sharkNetModel.newProfile(textFieldNick.getText(), "uid");
		// set values if set
		if (passwordFieldPass.getText().length() > 0) {
			p.setPassword(passwordFieldPass.getText());
		}
		if (textFieldName.getText().length() > 0) {
			p.getContact().addName(textFieldName.getText());
		}
		if (textFieldMail.getText().length() > 0) {
			if (textFieldMail.getText().matches(EMAIL_PATTERN)) {
				p.getContact().setEmail(textFieldMail.getText());
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(getString("login.profile.error"));
				alert.setContentText(getString("login.profile.error.mail"));
				alert.setHeaderText("");
				alert.showAndWait();
				return;
			}

		}
		if (textFieldTel.getText().length() > 0) {
			p.getContact().addTelephonnumber(textFieldTel.getText());
		}
		// profile picture
		InputStream in = null;
		if (profileFile == null) {
			in = App.class.getResourceAsStream("images/profile-placeholder.jpg");
		} else {
			try {
				in = new BufferedInputStream(new FileInputStream(profileFile.getAbsolutePath()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		// if picture is set and loaded to stream
		if (in != null) {
			// extract extension
			String extension = "jpg";
			if (profileFile != null) {
				int i = profileFile.getPath().lastIndexOf('.');
				if (i > 0) {
					extension = profileFile.getPath().substring(i + 1);
				}
				// create content for the picture
				Content pic = new ImplContent(in, extension, profileFile.getName());
				// add the picture to contact
				p.getContact().setPicture(pic);
			}
			else {
				Content pic = new ImplContent(in, extension, "profile placeholder");
				// add the picture to contact
				p.getContact().setPicture(pic);
			}
		}
		// save all profile changes
		p.save();
		stage.close();
	}
}
