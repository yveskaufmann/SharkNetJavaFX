package net.sharksystem.sharknet.javafx.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Supporter class which helps to access local-specific string constants.
 */
public class I18N {
	/**
	 * The logger for this class
	 */
	private static final Logger Log = LoggerFactory.getLogger(I18N.class);

	/**
	 * The base name of the default resource bundle
	 */
	private static final String MAIN_BUNDLE = "net.sharksystem.sharknet.javafx.i18N.sharknet";

	/**
	 * The current local resource bundle
	 */
	private static ResourceBundle resourceBundle;


	/**
	 * Retrieves the main ResourceBundle which contains all local-specific
	 * string constants.
	 *
	 * @return the main {@link ResourceBundle}.
     */
	synchronized
	public static ResourceBundle getResourceBundle() {
		if (resourceBundle == null) {
			resourceBundle = ResourceBundle.getBundle(MAIN_BUNDLE);
		}
		return resourceBundle;
	}

	/**
	 * <p>
	 * Retrieve the local-specific string constant which
	 * is stored in <code>resources/net/sharksystem/sharknet/javafx/i18n/sharknet_LANGUAGE_COUNTRY.properties</code>.
	 * </p>
	 *
	 * <p>
	 * For <b>development purpose</b> we are firstly care <code>resources/net/sharksystem/sharknet/javafx/i18n/sharknet_LANGUAGE_COUNTRY.properties</code>
	 * which is loaded when no local-specific resource bundle is present.
	 * </p>
	 *
	 * @param key the name of the desired string constant.
	 * The key can be prefixed with {@code "%"}, in this case the prefix
	 * will be ignored. This is can be kind useful if the key
	 * key comes from a view which was created by using <a href="http://gluonhq.com/open-source/scene-builder/">Scene Builder</a>.
	 *
	 * @return return the desired string constant or the passed key prefixed with {@code "%"} if the
	 * desired string constant doesn't exists.
	 *
	 * @see #getString(String, Object...)
     */
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

	/**
	 * Works like {link {@link #getString(String)}} but the returned
	 * string constant will be used as {@link MessageFormat} pattern
	 * and is used to format the given argument(s).
	 *
	 * @param key the name of the desired string constant which contains the {@link MessageFormat} pattern.
	 * The key can be prefixed with {@code "%"}, in this case the prefix
	 * will be ignored. This is can be kind useful if the key
	 * key comes from a view which was created by using <a href="http://gluonhq.com/open-source/scene-builder/">Scene Builder</a>.
	 *
	 * @param arguments objects to format
     *
	 * @return the formatted string or the given string constant key
	 * if the given string constant doesn't exists.
	 *
	 * @throws IllegalArgumentException if key constant contains a invalid {@link MessageFormat} pattern,
	 * 									or if an argument in the arguments array is not of the type expected
	 * 									by the format element(s) that use it.
	 *
	 * @see #getString(String)
	 *
     */
	public static String getString(String key, Object... arguments) {
		final String pattern = getString(key);
		try {
			return MessageFormat.format(pattern, arguments);
		} catch (IllegalArgumentException ex) {
			Log.error(
				MessageFormat.format("Can't format the specified string constant {}: Invalid format or arguments", key)
			, ex);
			throw ex;
		}
	}
}
