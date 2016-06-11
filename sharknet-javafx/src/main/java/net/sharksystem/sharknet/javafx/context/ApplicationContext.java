package net.sharksystem.sharknet.javafx.context;


import com.google.inject.Injector;
import javafx.application.Application;
import net.sharksystem.sharknet.javafx.modules.SharkNetModule;

import java.util.Collections;

public class ApplicationContext extends AbstractContext {

	/******************************************************************************
	 *
	 * Static Stuff
	 *
	 ******************************************************************************/
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

	public void init(Application application) {
		this.application = application;
		this.diContext = new GuiceInjectorContext(application, () -> Collections.singletonList(new SharkNetModule()));
		this.diContext.init();
	}

	public void destroy() {
		ensureContextCreated();
		diContext.destroy();
		properties.clear();
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

	private void ensureContextCreated() {
		if (diContext == null) {
			throw new IllegalStateException("Application context wasn't initialized by calling init");
		}
	}
}
