package net.sharksystem.sharknet.javafx.controller.interest;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TXSemanticTag;
import net.sharksystem.sharknet.javafx.controls.MessageBanner;
import net.sharksystem.sharknet.javafx.controls.cell.HyperLinkTableCell;
import org.controlsfx.control.PopOver;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.util.Arrays;
import java.util.function.Predicate;

import static net.sharksystem.sharknet.javafx.i18n.I18N.getString;

public class InterestEntryController {

	private enum SubscriptionType {
		ACTIVATED(getString("interest.subscription.activated")),
		DEACTIVATED(getString("interest.subscription.deactivated"));

		private String caption;

		SubscriptionType(String caption) {
			this.caption = caption;
		}

		public String getCaption() {
			return caption;
		}

		@Override
		public String toString() {
			return caption;
		}
	}

	public static class LinkEntry {
		private final SimpleStringProperty link;

		public LinkEntry() {this("");}

		public LinkEntry(String link) {
			this.link = new SimpleStringProperty(link);
		}

		public String getLink() {
			return link.get();
		}

		public void setLink(String link) {
			this.link.setValue(link);
		}

		@Override
		public String toString() {
			return link.get();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			LinkEntry linkEntry = (LinkEntry) o;

			return getLink().equals(linkEntry.getLink());
		}

		@Override
		public int hashCode() {
			return link != null ? link.get().hashCode() : 0;
		}
	}

