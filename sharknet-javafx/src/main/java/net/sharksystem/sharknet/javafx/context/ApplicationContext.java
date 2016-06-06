package net.sharksystem.sharknet.javafx.context;


import com.google.inject.Injector;

public class ApplicationContext extends AbstractContext {

	/******************************************************************************
	 *
	 * Constants
	 *
	 ******************************************************************************/
	/***
	 * Field of the injector
	 */
	public static final String INJECTOR_FIELD = "injector";

	/***
	 * The context which is shared with the whole application
	 */
	private static ApplicationContext INSTANCE;

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
	 * Methods
	 *
	 ******************************************************************************/

	synchronized
	public static ApplicationContext getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ApplicationContext();
		}
		return INSTANCE;
	}

	public void registerInjector(Injector injector) {
		setProperty(INJECTOR_FIELD, injector);
	}

	public Injector getInjector() {
		return getProperty(INJECTOR_FIELD, Injector.class);
	}
}
