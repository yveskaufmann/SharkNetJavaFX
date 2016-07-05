package net.sharksystem.sharknet.javafx.controls.skins;

import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import net.sharksystem.sharknet.javafx.controls.TextField;
import net.sharksystem.sharknet.javafx.utils.FontAwesomeIcon;
import net.sharksystem.sharknet.javafx.utils.ReflectionUtils;

/**
 * Google Material Like TextField.
 *
 * @see <a href="https://www.google.com/design/spec/components/text-fields.html">TextField Design</a>
 */
public class TextFieldSkin extends com.sun.javafx.scene.control.skin.TextFieldSkin {

	// TODO: [x] underline
	// TDOO: [ ] underline color
	// TODO: [ ] underline focus color
	// TODO: [ ] label/prompt
	// TODO: [x] clear
	// TODO: [x] clear layout
	// TODO: [ ] required field

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/
	private Text clearButton;
	private Text promptNode;
	private Text textNode;
	private Text label;

	private Line line;
	private StackPane labelContainer;
	private Pane textPane;
	private boolean dirty = true;

	public TextFieldSkin(TextField textField) {
		super(textField);

		Class<?> baseSkinClass = com.sun.javafx.scene.control.skin.TextFieldSkin.class;
		promptNode = (Text) ReflectionUtils.getFieldValue("promptNode", baseSkinClass , this);
		textNode = (Text) ReflectionUtils.getFieldValue("textNode", baseSkinClass, this);

		textPane = ((Pane)this.getChildren().get(0));
		textPane.prefWidthProperty().bind(getSkinnable().prefWidthProperty());

		label = new Text();
		label.textProperty().bind(textField.promptTextProperty());
		textPane.getChildren().add(label);

		clearButton = new Text(FontAwesomeIcon.REMOVE.toString());
		clearButton.setFill(Color.BLACK);
		clearButton.getStyleClass().add("icon-label");
		clearButton.setManaged(false);
		clearButton.setVisible(false);
		clearButton.setOnMousePressed((e) -> {
			textField.setText("");
		});
		getChildren().add(clearButton);

		line = new Line();
		line.setStrokeWidth(1);
		line.setStrokeType(StrokeType.CENTERED);
		line.setFill(Color.BLACK);
		line.setStroke(Color.BLACK);
		line.setManaged(false);
		line.setStartX(0);
		line.endXProperty().bind(textPane.widthProperty().add(Bindings.createDoubleBinding(() -> clearButton.getLayoutBounds().getWidth()).multiply(2)));
		line.endYProperty().bind(line.startYProperty());
		line.startYProperty().bind(textPane.heightProperty());
		line.setTranslateY(-2);
		getChildren().add(line);

		textField.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		textField.setAlignment(Pos.CENTER_LEFT);

		caretPath.fillProperty().unbind();
		caretPath.fillProperty().bind(textField.cursorColorProperty());
		caretPath.strokeProperty().bind(textField.cursorColorProperty());


		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			clearButton.setVisible(! newValue.isEmpty());
		});

		textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            getSkinnable().layout();
        });

	}

	@Override
	protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		return super.computeMaxWidth(height, topInset, rightInset + 20, bottomInset, leftInset);
	}

	@Override
	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		return super.computeMinWidth(height, topInset, rightInset + 20, bottomInset, leftInset);
	}

	@Override
	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		return super.computePrefWidth(height, topInset, rightInset + 20, bottomInset, leftInset);
	}

	@Override protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		return super.computePrefHeight(width, topInset, rightInset, bottomInset + 5, leftInset);
	}

	@Override protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		return super.computeMaxHeight(width, topInset, rightInset, bottomInset + 5, leftInset);
	}
	@Override protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		return super.computeMinHeight(width, topInset, rightInset, bottomInset + 1, leftInset);
	}
	@Override
	protected void layoutChildren(double x, double y, double w, double h) {
		super.layoutChildren(x, y, w, h);

		Bounds clearButtonBounds = clearButton.getLayoutBounds();
		line.relocate(x, y + h);
		clearButton.relocate(w + clearButtonBounds.getWidth(), (h - clearButtonBounds.getHeight()) / 2 );

	}
}
