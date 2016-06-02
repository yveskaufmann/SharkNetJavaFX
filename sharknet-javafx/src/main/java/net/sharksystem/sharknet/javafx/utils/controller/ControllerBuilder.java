package net.sharksystem.sharknet.javafx.utils.controller;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import net.sharksystem.sharknet.javafx.actions.ActionEntry;
import net.sharksystem.sharknet.javafx.actions.annotations.Action;
import net.sharksystem.sharknet.javafx.context.ViewContext;
import net.sharksystem.sharknet.javafx.controls.FontIcon;
import net.sharksystem.sharknet.javafx.i18n.I18N;
import net.sharksystem.sharknet.javafx.utils.ReflectionUtils;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.controller.annotations.FXMLViewContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Builder class for creating Controller instances.
 */
public class ControllerBuilder {

	private static final Logger Log = LoggerFactory.getLogger(ControllerBuilder.class);

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private static ControllerBuilder INSTANCE;

	private Map<Class<? extends AbstractController>, ViewContext> controllerContexts;


	/******************************************************************************
	 *
	 * Constructors
	 *
	 ************************************************************************F******/

	private ControllerBuilder() {
		controllerContexts = new WeakHashMap<>();
	}


	public synchronized static ControllerBuilder getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ControllerBuilder();
		}
		return INSTANCE;
	}

	/******************************************************************************
	 *
	 * Builder methods
	 *
	 ******************************************************************************/

	public <T extends AbstractController> ViewContext<T>  createBy(Class<T> controllerClass) throws ControllerLoaderException {
		try {
			return createBy(controllerClass.newInstance(), controllerClass);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ControllerLoaderException("Could not create controller: " + controllerClass.getSimpleName(), e);
		}
	}

	public <T extends AbstractController> ViewContext<T> createBy(T controller, Class<T> controllerClass) throws ControllerLoaderException {

		try {
			// Load meta data
			ControllerMeta meta = new ControllerMeta();
			Controller controllerAnnotation = controllerClass.getAnnotation(Controller.class);

			if (controllerAnnotation != null) {
				if (! controllerAnnotation.title().isEmpty()) {
					String title = I18N.getString(controllerAnnotation.title());
					meta.setTitle(title);
				}

				if (! controllerAnnotation.icon().isEmpty()) {
					meta.setGraphicNode(new FontIcon(controllerAnnotation.icon()));
				}
			}
			// Load all actions
			loadMethodMetaData(controllerClass, controller, meta);

			// Create loader and controller instance

			FXMLLoader loader = createLoader(controller);
			final Parent rootView =  loader.load();

			ViewContext<T> ctx = new ViewContext<>(controller, rootView, meta);
			ctx.setProperty(ViewContext.PROPERTY_CONTROLLER, controller);

			injectDependencies(controllerClass, ctx);

			ReflectionUtils.invokeMethodsWithAnnotation(controllerClass, controller, PostConstruct.class);

			return ctx;
		} catch (Exception ex) {
			throw new ControllerLoaderException(ex);
		}
	}



	private <T extends AbstractController> void injectFieldValue(Field field, ViewContext<T> ctx) {
		Class<?> requiredFieldClass = ctx.getClass();
		Class<?> fieldType = field.getType();

		if (fieldType.equals(requiredFieldClass) || requiredFieldClass.isAssignableFrom(fieldType)) {
			ReflectionUtils.setFieldValue(field, ctx.getController() ,ctx);
		}
	}

	private <T extends AbstractController> FXMLLoader createLoader(T controller) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(controller.getLocation());
		loader.setResources(controller.getResourceBundle());
		loader.setCharset(Charset.forName("UTF-8"));
		loader.setController(controller);
		loader.setControllerFactory(c -> controller);
		return loader;
	}

	private <T extends AbstractController> void injectDependencies(Class<T> controllerClass, ViewContext<T> ctx) {
		for(Field field : ReflectionUtils.getAllFields(controllerClass)) {
			FXMLViewContext contextAnnotation = field.getAnnotation(FXMLViewContext.class);
			if (contextAnnotation != null) {
				injectFieldValue(field, ctx);
			}
		}
	}

	private <T extends AbstractController> void loadMethodMetaData(Class<T> controllerClass, T controller, ControllerMeta meta) {

		List<Method> actionMethods = ReflectionUtils.getMethodsWithAnnotation(controllerClass, Action.class);
		for (Method method : actionMethods) {
			Action action = method.getAnnotation(Action.class);

			ActionEntry actionEntry = new ActionEntry();
			actionEntry.setTooltip(action.tooltip());
			actionEntry.setTitle(action.text());
			actionEntry.setIcon(action::icon);
			actionEntry.setCallback(() -> {
				try {
					Platform.runLater(() -> ReflectionUtils.invokeMethod(controller, method));
				} catch (Exception ex) {
					Log.error("Failed to invoke the controller action {}{}.", controllerClass.getName(), method.getName());
					throw new IllegalStateException("Could not invoke action method " + method, ex);
				}
			});

			meta.actionEntriesProperty().add(actionEntry);

		}
	}
}