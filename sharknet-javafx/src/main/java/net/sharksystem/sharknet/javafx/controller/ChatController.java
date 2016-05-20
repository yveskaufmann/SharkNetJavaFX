package net.sharksystem.sharknet.javafx.controller;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import net.sharksystem.sharknet.api.ImplChat;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.AbstractController;


@Controller( title = "%sidebar.chat")
public class ChatController extends AbstractController {

	private AppController appController;
	private ImplChat chat;

	@FXML
	private TextField textFieldMessage;
	@FXML
	private ImageView imageViewAttachment;
	@FXML
	private ImageView imageViewAdd;
	@FXML
	private ImageView imageViewContactProfile;
	@FXML
	private ImageView imageViewVote;
	@FXML
	private TextArea textAreaChat;


	public ChatController(AppController appController) {
		super(App.class.getResource("views/chatView.fxml"));
		this.appController = appController;
		this.chat = new ImplChat(null);
	}

	@Override
	protected void onFxmlLoaded() {
		//TODO: Implement chat controller

		// set onMouseClick for Attachment ImageView
		imageViewAttachment.setOnMouseClicked(event -> {
			onAttachmentClick();
			event.consume();
		});
		// set onMouseClick for Add Chat Contact ImageView
		imageViewAdd.setOnMouseClicked(event -> {
			onAddClick();
			event.consume();
		});
		// set onMouseClick for Contact Profile ImageView
		imageViewContactProfile.setOnMouseClicked(event -> {
			onContactProfileClick();
			event.consume();
		});
		// set onMouseClick for Vote ImageView
		imageViewVote.setOnMouseClicked(event -> {
			onVoteClick();
			event.consume();
		});
	}

	private void onAttachmentClick() {
		System.out.println("onAttachmentClick");
	}

	private void onAddClick() {
		System.out.println("onAddClick");
	}

	private void onContactProfileClick() {
		System.out.println("onContactProfileClick");
	}

	private void onVoteClick() {
		System.out.println("onVoteClick");
	}

	@FXML
	private void onSendClick(ActionEvent event) {
		String message = textFieldMessage.getText();
		chat.sendMessage(message);
		textAreaChat.appendText('\n' + message);
	}

	@FXML
	private void onNewChatClick(ActionEvent event) {
		System.out.println("onNewChatClick");
	}
}
