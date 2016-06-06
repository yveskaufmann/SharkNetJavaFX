package net.sharksystem.sharknet.javafx.utils;


import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.apache.commons.io.IOUtils;

public class NodeUtils {

	public static void centerNode(Node node) {
		Parent parent = node.getParent();
		centerNode(node, parent);
	}

	public static void centerNode(Node node, Node parent) {
		Bounds nodeBounds = node.getLayoutBounds();
		Bounds nodeParent = parent.getLayoutBounds();

		if (parent != null) {
			node.setTranslateX((nodeParent.getWidth() - nodeBounds.getWidth()) * 0.5);
			node.setTranslateY((nodeParent.getHeight() - nodeBounds.getHeight()) * 0.5);
		}
	}
}
