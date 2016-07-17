package net.sharksystem.sharknet.api.utils;

import net.sharksystem.sharknet.api.SharkNet;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * @author Yves Kaufmann
 * @since 17.07.2016
 */
public class Resources {
	public static File get(String name) {
		final URL recourseURL = SharkNet.class.getResource(name);
		if (recourseURL == null) {
			throw new RuntimeException("Resource not found" + name);
		}

		try {
			return Paths.get(recourseURL.toURI()).toFile();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		return null;
	}
}
