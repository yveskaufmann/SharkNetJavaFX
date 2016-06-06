package net.sharksystem.sharknet.javafx.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.sharksystem.sharknet.api.ImplSharkNet;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.services.ImageManager;

/***
 * This module contains all bindings for the SharkNet Api.
 *
 * For more information about guice injection modules
 * see: https://github.com/google/guice/wiki/GettingStarted.
 */
public class SharkNetModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ImageManager.class).in(Singleton.class);
	}

	@Provides
	@Singleton
	SharkNet provideSharkNetApiInstance() {
		ImplSharkNet sharkNet = new ImplSharkNet();
		sharkNet.fillWithDummyData();
		sharkNet.setProfile(sharkNet.getProfiles().get(1), "");
		return sharkNet;
	}

}
