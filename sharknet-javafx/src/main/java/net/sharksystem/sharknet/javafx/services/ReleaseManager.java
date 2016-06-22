package net.sharksystem.sharknet.javafx.services;

import com.google.inject.Singleton;
import net.sharksystem.sharknet.javafx.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


/***
 * Handles release specific tasks such as determines this current version
 * and check for new versions.
 */
@Singleton
public class ReleaseManager {

	/**
	 * ReleaseManager logger class
	 */
	private static final Logger Log = LoggerFactory.getLogger(ReleaseManager.class);

	/**
	 * The current implementation version of the application.
	 */
	private final String currentVersion;

	@Inject
	public ReleaseManager() {
		currentVersion = App.class.getPackage().getImplementationVersion();
	}

	/**
	 * @return the current version of this release or DEVELOPMENT if the app is running in an ide without maven.
     */
	public String getCurrentVersion() {
		return currentVersion !=null ? currentVersion : "DEVELOPMENT";
	}

}
