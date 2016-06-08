package net.sharksystem.sharknet.javafx.services;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import net.sharksystem.sharknet.api.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * This class supports the loading, saving and editing of images.
 */
public class ImageManager {

	/******************************************************************************
	 *
	 * Constants
	 *
	 ******************************************************************************/

	private Logger Log = LoggerFactory.getLogger(ImageManager.class);
	private final static List<String> SUPPORTED_FORMATS = Arrays.asList("png", "jpg", "jpeg", "bmp");

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private Map<Content, Image> alreadyLoadedContentImages;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/
	ImageManager() {
		alreadyLoadedContentImages = Collections.synchronizedMap(new WeakHashMap<>());
	}

	/**
	 * Loads a image synchronously from a passed content object and return it
	 * as a {@link Optional<Image>} object. When the content isn't readable
	 * then a empty optional is returned.
	 *
	 * <p> Usage example:
	 * <pre>
	 *     private ImageView imageView;
	 *     // ...
	 *     public void loadImageFromContact(Contact contact) {
	 *         readImageFrom(contact.getPicture()).ifPresent(imageView::setImage);
	 *     }
	 * </pre>
	 *
	 * @param content the content to read from
	 * @return a optional which contains the image if content is not null and readable.
     */
	public Optional<Image> readImageFrom(Content content) {
		//TODO: check mime type
		//TODO: test image object caching with changed profile image

		if (alreadyLoadedContentImages.containsKey(content)) {
			Log.debug("Load image from cache: " + content);
			return Optional.of(alreadyLoadedContentImages.get(content));
		}

		if (content != null && isImage(content)) {

			try (InputStream in = content.getFile()) {
				assert in.available() > 0;
				BufferedImage bufferedImage = ImageIO.read(in);
				if (bufferedImage != null) {
					Image image = SwingFXUtils.toFXImage(bufferedImage, null);
					alreadyLoadedContentImages.put(content, image);
					Log.debug("Load image from content: " + content);
					return Optional.of(image);
				}
			} catch (IOException e) {
				Log.warn("Could not load image from content", e);
			}
		}
		return Optional.empty();
	}

	/**
	 * Checks if content contains a image with a supported format.
	 *
	 * <p> Currently only these formats are supported:
	 * <ul>
	 *     <li>png</li>
	 *     <li>jpg</li>
	 *     <li>jpeg</li>
	 *     <li>bmp</li>
	 * </ul>
	 *
	 * @param content content to check for
	 * @return true if the content contains a image with a supported format.
     */
	public boolean isImage(Content content) {
		String type = Objects.toString(content.getFileExtension(), "NULL");
		if(SUPPORTED_FORMATS.contains(type)) return true;

		Log.warn("Content doesn't contains a image, invalid type: '" + type + "'");
		return false;
	}

	/**
	 * Writes a image synchronously to a {@link OutputStream} in specified image format.
	 *
	 * @param image the image object to write
	 * @param targetFormat the target format in which the image is written to the stream
	 * @param out a stream which receives the formatted image data
	 * @throws IOException
     */
	public void write(Image image, String targetFormat, OutputStream out) throws IOException {
		// TODO: discuss content saving
		// TODO: writing should not happen in the gui thread
		BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
		ImageIO.write(bufferedImage, targetFormat, out);
	}


}
