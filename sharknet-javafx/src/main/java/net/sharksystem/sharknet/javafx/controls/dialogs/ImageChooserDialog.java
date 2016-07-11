package net.sharksystem.sharknet.javafx.controls.dialogs;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.controls.FontIcon;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;
import net.sharksystem.sharknet.javafx.utils.NodeUtils;
import org.controlsfx.control.SnapshotView;

import java.io.File;



/**
 * Image chooser Dialog which enables a user to load a image
 * and select a subsection of the image.
 */
public class ImageChooserDialog extends Dialog<Image> {


	private class ImageSectionChooser extends StackPane {

		/******************************************************************************
		 *
		 * Constants
		 *
		 ******************************************************************************/

		private final int NW_RESIZE_HANDLER = 0;
		private final int NE_RESIZE_HANDLER = 1;
		private final int SE_RESIZE_HANDLER = 2;
		private final int SW_RESIZE_HANDLER = 3;

		/******************************************************************************
		 *
		 * Fields
		 *
		 ******************************************************************************/
		private final ImageView imageView;
		private final StackPane overlay;
		private final Rectangle imageArea;
		private final Rectangle imageSectionRectangle;

		private final Rectangle[] resizeHandles = new Rectangle[4];
		private Point2D dragStart;
		private Point2D resizeStart;
		private double requiredRatio = 1.0;


		/******************************************************************************
		 *
		 * Constructors
		 *
		 ******************************************************************************/

		ImageSectionChooser(double requiredSectionRatio) {
			if (requiredSectionRatio <= 0 || requiredSectionRatio > 1 ) {
				throw new IllegalArgumentException("requiredSectionRatio must be between 0.0 and 1.0");
			}
			this.requiredRatio = requiredSectionRatio;

			getStyleClass().add("image-section-chooser");
			imageView = new ImageView();
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(tabs.getPrefHeight() * 0.8);
			imageView.setEffect(new DropShadow());

			overlay = new StackPane();
			overlay.getStyleClass().add("overlay");
			overlay.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			overlay.setOpacity(0.8);

			imageArea = new Rectangle(getMaxWidth(), getMaxHeight());
			imageArea.setVisible(false);

			double initialSize = imageView.getFitHeight() * 0.2;
			imageSectionRectangle = new Rectangle(initialSize * 1/requiredSectionRatio, initialSize);
			imageSectionRectangle.setFill(Color.TRANSPARENT);
			imageSectionRectangle.setStroke(Color.BLACK);
			imageSectionRectangle.setStrokeWidth(1);
			imageSectionRectangle.setCursor(Cursor.MOVE);

			imageSectionRectangle.setOnMousePressed((e) -> {
				dragStart = new Point2D(imageSectionRectangle.getTranslateX() - e.getSceneX(), imageSectionRectangle.getTranslateY() - e.getSceneY());
				e.consume();
			});

			imageSectionRectangle.setOnMouseDragged((e) -> {
				if (dragStart != null) {

					double x = e.getSceneX() + dragStart.getX();
					double y = e.getSceneY() + dragStart.getY();
					ensureImageSelectionRectangleIsInBounds(x, y);
				}
				e.consume();
			});

			imageSectionRectangle.setOnMouseReleased((e) -> {
				dragStart = null;
				e.consume();
			});

			StackPane.setAlignment(imageView, Pos.CENTER);
			StackPane.setAlignment(overlay, Pos.CENTER);
			StackPane.setAlignment(imageSectionRectangle, Pos.TOP_LEFT);
			StackPane.setAlignment(imageArea, Pos.TOP_LEFT);


			getChildren().addAll(imageView, overlay, imageArea, imageSectionRectangle);
			createResizeHandlers();
			layout();
		}

		/******************************************************************************
		 *
		 * Properties
		 *
		 ******************************************************************************/

		private ObjectProperty<Image> image;
		public ObjectProperty<Image> imageProperty() {
			if (image == null) {
				image = new SimpleObjectProperty<Image>(ImageSectionChooser.this, "image") {
					@Override
					protected void invalidated() {
						imageView.setImage(imageProperty().getValue());
						NodeUtils.centerNode(imageSectionRectangle, imageView);
						layout();
					}
				};
			}
			return image;
		}

