package net.sharksystem.sharknet.api;

import java.io.*;
import java.util.List;


/**
 * Created by timol on 04.06.2016.
 */
public class TestFileInput {
	InputStream in = null;
	OutputStream out = null;
	String in_str;
	String out_str;

	public TestFileInput() throws IOException {
	}


		public Content setFile(String filepath) throws FileNotFoundException {
		in_str = filepath;
		in = new FileInputStream(in_str);
		return new ImplContent(in, "txt", "foo");
		}

	public void copyFile(Content content, String filepath) throws IOException {
		out_str = filepath;
		out = new FileOutputStream(out_str);
		int c;

		while ((c = content.getFile().read()) != -1) {
			out.write(c);
		}

		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}

	}


	public static void main(String[] args) {
		ImplSharkNet s = new ImplSharkNet();
		s.fillWithDummyData();
		List<Feed> flist = s.getFeeds(10, 15);
		System.out.println("foo");

	}

}

