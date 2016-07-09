package net.sharksystem.sharknet.javafx.controller.interest;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.MessageBanner;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.util.stream.Collectors;

import static net.sharksystem.sharknet.javafx.i18n.I18N.getString;

public class InterestCreationDialog extends Dialog<Pair<String,String>> {

	private final CustomTextField txInterestName;
	private final CustomTextField txInterestLink;
	private final ValidationSupport validationSupport;

	public InterestCreationDialog() {
		final DialogPane dialogPane = getDialogPane();
		dialogPane.getStylesheets().add(App.getAppStyleSheet());

		ButtonType creationButtonType = new ButtonType(getString("%interest.creation.dlg.create.caption"), ButtonBar.ButtonData.OK_DONE);

		setTitle(getString("interest.creation.dlg.title"));
		dialogPane.setHeaderText(getString("interest.creation.dlg.header"));
		dialogPane.getStyleClass().add("theme-presets");
		dialogPane.getStyleClass().add("interest-dialog");
		dialogPane.getButtonTypes().add(ButtonType.CANCEL);
		dialogPane.getButtonTypes().add(creationButtonType);


		txInterestName = (CustomTextField) TextFields.createClearableTextField();
		txInterestLink = (CustomTextField) TextFields.createClearableTextField();

		MessageBanner lbMessage = new MessageBanner();

		final VBox content = new VBox(10);
		content.setAlignment(Pos.CENTER);
		content.getChildren().add(lbMessage);
		content.getChildren().add(txInterestName);
		content.getChildren().add(txInterestLink);

		dialogPane.setContent(content);

		String interestNameCaption = getString("interest.header");
		String interestLinkCaption = getString("interest.si.prompt");
		txInterestName.setPromptText(interestNameCaption);
		txInterestLink.setPromptText(interestLinkCaption);

		validationSupport = new ValidationSupport();
		Platform.runLater( () -> {
			String validationMessageId = "%validation.required.msg";
			validationSupport.registerValidator(txInterestName, Validator.createEmptyValidator(getString(validationMessageId, interestNameCaption)));
			validationSupport.registerValidator(txInterestLink, Validator.createEmptyValidator(getString(validationMessageId, interestLinkCaption)));
		});

		Button creationButton = (Button) dialogPane.lookupButton(creationButtonType);

		// Ensure dialog can only submit if input is valid
		creationButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (validationSupport.isInvalid()) {
				lbMessage.show(validationSupport);
                event.consume();
            } else {
             	lbMessage.hide();
            }

			dialogPane.getScene().getWindow().sizeToScene();
        });

		creationButton.setOnAction(actionEvent -> {
			hide();
		});

		setResultConverter(dialogButton -> dialogButton == creationButtonType ?
			new Pair<>(txInterestName.getText(), txInterestLink.getText()) : null);
	}
}
