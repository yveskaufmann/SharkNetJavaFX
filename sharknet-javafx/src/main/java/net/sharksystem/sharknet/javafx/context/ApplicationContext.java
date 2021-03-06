package net.sharksystem.sharknet.javafx.context;


import javafx.application.Application;
import net.sharksystem.sharknet.javafx.modules.SharkNetModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class ApplicationContext extends AbstractContext {

	/******************************************************************************
	 *
	 * Static Stuff
	 *
	 ******************************************************************************/

	private static final Logger Log = LoggerFactory.getLogger(ApplicationContext.class);

	/***
	 * Field of the injector
	 */
	public static final String APPLICATION_FIELD = "applicationFX";

	/***
	 * The context which is shared with the whole application
	 */
	private static ApplicationContext INSTANCE;

	/**
	 * Provides access to our application context.
	 *
	 * @return the application context instance.
     */
	synchronized
	public static ApplicationContext get() {
		if (INSTANCE == null) {
			INSTANCE = new ApplicationContext();
		}
		return INSTANCE;
	}

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/



	private Application application;
	private DIContext diContext = null;
	private boolean isDisposed = true;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	private ApplicationContext() {
		super();
	}

	/******************************************************************************
	 *
	 * Public Api
	 *
	 ******************************************************************************/

	synchronized
	public void init(Application application) {
		if (isDisposed) {
			Log.info("Initializing " + getClass().getSimpleName());
			this.application = application;
			this.diContext = new GuiceInjectorContext(application, () -> Collections.singletonList(new SharkNetModule()));
			this.diContext.init();
			isDisposed = false;
			Log.info("Initialized " + getClass().getSimpleName());
		} else {
			Log.warn(getClass().getSimpleName() + " is already initialized");
		}
	}

	synchronized
	public void destroy() {
		if (! isDisposed) {
			Log.info("Destroying " + getClass().getSimpleName());
			diContext.destroy();
			properties.clear();
			isDisposed = true;
			Log.info("Destroyed " + getClass().getSimpleName());
		} else {
			Log.warn(getClass().getSimpleName() + " was already destroyed");
		}
	}

	/**
	 * Provides access to dependency injector
	 * context, enables callers to get instances
	 * without using annotations.
	 *
	 * @return the current injector context.
     */
	public DIContext getInjector() {
		ensureContextCreated();
		return diContext;
	}

	/**
	 * Get the application to which this context belongs.
	 *
	 * @return the application object.
     */
	public Application getApplication() {
		ensureContextCreated();
		return application;
	}

	synchronized
	private void ensureContextCreated() {
		if (isDisposed) {
			throw new IllegalStateException("Application context wasn't initialized by calling init");
		}
	}
}
