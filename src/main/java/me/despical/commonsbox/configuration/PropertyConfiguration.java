/*
 * Commons Box - Box of common utilities.
 * Copyright (C) 2020 Despical
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.despical.commonsbox.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class PropertyConfiguration implements Configuration<Properties> {

	private final File dataFolder;

	public PropertyConfiguration(File dataFolder) {
		this.dataFolder = dataFolder;
	}

	@Override
	public Properties getConfiguration(String file) {
		try {
			Properties properties = new Properties();

			properties.load(new FileInputStream(new File(dataFolder, file + ".properties")));

			return properties;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Properties();
	}

	public File getDataFolder() {
		return dataFolder;
	}
}