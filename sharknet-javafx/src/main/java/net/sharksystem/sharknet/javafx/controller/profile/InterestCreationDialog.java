package net.sharksystem.sharknet.javafx.controller.profile;

import javafx.application.Platform;
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

	public InterestCreationDialog() {
		final DialogPane dialogPane = getDialogPane();

		setTitle(getString("profile.interestCreation.dlg.title"));
		dialogPane.setHeaderText(getString("profile.interestCreation.dlg.header"));
		dialogPane.getStyleClass().add("interest-dialog");
		dialogPane.getButtonTypes().addAll(ButtonType.CANCEL);
		dialogPane.getButtonTypes().addAll(ButtonType.FINISH);

		txInterestName = (CustomTextField) TextFields.createClearableTextField();
		txInterestLink = (CustomTextField) TextFields.createClearableTextField();

		Label lbMessage= new Label("");  //$NON-NLS-1$
		lbMessage.getStyleClass().addAll("message-banner"); //$NON-NLS-1$
		lbMessage.setVisible(false);
		lbMessage.setManaged(false);

		final VBox content = new VBox(10);
		content.getChildren().add(lbMessage);
		content.getChildren().add(txInterestName);
		content.getChildren().add(txInterestLink);

		dialogPane.setContent(content);

		Button creationButton = (Button) dialogPane.lookupButton(ButtonType.FINISH);
		creationButton.setOnAction(actionEvent -> {
			try {
				lbMessage.setVisible(false);
				lbMessage.setManaged(false);
				hide();
			} catch( Throwable ex ) {
				lbMessage.setVisible(true);
				lbMessage.setManaged(true);
				lbMessage.setText(ex.getMessage());
				ex.printStackTrace();
			}
		});

		String interestNameCaption = getString("profile.interestCreation.dlg.name.caption");
		String interestLinkCaption = getString("profile.interestCreation.dlg.webpage.caption");
		txInterestName.setPromptText(interestNameCaption);
		txInterestLink.setPromptText(interestLinkCaption);

		ValidationSupport validationSupport = new ValidationSupport();
		Platform.runLater( () -> {
			String requiredFormat = "'%s' is required";
			validationSupport.registerValidator(txInterestName, Validator.createEmptyValidator( String.format( requiredFormat, interestNameCaption )));
			validationSupport.registerValidator(txInterestLink, Validator.createEmptyValidator(String.format( requiredFormat, interestLinkCaption )));
			txInterestName.requestFocus();
		} );


		setResultConverter(dialogButton -> dialogButton == ButtonType.FINISH ?
			new Pair<>(txInterestName.getText(), txInterestLink.getText()) : null);
	}
}