		public Image getImage() {
			if (image == null) return null;
			return image.get();
		}

		public void setImage(Image image) {
			imageProperty().set(image);
		}

		/******************************************************************************
		 *
		 * Methods
		 *
		 ******************************************************************************/

		/**
		 * Returns the selected image section as image.
		 *
		 * @return
         */
		public Image getSelectedImage() {

			Image image = getImage();
			Bounds layoutBounds = imageView.getLayoutBounds();
			double imageWidth = image.getWidth();
			double imageHeight = image.getHeight();

			double fitWidth = layoutBounds.getWidth();
			double fitHeight = layoutBounds.getHeight();
			double scaleX = imageWidth / fitWidth;
			double scaleY = imageHeight / fitHeight;


			int sectionX = (int) Math.ceil(imageSectionRectangle.getTranslateX() * scaleX);
			int sectionY = (int) Math.ceil(imageSectionRectangle.getTranslateY() * scaleY);
			int sectionW = (int) Math.ceil(imageSectionRectangle.getWidth() * scaleX);
			int sectionH = (int) Math.ceil(imageSectionRectangle.getWidth() * scaleX);


			PixelReader reader = image.getPixelReader();
			return new WritableImage(
				image.getPixelReader(),
				sectionX <= 0 ?  0 : sectionX,
				sectionY <= 0 ?  0 : sectionY,
				sectionW <= 0 ?  1 : sectionW,
				sectionH <= 0 ?  1 : sectionH
			);
		}

		@Override
		protected void layoutChildren() {
			super.layoutChildren();
			Bounds layoutBounds = imageView.getLayoutBounds();

			setMaxWidth(layoutBounds.getWidth());
			setMaxHeight(layoutBounds.getHeight());
			imageArea.setWidth(getMaxWidth());
			imageArea.setHeight(getMaxHeight());
			imageArea.relocate(layoutBounds.getMinX(), layoutBounds.getMinY());
			imageSectionRectangle.relocate(layoutBounds.getMinX(), layoutBounds.getMinY());

			// TODO: this operation is to exensive to call it multiple times
			Shape clip = Shape.subtract(imageArea, imageSectionRectangle);
			clip.relocate(0, 0);
			overlay.setClip(clip);
		}

		private void ensureImageSelectionRectangleIsInBounds(double x, double y) {
			double rect_w = imageSectionRectangle.getWidth();
			double rect_h = imageSectionRectangle.getHeight();
			double image_w = getWidth();
			double image_h = getHeight();

			if (x < 0) x = 0;
			if (x + rect_w > image_w) x = image_w - rect_w;

			if (y < 0) y = 0;
			if (y + rect_h > image_h) y = image_h - rect_h;

			imageSectionRectangle.setTranslateX(x);
			imageSectionRectangle.setTranslateY(y);
			layoutChildren();
		}

