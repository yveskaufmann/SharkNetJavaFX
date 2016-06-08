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
	public static final int cols = 43;
	public static final int rows = 43;

	public Emoji() {
		emojis = new ArrayList<>();
	}

	public void loadEmojis() {
		URI cssPath = null;
		try {
			cssPath = App.class.getResource("css/emojis.css").toURI();
			List<String> cssLines = Files.readAllLines(Paths.get(cssPath), Charset.defaultCharset());
			for (int i = 0; i < cssLines.size(); i++) {
				if (cssLines.get(i).contains(".emojione-")) {
					String tmp = cssLines.get(i).replace("{", "").replace(".","").trim();
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
