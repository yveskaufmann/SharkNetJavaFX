package net.sharksystem.sharknet.javafx.controller;

import net.sharksystem.sharknet.javafx.utils.controller.annotations.Controller;
import net.sharksystem.sharknet.javafx.utils.controller.AbstractController;
import org.junit.Test;

import java.net.URL;


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
