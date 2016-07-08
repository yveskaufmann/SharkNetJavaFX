package net.sharksystem.sharknet.javafx.controller.chat;

import net.sharksystem.sharknet.javafx.App;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benni on 06.06.2016.
 */
public class Emoji {

	private List<String> emojis;
	private static Emoji emojiInstance = null;
	public static int cols = 43;
	public static int rows = 43;

	private Emoji() {
		emojis = new ArrayList<>();
		loadEmojis();
	}

	// singleton
	public static Emoji getInstance() {
		if (emojiInstance == null) {
			emojiInstance = new Emoji();
		}
		return emojiInstance;
	}

	/**
	 * loading emoji classes into array from css file
	 */
	private void loadEmojis() {
		URI cssPath = null;
		try {
			cssPath = App.class.getResource("css/emojis.css").toURI();
			List<String> cssLines = Files.readAllLines(Paths.get(cssPath), Charset.defaultCharset());
			for (String line : cssLines) {
				if (line.contains(".emojione-")) {
					String tmp = line.replace("{", "").replace(".","").trim();
					emojis.add(tmp);
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getEmojis() {
		return emojis;
	}
}
