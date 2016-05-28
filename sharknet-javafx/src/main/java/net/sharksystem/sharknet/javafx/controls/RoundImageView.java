package net.sharksystem.sharknet.javafx.controls;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

/**
 * A rounded image view which is clipped by a circle.
 */
public class RoundImageView extends ImageView {

	private Circle clip;

	/**
	 * Allocates a new RoundImageView object.
	 */
	public RoundImageView() {
		super();
		initialize();
	}

	/**
	 * Allocates a new ImageView object with image loaded from the specified
	 * URL.
	 * <p>
	 * The {@code new ImageView(url)} has the same effect as
	 * {@code new ImageView(new Image(url))}.
	 * </p>
	 *
	 * @param url the string representing the URL from which to load the image
	 * @throws NullPointerException if URL is null
	 * @throws IllegalArgumentException if URL is invalid or unsupported
	 * @since JavaFX 2.1
	 */
	public RoundImageView(String url) {
		super(url);
		initialize();
	}

	/**
	 * Allocates a new ImageView object using the given image.
	 *
	 * @param image Image that this ImageView uses
	 */
	public RoundImageView(Image image) {
		super(image);
		initialize();
	}

	private void initialize() {
		getStyleClass().add("round-image-view");
		if (clip != null) return; // ImageView has already a clip object

		clip = new Circle(getFitWidth() * 0.5, getFitHeight() * 0.5, getFitWidth() * 0.5);
		clip.radiusProperty().bind(fitWidthProperty().multiply(0.5));
		clip.centerXProperty().bind(fitWidthProperty().multiply(0.5));
		clip.centerYProperty().bind(fitHeightProperty().multiply(0.5));
		setClip(clip);
	}


}