		private void createResizeHandlers() {
			final Cursor[] resizeCursors = {Cursor.NW_RESIZE, Cursor.NE_RESIZE, Cursor.SE_RESIZE, Cursor.SW_RESIZE};
			final ObservableNumberValue zero = Bindings.createDoubleBinding(() -> 0.0);
			for (int i = NW_RESIZE_HANDLER; i <= SW_RESIZE_HANDLER ; i++) {
				final int resizeHandlerId = i;
				final Rectangle resizeHandler = resizeHandles[i] = new Rectangle(10, 10);
				resizeHandler.setFill(Color.WHITE);
				resizeHandler.setStroke(Color.BLACK);
				resizeHandler.setStrokeWidth(1);
				resizeHandler.setCursor(resizeCursors[i]);
				StackPane.setAlignment(resizeHandler, Pos.TOP_LEFT);

				ObservableNumberValue offsetX = zero;
				ObservableNumberValue offsetY = zero;

				switch (i) {
					case NE_RESIZE_HANDLER:
						offsetX = imageSectionRectangle.widthProperty();
						break;
					case SE_RESIZE_HANDLER:
						offsetX = imageSectionRectangle.widthProperty();
						offsetY = imageSectionRectangle.heightProperty();
						break;
					case SW_RESIZE_HANDLER:
						offsetY = imageSectionRectangle.heightProperty();
						break;
				}

				resizeHandler.translateXProperty().bind(imageSectionRectangle.translateXProperty().subtract(5).add(offsetX));
				resizeHandler.translateYProperty().bind(imageSectionRectangle.translateYProperty().subtract(5).add(offsetY));

				resizeHandler.setOnMousePressed((e) -> {
					resizeStart = new Point2D(e.getSceneX(), e.getSceneY());
					e.consume();
				});

				resizeHandler.setOnMouseDragged((e) -> {
					double xOffset = e.getSceneX() - resizeStart.getX();
					double yOffset = e.getSceneY() - resizeStart.getY();

					double newWidth = imageSectionRectangle.getWidth();
					double newHeight = imageSectionRectangle.getHeight();


					double requiredRation = 16/9;
					// TODO: maintain aspect ration: 16:9 and 1:1

					switch (resizeHandlerId) {
						case (NW_RESIZE_HANDLER):
							newWidth -= xOffset;
							newHeight -= yOffset;
							break;
						case (NE_RESIZE_HANDLER):
							newWidth += xOffset;
							newHeight -= yOffset;
							xOffset = 0;
							break;
						case (SE_RESIZE_HANDLER):
							newWidth += xOffset;
							newHeight += yOffset;
							xOffset = 0;
							yOffset = 0;
							break;
						case (SW_RESIZE_HANDLER):
							newWidth -= xOffset;
							newHeight += yOffset;
							yOffset = 0;
							break;
					}

					if (newWidth > getMaxWidth()) newWidth = getMaxWidth();
					if (newWidth < 64) newWidth = 64;

					if (newHeight > getMaxHeight()) newHeight = getMaxHeight();
					if (newHeight < 64) newHeight = 64;

					if (newWidth != imageSectionRectangle.getWidth() || newHeight != imageSectionRectangle.getHeight()) {
						imageSectionRectangle.setWidth(newWidth);
						imageSectionRectangle.setHeight(newHeight);

						ensureImageSelectionRectangleIsInBounds(
							imageSectionRectangle.getTranslateX() + xOffset,
							imageSectionRectangle.getTranslateY() + yOffset
						);
					}
					resizeStart = new Point2D(e.getSceneX(), e.getSceneY());

					e.consume();
				});

				getChildren().add(resizeHandler);

			}
		}
	}

	private TabPane tabs;
	private Tab chooseLocalImageTab;
	private final StackPane chooseLocalImagePane;
	private final Button chooseImageButton;
	private final ImageSectionChooser imageSectorChooser;
	private final ProgressBar progressBar;
	private final VBox imageChoosePane;

	private Tab chooseWebCamImageTab;
	private final StackPane chooseWebCamImagePane;

