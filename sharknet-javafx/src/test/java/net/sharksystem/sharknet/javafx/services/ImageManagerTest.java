package net.sharksystem.sharknet.javafx.services;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import javafx.scene.image.Image;
import net.sharksystem.sharknet.api.Content;
import net.sharksystem.sharknet.api.SharkNet;
import net.sharksystem.sharknet.javafx.App;
import net.sharksystem.sharknet.javafx.modules.SharkNetModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@GuiceModules(SharkNetModule.class)
@RunWith(GuiceTestRunner.class)
public class ImageManagerTest {

	@Inject
	private SharkNet sharkNet;

	@Inject
	private ImageManager imageManager;

	@Test
	public void testReadImageFromValidContent() throws IOException {
		Content validImage = Mockito.mock(Content.class);
		Mockito.when(validImage.getFileExtension()).thenReturn("jpg");
		Mockito.when(validImage.getFile()).thenReturn(App.class.getResource("images/profile-placeholder.jpg").openStream());

		Optional<Image> image = imageManager.readImageFrom(validImage);
		assertTrue(image.isPresent());
	}

	@Test
	public void testReadFromInvalidContentType() throws IOException {
		Content invalidImage = Mockito.mock(Content.class);
		Mockito.when(invalidImage.getFileExtension()).thenReturn("NotAnImage");
		Optional<Image> image = imageManager.readImageFrom(invalidImage);
		assertFalse(image.isPresent());
	}

	@Test
	public void isImage() throws Exception {


		String[] requiredSupportedTypes = {"bmp","jpg","jpeg","png"};

		for(String format : requiredSupportedTypes) {
			Content content = Mockito.mock(Content.class);
			Mockito.when(content.getFileExtension()).thenReturn(format);
			assertTrue("Format must be supported " + format, imageManager.isImage(content));
		}

	}


}
