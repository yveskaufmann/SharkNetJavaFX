package net.sharksystem.sharknet.javafx.utils;


import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import org.apache.commons.io.IOUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;


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

	public static final void printSizeInfo(final Region node) {
		printSizeInfo(node, null);
	}

	public static final void printSizeInfo(final Region node, String alternativeName) {

		Supplier<String> nameSupplier = () -> {

			String name = alternativeName == null? node.getId() : alternativeName;
			if (name == null) {
				name = node.getClass().getSimpleName();
			}
			return name;
		};

		final String nodeName = nameSupplier.get();

		List<String> sizeProperties = Arrays.asList("Width", "Height");
		List<String> sizeAttributes = Arrays.asList("Min", "Pref", "Max");

		/* Print all size information about a node */
		sizeAttributes.stream()
			.flatMap((attr) -> sizeProperties.stream().map((prop) -> "get" + attr + prop ))
			.forEach((method) -> {
				try {
					System.out.println(nodeName + "." + method + "() = " + ReflectionUtils.invokeMethod(node, method));
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			});

		System.out.println(nodeName + ".getLayoutBounds() = " + node.getLayoutBounds());
	}
}