	public ImageChooserDialog(Stage primaryStage) {
		initStyle(StageStyle.UNDECORATED);
		initModality(Modality.APPLICATION_MODAL);
		
		chooseImageButton = new Button("Foto vom Computer auswählen");
		chooseImageButton.setOnAction(this::showImageSelectDialog);

		imageChoosePane = new VBox();
		imageChoosePane.setAlignment(Pos.CENTER);
		StackPane.setAlignment(imageChoosePane, Pos.CENTER);
		imageChoosePane.getChildren()
			.addAll(
				new FontIcon(FontAwesomeIcon.FILE_IMAGE_O),
				new Label("Foto hierher ziehen"),
		new Label("- Oder - "),
				chooseImageButton
			);

		imageChoosePane.getChildren().get(0).setStyle("-fx-font-size: 56px; -fx-text-fill: #AAAAAAFF;");
		imageChoosePane.getChildren().get(1).setStyle("-fx-font-weight: bold;fx-font-family:Roboto; -fx-font-size: 20px; -fx-text-fill: #aaa; -fx-padding: 10px 0 10px 0;");
		imageChoosePane.getChildren().get(2).setStyle("-fx-font-weight: bold;fx-font-family:Roboto; -fx-font-size: 15px; -fx-text-fill: #aaa; -fx-padding: 15px 0 15px 0;");

		chooseLocalImagePane = new StackPane();
		chooseLocalImagePane.setStyle("-fx-background-color: #e5e5e5");
		chooseLocalImagePane.getChildren().add(imageChoosePane);
		chooseLocalImagePane.setOnDragOver(this::onDragOver);
		chooseLocalImagePane.setOnDragDropped(this::onDragDropped);


		chooseLocalImageTab = new Tab("Hochladen");
		chooseLocalImageTab.setContent(chooseLocalImagePane);
		chooseLocalImageTab.selectedProperty().addListener(observable -> {
			showImageSelector();
		});

		chooseWebCamImagePane = new StackPane();
		chooseWebCamImagePane.setStyle("-fx-background-color: #e5e5e5");
		chooseWebCamImageTab = new Tab("Webkamera");
		chooseWebCamImageTab.setContent(chooseWebCamImagePane);

		Screen screen = Screen.getPrimary();
		Rectangle2D screenBounds = screen.getBounds();
		tabs = new TabPane();
		tabs.setPrefWidth(screenBounds.getWidth() * 0.8);
		tabs.setMinWidth(tabs.getMinWidth());
		tabs.setPrefHeight(screenBounds.getHeight() * 0.8);
		tabs.setMinHeight(tabs.getPrefHeight());

		tabs.getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
		tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		tabs.getStyleClass().add("theme-presets");
		tabs.getTabs().addAll(chooseLocalImageTab, chooseWebCamImageTab);

		progressBar = new ProgressBar();
		progressBar.setPrefWidth( tabs.getPrefWidth() * 0.8 );

		imageSectorChooser = new ImageSectionChooser(1.0);

		getDialogPane().setEffect(new DropShadow());
		getDialogPane().setContent(tabs);
		getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		ButtonType applyButton = new ButtonType("Als Profilbild festlegen", ButtonBar.ButtonData.APPLY);
		getDialogPane().getButtonTypes().add(applyButton);


		setResultConverter(param -> {
			if (param.equals(applyButton)) {
				return imageSectorChooser.getSelectedImage();
			}
			return null;
		});
	}


	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void showImageSelectDialog(ActionEvent event) {
		FileChooser imageChooser = new FileChooser();
		File imageFile = imageChooser.showOpenDialog(getOwner());
		if (imageFile != null) {
			loadImage(imageFile.toURI().toString());
		}
	}

	private void loadImage(String imageToLoad) {
		try {
			Image image = new Image(imageToLoad, true);
			progressBar.setProgress(0);
			chooseLocalImagePane.getChildren().clear();
			chooseLocalImagePane.getChildren().add(progressBar);
			StackPane.setAlignment(progressBar, Pos.CENTER);

			image.errorProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    throw new IllegalArgumentException("Image not loadable");
                }
            });
			image.progressProperty().addListener((observable, oldValue, newValue) -> {
              	progressBar.setProgress(newValue.doubleValue());
				if(newValue.doubleValue() >= 1.0) {
					showImageSelector(image);
				}
            });
		} catch (IllegalArgumentException e) {
			showImageSelector();
		}
	}

	private void showImageSelector(Image image) {
		chooseLocalImagePane.getChildren().clear();
		chooseLocalImagePane.getChildren().add(imageSectorChooser);
		imageSectorChooser.setImage(image);
	}

	private void showImageSelector() {
		chooseLocalImagePane.getChildren().clear();
		chooseLocalImagePane.getChildren().add(imageChoosePane);
	}

	/******************************************************************************
	 *
	 * Event Handling
	 *
	 ******************************************************************************/

	private void onDragOver(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasUrl()) {
			event.acceptTransferModes(TransferMode.COPY);
			event.consume();
		}
	}

	private void onDragDropped(DragEvent event) {
		Dragboard db = event.getDragboard();
		boolean success = false;
		String imageToLoad = null;

		if (db.hasUrl()) {
			imageToLoad = db.getUrl();
			success = true;
			loadImage(imageToLoad);
		}

		event.setDropCompleted(success);
		event.consume();
	}
}
