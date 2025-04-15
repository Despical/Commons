/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2024 Despical
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

package me.despical.commons.database;

import com.zaxxer.hikari.HikariDataSource;
import me.despical.commons.configuration.ConfigUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 * @version 1.0.0
 */
public class MySQLDatabase {

	private HikariDataSource hikariDataSource;

	private Logger logger;

	public MySQLDatabase(JavaPlugin plugin, String fileName) {
		this(plugin, ConfigUtils.getConfig(plugin, fileName));
	}

	public MySQLDatabase(JavaPlugin plugin, FileConfiguration config) {
		this(config);
		this.logger = plugin.getLogger();
	}

	public MySQLDatabase(FileConfiguration configuration) {
		this(configuration, "user", "password", "address");
	}

	public MySQLDatabase(FileConfiguration configuration, String userPath, String passwordPath, String jdbcUrlPath) {
		this(configuration.getString(userPath), configuration.getString(passwordPath), configuration.getString(jdbcUrlPath));
	}

	public MySQLDatabase(String user, String password, String jdbcUrl) {
		logger = Logger.getLogger("Commons Database");
		logger.info("Configuring MySQL connection!");

		configureConnPool(user, password, jdbcUrl);

		try (Connection connection = getConnection()) {
			if (connection == null) {
				logger.severe("Failed to connect to database!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public MySQLDatabase(String user, String password, String host, String database, int port) {
		logger = Logger.getLogger("Commons Database");
		logger.info("Configuring MySQL connection!");

		configureConnPool(user, password, host, database, port);

		try (Connection connection = getConnection()) {
			if (connection == null) {
				logger.severe("Failed to connect to database!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void configureConnPool(String user, String password, String jdbcUrl) {
		try {
			logger.info("Creating HikariCP Configuration...");
			HikariDataSource config = new HikariDataSource();
			config.setJdbcUrl(jdbcUrl);
			config.addDataSourceProperty("user", user);
			config.addDataSourceProperty("password", password);
			hikariDataSource = config;
			logger.info("Setting up MySQL Connection pool...");
			logger.info("Connection pool successfully configured. ");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warning("Cannot connect to MySQL database!");
			logger.warning("Check configuration of your database settings!");
		}
	}

	private void configureConnPool(String user, String password, String host, String database, int port) {
		try {
			logger.info("Creating HikariCP Configuration...");
			HikariDataSource config = new HikariDataSource();
			config.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
			config.addDataSourceProperty("serverName", host);
			config.addDataSourceProperty("portNumber", port);
			config.addDataSourceProperty("databaseName", database);
			config.addDataSourceProperty("user", user);
			config.addDataSourceProperty("password", password);
			hikariDataSource = config;
			logger.info("Setting up MySQL Connection pool...");
			logger.info("Connection pool successfully configured. ");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warning("Cannot connect to MySQL database!");
			logger.warning("Check configuration of your database settings!");
		}
	}

	public void executeUpdate(String query) {
		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(query);
			}
		} catch (SQLException e) {
			logger.warning("Failed to execute update: " + query);
		}
	}

	public void shutdownConnPool() {
		try {
			logger.info("Shutting down connection pool. Trying to close all connections.");

			if (!hikariDataSource.isClosed()) {
				hikariDataSource.close();

				logger.info("Pool successfully shutdown.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		Connection conn = null;

		try {
			conn = hikariDataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}