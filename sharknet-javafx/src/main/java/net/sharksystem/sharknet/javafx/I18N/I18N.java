package net.sharksystem.sharknet.javafx.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18N {

	private static final Logger Log = LoggerFactory.getLogger(I18N.class);

	/**
	 * The base name of the default resource bundle
	 */
	private static final String MAIN_BUNDLE = "net.sharksystem.sharknet.javafx.I18N.sharknet";

	/**
	 * The current local resource bundle
	 */
	private static ResourceBundle resourceBundle;

	synchronized
	public static ResourceBundle getResourceBundle() {
		if (resourceBundle == null) {
			resourceBundle = ResourceBundle.getBundle(MAIN_BUNDLE);
		}
		return resourceBundle;
	}

	public static String getString(String key) {
		if (key.startsWith("%")) {
			key = key.substring(1, key.length());
		}

		if (getResourceBundle().containsKey(key)) {
			return getResourceBundle().getString(key);
		}
		Log.error("Requested string with the key '{}' is missing in the resource bundle: '{}'", key, getResourceBundle().getBaseBundleName());
		return "%" + key;
	}

	public static String getString(String key, Object... arguments) {
		final String pattern = getString(key);
		return MessageFormat.format(pattern, arguments);
	}
}
