package net.sharksystem.sharknet.javafx.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.MessageFormat;


public class ReflectionUtils {

	private static final Logger Log = LoggerFactory.getLogger(ReflectionUtils.class);

	/**
	 * Retrieves a field instance by its name from a given class instance.
	 *
	 * @param fieldName the name of the desired field.
	 * @param instance the instance which has the desired field.
	 * @return the desired field or null if the given instance doesn't provide the desired field.
	 */
	public static Object getFieldValue(String fieldName, Object instance) {
		return getFieldValue(fieldName, instance.getClass(), instance);
	}

	/**
	 * Retrieves a field instance by its name from a given class instance.
	 *
	 * @param fieldName the name of the desired field.
	 * @param cls  the class which owns the desired field.
	 * @param instance the instance which has the desired field.
     * @return the desired field or null if the given instance doesn't provide the desired field.
     */
	public static Object getFieldValue(String fieldName, Class<?> cls, Object instance) {
		try {
			Field field = cls.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);

		} catch (NoSuchFieldException | IllegalAccessException e) {
			Log.warn(MessageFormat.format("\"{0}\" is not a accessible field of {1}.", fieldName, cls.getName()),e);
		}
		return null;
	}
}
