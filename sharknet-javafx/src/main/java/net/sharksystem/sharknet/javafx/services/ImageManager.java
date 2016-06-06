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

	public Optional<Image> readSync(Content content) throws IOException {

		//TODO: check mime type
		//TODO: test image object caching with changed profile image

		if (alreadyLoadedContentImages.containsKey(content)) {
			Log.debug("Load image from cache: " + content);
			return Optional.of(alreadyLoadedContentImages.get(content));
		}

		String type = content.getFileExtension();
		if (! SUPPORTED_FORMATS.contains(content.getFileExtension())) {
			throw new IllegalArgumentException("Content isn't a supported image format");
		}

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
			throw new IOException("Could not load image from content", e);
		}

		return Optional.empty();
	};


	public void write(Image image, String format, OutputStream out) throws IOException {
		BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
		ImageIO.write(bufferedImage, format, out);
	}
}
