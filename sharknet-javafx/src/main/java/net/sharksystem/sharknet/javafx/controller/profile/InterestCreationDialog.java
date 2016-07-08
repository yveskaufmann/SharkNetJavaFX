package net.sharksystem.sharknet.javafx.controller.profile;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import static net.sharksystem.sharknet.javafx.i18n.I18N.getString;

public class InterestCreationDialog extends Dialog<Pair<String,String>> {

	private final CustomTextField txInterestName;
	private final CustomTextField txInterestLink;
	private final ValidationSupport validationSupport;

	public InterestCreationDialog() {
		final DialogPane dialogPane = getDialogPane();

		ButtonType creationButtonType = new ButtonType("Ersellen", ButtonBar.ButtonData.OK_DONE);

		setTitle(getString("profile.interestCreation.dlg.title"));
		dialogPane.setHeaderText(getString("profile.interestCreation.dlg.header"));
		dialogPane.getStyleClass().add("interest-dialog");
		dialogPane.getButtonTypes().addAll(ButtonType.CANCEL, creationButtonType);

		txInterestName = (CustomTextField) TextFields.createClearableTextField();
		txInterestLink = (CustomTextField) TextFields.createClearableTextField();

		Label lbMessage= new Label("");
		lbMessage.getStyleClass().addAll("message-banner");
		lbMessage.setVisible(false);
		lbMessage.setManaged(false);

		final VBox content = new VBox(10);
		content.getChildren().add(lbMessage);
		content.getChildren().add(txInterestName);
		content.getChildren().add(txInterestLink);

		dialogPane.setContent(content);

		String interestNameCaption = getString("profile.interestCreation.dlg.name.caption");
		String interestLinkCaption = getString("profile.interestCreation.dlg.webpage.caption");
		txInterestName.setPromptText(interestNameCaption);
		txInterestLink.setPromptText(interestLinkCaption);

		validationSupport = new ValidationSupport();
		Platform.runLater( () -> {
			String validationMessageId = "validation.required.msg";
			validationSupport.registerValidator(txInterestName, Validator.createEmptyValidator(getString(validationMessageId, interestNameCaption)));
			validationSupport.registerValidator(txInterestLink, Validator.createEmptyValidator(getString(validationMessageId, interestLinkCaption)));
			txInterestName.requestFocus();
		});

		Button creationButton = (Button) dialogPane.lookupButton(creationButtonType);
		validationSupport.invalidProperty().addListener((observable, oldValue, newValue) -> {
            creationButton.setDisable(newValue);
        });
		// only close dialog on a valid input or the user choose chancel
		creationButton.addEventFilter(ActionEvent.ACTION, (e) -> {
			if (validationSupport.isInvalid()) {
				e.consume();
			}
		});

		creationButton.setOnAction(actionEvent -> {
			if (validationSupport.isInvalid()) {
				actionEvent.consume();
				lbMessage.setText("");
				validationSupport.getHighestMessage(txInterestName).ifPresent((e) -> lbMessage.setText(e.getText()));
				validationSupport.getHighestMessage(txInterestLink).ifPresent((e) -> lbMessage.setText(lbMessage.getText() + "\n" + e.getText()));
				lbMessage.setVisible(true);
				lbMessage.setManaged(true);
			} else {
				lbMessage.setVisible(false);
				lbMessage.setManaged(false);
				hide();
			}
		});

		setResultConverter(dialogButton -> dialogButton == creationButtonType ?
			new Pair<>(txInterestName.getText(), txInterestLink.getText()) : null);
	}
}
