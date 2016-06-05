package net.sharksystem.sharknet.javafx.model;

import net.sharksystem.sharknet.api.ImplSharkNet;
import net.sharksystem.sharknet.api.SharkNet;

/**
 * Created by Benni on 05.06.2016.
 */
public class SharkNetModel {

	private static SharkNetModel instance = null;
	private ImplSharkNet modelSharkNet;

	private SharkNetModel() {
		modelSharkNet = new ImplSharkNet();
		modelSharkNet.fillWithDummyData();
		modelSharkNet.setProfile(modelSharkNet.getProfiles().get(1), "");
	}

	public static SharkNetModel getInstance() {
		if(instance == null) {
			instance = new SharkNetModel();
		}
		return instance;
	}

	public ImplSharkNet getSharkNetImpl() {
		return modelSharkNet;
	}
}
