package net.sharksystem.sharknet.javafx.services;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.scene.image.Image;
import net.sharksystem.sharknet.api.*;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.modules.SharkNetModule;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.Assert.*;

@GuiceModules(SharkNetModule.class)
@RunWith(GuiceTestRunner.class)
public class ImageManagerTest {

	@Inject
	private SharkNet sharkNet;

	@Inject
	private ImageManager imageManager;

	@Test
	public void testReadImageFromContent() throws IOException {
		InputStream in = App.class.getResource("images/profile-placeholder.jpg").openStream();
		Content picture = new ImplContent(in, "jpg", "");
		Optional<Image> image = imageManager.readSync(picture);
		assertTrue(image.isPresent());
	}

}
