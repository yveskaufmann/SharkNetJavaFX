package net.sharksystem.sharknet.javafx.controller.chat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Benni on 06.06.2016.
 */
public class Emoji {

	private BufferedImage bufImage;
	private BufferedImage[][] emojis;
	private String fileName;
	public static final int cols = 43;
	public static final int rows = 43;
	private final int width = 64;
	private final int height = 64;
	private InputStream in;


	public Emoji(InputStream in) {
		this.in = in;
		emojis = new BufferedImage[cols][rows];
	}

	public void loadEmojis() {
		try {
			bufImage = ImageIO.read(in);
			for (int i = 0; i < cols; i++)
			{
				for (int j = 0; j < rows; j++)
				{
					emojis[i][j] = bufImage.getSubimage(
						j * width,
						i * height,
						width,
						height
					);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage[][] getEmojis() {
		return emojis;
	}
}
