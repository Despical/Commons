package me.despical.commonsbox.file;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @author Despical
 * <p>
 * @since 1.1.8
 * Created at 31.10.2020
 */
public class FileUtils {

	/**
	 * Download a file from specified URL to destination file.
	 *
	 * @param url where to download file
	 * @param destination where the output stream will be in
	 * @throws IOException if connection fails
	 */
	public static void copyURLToFile(URL url, File destination) throws IOException {
		ReadableByteChannel channel = Channels.newChannel(url.openStream());
		FileOutputStream stream = new FileOutputStream(destination);

		stream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);

		channel.close();
		stream.close();
	}
}