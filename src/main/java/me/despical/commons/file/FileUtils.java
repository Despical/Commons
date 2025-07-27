/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2025 Despical
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.despical.commons.file;

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