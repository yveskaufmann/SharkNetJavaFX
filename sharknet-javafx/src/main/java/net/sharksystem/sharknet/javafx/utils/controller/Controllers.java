package net.sharksystem.sharknet.javafx.utils.controller;

import net.sharksystem.sharknet.javafx.context.ViewContext;
import net.sharksystem.sharknet.javafx.controller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Controller Registry
 */
public class Controllers {

	private static final Logger Log = LoggerFactory.getLogger(Controllers.class);

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private static Controllers INSTANCE;

	private Map<Class<? extends AbstractController>, ViewContext> controllerMap;


	/******************************************************************************
	 *
	 * Constructors
	 *
	 ************************************************************************F******/

	private Controllers() {
		controllerMap = new HashMap<>();
	}


	public synchronized static Controllers getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Controllers();
		}
		return INSTANCE;
	}

	public void registerController(Class<? extends AbstractController> controllerType) {
		if (! controllerMap.containsKey(controllerType)) {
			String controllerName = controllerType.getSimpleName();
			try {
				AbstractController controllerInstance = (AbstractController) controllerType.newInstance();
				controllerMap.put(controllerType, controllerInstance.getContext());
			} catch (InstantiationException | IllegalAccessException e) {
				Log.warn("Failed to register {}: Could not instantiate a instance ", controllerName, e);
			}
		}
	};

	public void unregisterController(Class<? extends AbstractController> controllerType) {
		if (controllerMap.containsKey(controllerType)) {
			ViewContext ctx = controllerMap.remove(controllerType);
			Log.debug("Shutdown controller {}", controllerType.getName());
			ctx.getController().onShutdown();
		}
	};

	public <T extends AbstractController> T get(Class<T> controllerType) {
		ViewContext<T> ctx = getContext(controllerType);
		return  ctx != null ? ctx.getController() : null;
	}

	public <T extends AbstractController> ViewContext<T> getContext(Class<T> controllerType) {
		if (controllerMap.containsKey(controllerType)) {
			return controllerMap.get(controllerType);
		}
		return null;
	}

	// TODO register by context
}