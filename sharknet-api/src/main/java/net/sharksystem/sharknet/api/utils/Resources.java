package net.sharksystem.sharknet.api.utils;

import net.sharkfw.knowledgeBase.Information;
import net.sharkfw.knowledgeBase.inmemory.InMemoInformation;
import net.sharksystem.sharknet.api.Content;
import net.sharksystem.sharknet.api.ImplContent;
import net.sharksystem.sharknet.api.Profile;
import net.sharksystem.sharknet.api.SharkNet;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Helper class which supports
 * to load dummy data from the resource folders.
 *
 * @author Yves Kaufmann
 * @since 17.07.2016
 */
public class Resources {

	public static Content getImage(String name, String mimeType, Profile owner) {
		final URL recourseURL = SharkNet.class.getResource(name);
		if (recourseURL == null) {
			throw new RuntimeException("Resource not found " + name);
		}

		try (final InputStream inputStream = SharkNet.class.getResourceAsStream(name)) {

			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			IOUtils.copy(inputStream, outputStream);

			final InMemoInformation inMemoInformation = new InMemoInformation(outputStream.toByteArray());
			inMemoInformation.setContentType(mimeType);

			ImplContent content = new ImplContent(inMemoInformation, owner);
			content.setFilename(new File(recourseURL.getFile()).getName());

			return content;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
