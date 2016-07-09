package net.sharksystem.sharknet.javafx.controller.login;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import net.sharksystem.sharknet.javafx.controls.RoundImageView;
import net.sharksystem.sharknet.javafx.services.ImageManager;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;

import static net.sharksystem.sharknet.javafx.i18n.I18N.getString;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Benni on 18.05.2016.
 */
public class LoginController extends AbstractController {


	@Inject private SharkNet sharkNetModel;
	@Inject private ImageManager imageManager;
	private FrontController frontController;
	private List<Profile> profileList;
	private int profileNumber;
	private LoginListener listener;
	private Stage stage;


	@FXML private Label labelProfileName;
	@FXML private ImageView imageViewScrollLeft;
	@FXML private ImageView imageViewScrollRight;
	@FXML private RoundImageView roundImageViewProfilePic;
	@FXML private JFXButton buttonLogin;
	@FXML private JFXButton buttonRegister;
	@FXML private PasswordField passwordfield;


	public LoginController() {
		super(App.class.getResource("views/loginView.fxml"));
		listener = null;
		Parent root = super.getRoot();
		stage = new Stage();
		stage.setTitle(getString("login.title"));
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
		buttonLogin.setText(getString("login.login"));
		buttonRegister.setText(getString("login.register"));
		imageViewScrollLeft.setOnMouseClicked(event -> {
			onScrollLeftClick();
			event.consume();
		});
		imageViewScrollRight.setOnMouseClicked(event -> {
			onScrollRightClick();
			event.consume();
		});
		buttonLogin.setOnMouseClicked(event -> {
			onLoginClick();
			event.consume();
		});
		buttonRegister.setOnMouseClicked(event -> {
			onRegisterClick();
			event.consume();
		});
		profileList = sharkNetModel.getProfiles();
		profileNumber = 0;
		onProfileChanged();
	}

	private void onScrollLeftClick() {
		if ( (profileNumber - 1) >= 0) {
			profileNumber -= 1;
			onProfileChanged();
		}
	}

	private void onScrollRightClick() {
		if ( (profileNumber + 1) < profileList.size()) {
			profileNumber += 1;
			onProfileChanged();
		}
	}

	private void onLoginClick() {
		if (sharkNetModel.getProfiles().get(profileNumber).login(passwordfield.getText())) {
			sharkNetModel.setProfile(sharkNetModel.getProfiles().get(profileNumber), passwordfield.getText());
			if (listener != null) {
				listener.onLoginSuccessful();
				stage.close();
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(getString("login.error.password.title"));
			alert.setContentText(getString("login.error.password"));
			alert.setHeaderText("");
			alert.showAndWait();
		}
	}


	private void onRegisterClick() {
		CreateProfileController cp = new CreateProfileController();
	}

	private void onProfileChanged() {
		if (profileList != null) {
			labelProfileName.setText(profileList.get(profileNumber).getContact().getNickname());
			imageManager.readImageFrom(profileList.get(profileNumber).getContact().getPicture()).ifPresent(roundImageViewProfilePic::setImage);
		}
	}

	public void setLoginListener(LoginListener listener) {
		this.listener = listener;
	}
}
