package net.sharksystem.sharknet.javafx.services;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import net.sharksystem.sharknet.api.ImplSharkNet;
import net.sharksystem.sharknet.api.SharkNet;

@Singleton
public class SharkNetProvider implements Provider<SharkNet> {

	@Override
	public SharkNet get() {
		ImplSharkNet sharkNet = new ImplSharkNet();
		sharkNet.fillWithDummyData();
		sharkNet.setProfile(sharkNet.getProfiles().get(1), "");
		return sharkNet;
	}
}