	private final static LinkEntry TEST_ENTRY = new LinkEntry("");

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/
	@FXML private AnchorPane interestEditorRoot;
	@FXML private TextField interestNameTextbox;
	@FXML private TableView<LinkEntry> interestLinkTable;
	@FXML private ComboBox<SubscriptionType> subscriptionChooser;
	@FXML private Button addLinkButton;
	@FXML private Button removeLinkButton;
	@FXML private TextField linkToAddTextfield;

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private ValidationSupport validationSupport;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public void initialize() {

		subscriptionChooser.getItems().addAll(SubscriptionType.ACTIVATED, SubscriptionType.DEACTIVATED);

		TableColumn<LinkEntry, String> linkColumn = new TableColumn<>(getString("interest.si.header"));
		linkColumn.setCellFactory(HyperLinkTableCell.forTableColumn());
		linkColumn.setCellValueFactory(new PropertyValueFactory<>("link"));
		linkColumn.setEditable(true);
		linkColumn.prefWidthProperty().bind(interestLinkTable.widthProperty().multiply(0.99));




		interestLinkTable.setEditable(true);
		interestLinkTable.getColumns().add(linkColumn);
		interestLinkTable.getItems().addListener(new ListChangeListener<LinkEntry>() {
			@Override
			public void onChanged(Change<? extends LinkEntry> c) {
				removeLinkButton.setDisable(interestLinkTable.getItems().size() <= 1);
			}
		});

		linkToAddTextfield.setOnKeyReleased(event -> {
			if (KeyCode.ENTER.equals(event.getCode())) {
				addLinkButton.fire();
			}
		});
		addLinkButton.setOnAction(this::onAddLinkClicked);
		removeLinkButton.setOnAction(this::onRemoveLinkClicked);

		interestNameTextbox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !"".equals(newValue.trim())) {
				getInterestTag().getValue().setName(newValue);
			}
			Event.fireEvent(getInterestTag(), new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), getInterestTag()));
        });

		/**
		 * Handle Link edits
		 */
		linkColumn.addEventHandler(TableColumn.editCommitEvent(), new EventHandler<TableColumn.CellEditEvent<LinkEntry, String>>() {

			@Override
			public void handle(TableColumn.CellEditEvent<LinkEntry, String> event) {
				TableColumn.CellEditEvent cellEvent = (TableColumn.CellEditEvent) event;
				String newValue = (String) cellEvent.getNewValue();
				if (newValue != null) {
					String oldValue = event.getOldValue();

					try {
						getInterestTag().getValue().addSI(newValue);
						if (oldValue != null) {
							getInterestTag().getValue().removeSI(oldValue);
						}
					} catch (SharkKBException e) {
						e.printStackTrace();
					}

				}
			}
		});

		interestTagProperty().addListener(this::onTagChanged);
		interestEditorRoot.visibleProperty().bind(visibilityProperty());

		validationSupport = new ValidationSupport();
		validationSupport.registerValidator(interestNameTextbox, true, Validator.createEmptyValidator(getString("%validation.required.msg", interestNameTextbox.getPromptText())));
		validationSupport.registerValidator(linkToAddTextfield, false, Validator.createPredicateValidator(o -> {
			TEST_ENTRY.setLink(linkToAddTextfield.getText().trim());
			boolean isValid = !"".equals(TEST_ENTRY.getLink()) && !interestLinkTable.getItems().contains(TEST_ENTRY);
			addLinkButton.setDisable(!isValid);
			return isValid;
		}, ""));
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/
	/**
	 * Loads the data of tag from interestTagProperty when its value is changed
	 *
	 * @param observableValue
	 * @param oldTag
	 * @param newTag
     */
	private void onTagChanged(ObservableValue<? extends TreeItem<TXSemanticTag>> observableValue, TreeItem<TXSemanticTag> oldTag, TreeItem<TXSemanticTag> newTag) {
		interestNameTextbox.clear();
		interestLinkTable.getItems().clear();
		subscriptionChooser.setValue(null);
		linkToAddTextfield.clear();

		interestNameTextbox.setText(newTag.getValue().getName());
		Arrays.asList(newTag.getValue().getSI())
			.stream()
			.map(LinkEntry::new)
			.forEach(linkEntry -> interestLinkTable.getItems().add(linkEntry));

	}

	/**
	 * Called when a user request to add a link to a interest
	 *
	 * @param event
     */
	private void onAddLinkClicked(ActionEvent event) {

		String newURL = linkToAddTextfield.getText().trim();
		LinkEntry linkEntry = new LinkEntry(newURL);
		if (interestLinkTable.getItems().contains(linkEntry)) {

			MessageBanner messageBanner = new MessageBanner();
			PopOver popOver = new PopOver(messageBanner);
			popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
			messageBanner.show(getString("%interest.si.duplication.msg", newURL));
			popOver.show(linkToAddTextfield);
			Timeline tl = new Timeline(new KeyFrame(Duration.millis(5000)));
			tl.setOnFinished((e) -> popOver.hide());
			tl.play();

			linkToAddTextfield.requestFocus();
			return;
		}
		linkToAddTextfield.clear();
		if (! "".equals(newURL)) {
			interestLinkTable.getItems().add(linkEntry);
			try {
				getInterestTag().getValue().addSI(newURL);
			} catch (SharkKBException e) {
				throw new RuntimeException("Failed to save si to " + getInterestTag().getValue().getName());
			}
		}
	}

	/**
	 * Called when a user requested to remove si identifier
	 *
	 * @param event
     */
	private void onRemoveLinkClicked(ActionEvent event) {
		TableView.TableViewSelectionModel<LinkEntry> selectionModel = interestLinkTable.getSelectionModel();
		if (! selectionModel.isEmpty()) {
			for (LinkEntry items:  selectionModel.getSelectedItems()) {
				if (interestLinkTable.getItems().size() > 1) {
					interestLinkTable.getItems().remove(items);
					try {
						getInterestTag().getValue().removeSI(items.getLink());
					} catch (SharkKBException e) {
						throw new RuntimeException("Failed to remove si from " + getInterestTag().getValue().getName(), e);
					}
				}
			}
		}
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	private ObjectProperty<TreeItem<TXSemanticTag>> interestTag;

	/**
	 * Specifies which TreeItem<TXSemanticTag> should be displayed to the user.
	 *
	 * @return a property
     */
	public  ObjectProperty<TreeItem<TXSemanticTag>> interestTagProperty() {
		if (interestTag == null) {
			interestTag = new SimpleObjectProperty<>(this, "interestTag");
		}
		return interestTag;
	}

	/**
	 * Returns which reeItem<TXSemanticTag>  should be displayed by this view.
	 *
	 * @return the currently specified TXSemanticTag
     */
	public TreeItem<TXSemanticTag> getInterestTag() {
		return interestTag == null ? null : interestTag.get();
	}

	/**
	 * Specifies which TXSemanticTag should be displayed by this view.
	 *
	 * @param tag the tag to display
     */
	public void setInterestTag(TreeItem<TXSemanticTag> tag) {
		interestTagProperty().setValue(tag);
	}

	private BooleanProperty visibility;

	/***
	 * Determines if this view should be visible
	 *
	 * @return visibility property
     */
	public BooleanProperty visibilityProperty() {
		if (visibility == null) {
			visibility = new SimpleBooleanProperty(false);
		}
		return visibility;
	}

	public boolean isVisible() {
		return visibilityProperty().get();
	}

	public void setVisible(boolean visible) {
		visibilityProperty().set(visible);
	}
}
