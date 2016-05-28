package net.sharksystem.sharknet.javafx.context;


public class ApplicationContext extends AbstractContext {

	private static ApplicationContext INSTANCE;

	private ApplicationContext() {
		super();
	}

	synchronized
	public static ApplicationContext getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ApplicationContext();
		}
		return INSTANCE;
	}
}
