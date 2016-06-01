package net.sharksystem.sharknet.javafx.controller;

import net.sharksystem.sharknet.javafx.actions.annotations.Controller;
import net.sharksystem.sharknet.javafx.context.AbstractContext;
import net.sharksystem.sharknet.javafx.utils.AbstractController;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;


public class ControllerMetaTest {
	public static final String DUMMY_TITLE = "Title";

	@Controller(title = DUMMY_TITLE)
	private class DummyController  extends AbstractController {
		public DummyController(URL location) {
			super(location);
		}

		@Override
		protected void onFxmlLoaded() {
		}
	}

	@Test
	public void retrieveTitleFromControllerClass() {

	}
}
