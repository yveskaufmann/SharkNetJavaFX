package net.sharksystem.sharknet.javafx.context;

import javafx.scene.Parent;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import net.sharksystem.sharknet.javafx.utils.controller.ControllerMeta;

public class ViewContext<T extends AbstractController> extends AbstractContext {

	/**
	 * Property for the associated controller.
	 */
	public static final String PROPERTY_CONTROLLER = "controller";

	private Parent rootNode;
	private T controller;
	private ControllerMeta meta;

	public ViewContext(T controller, Parent rootNode, ControllerMeta meta) {
		this.controller = controller;
		this.rootNode = rootNode;
		this.meta = meta;
	}

	public ApplicationContext getApplicationContext() {
		return ApplicationContext.get();
	}

	public T getController() {
		return controller;
	}

	public Parent getRootNode() {
		return rootNode;
	}

	public ControllerMeta getMeta() {
		return meta;
	}
}
