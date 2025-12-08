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

package dev.despical.commons.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.despical.commons.configuration.ConfigUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 * @version 1.0.0
 */
public class MySQLDatabase {

	private Logger logger;
	private final HikariDataSource hikariDataSource;

	/**
	 * Initializes the database using a custom config file name within a plugin's folder.
	 *
	 * @param plugin   The JavaPlugin instance.
	 * @param fileName The name of the config file (e.g., "database").
	 */
	public MySQLDatabase(JavaPlugin plugin, String fileName) {
		this(plugin, ConfigUtils.getConfig(plugin, fileName));
	}

	/**
	 * Initializes the database using a plugin's FileConfiguration.
	 * Uses the plugin's built-in logger.
	 *
	 * @param plugin The JavaPlugin instance, used to get the logger.
	 * @param config The FileConfiguration containing database credentials.
	 */
	public MySQLDatabase(JavaPlugin plugin, FileConfiguration config) {
		this.logger = plugin.getLogger();
		this.logger.info("Configuring MySQL connection using plugin config.");

		String user = config.getString("user", "root");
		String password = config.getString("password", "");
		String host = config.getString("host", "localhost");
		String database = config.getString("database", "test");
		int port = config.getInt("port", 3306);

		this.hikariDataSource = configureConnPool(user, password, host, database, port);

		boolean testOnStartup = config.getBoolean("test-on-startup");

		if (testOnStartup) {
			logger.info("Config requests a connection test on startup...");
			testConnection();
			return;
		}

		logger.info("Skipping connection test on startup (test-on-startup is false or not set).");
	}

	public MySQLDatabase(FileConfiguration configuration) {
		this(configuration, "user", "password", "address");
	}

	public MySQLDatabase(FileConfiguration configuration, String userPath, String passwordPath, String jdbcUrlPath) {
		this(configuration.getString(userPath), configuration.getString(passwordPath), configuration.getString(jdbcUrlPath));
	}

	/**
	 * Initializes the database using a full JDBC URL.
	 * Uses a default standalone logger.
	 *
	 * @param user     The database username.
	 * @param password The database password.
	 * @param jdbcUrl  The full JDBC connection URL.
	 */
	public MySQLDatabase(String user, String password, String jdbcUrl) {
		this.logger = Logger.getLogger("Commons Database");
		this.logger.info("Configuring MySQL connection using JDBC URL.");
		this.hikariDataSource = configureConnPool(user, password, jdbcUrl);
		testConnection();
	}

	/**
	 * Initializes the database using detailed connection properties.
	 * Uses a default standalone logger.
	 *
	 * @param user     The database username.
	 * @param password The database password.
	 * @param host     The database host address.
	 * @param database The name of the database.
	 * @param port     The port number for the database.
	 */
	public MySQLDatabase(String user, String password, String host, String database, int port) {
		this.logger = Logger.getLogger("Commons Database");
		this.logger.info("Configuring MySQL connection using connection details.");
		this.hikariDataSource = configureConnPool(user, password, host, database, port);
		testConnection();
	}

	private HikariDataSource configureConnPool(String user, String password, String jdbcUrl) {
		logger.info("Creating HikariCP Configuration.");

		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(user);
		config.setPassword(password);

		addCommonHikariSettings(config);

		try {
			logger.info("Setting up MySQL Connection pool.");

			HikariDataSource dataSource = new HikariDataSource(config);
			logger.info("Connection pool successfully configured.");
			return dataSource;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Cannot connect to MySQL database! Check configuration.", exception);
			return null;
		}
	}

	private HikariDataSource configureConnPool(String user, String password, String host, String database, int port) {
		logger.info("Creating HikariCP Configuration.");
		HikariConfig config = new HikariConfig();
		config.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
		config.addDataSourceProperty("serverName", host);
		config.addDataSourceProperty("portNumber", port);
		config.addDataSourceProperty("databaseName", database);
		config.setUsername(user);
		config.setPassword(password);

		addCommonHikariSettings(config);

		try {
			logger.info("Setting up MySQL Connection pool.");
			HikariDataSource dataSource = new HikariDataSource(config);

			logger.info("Connection pool successfully configured.");
			return dataSource;
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Cannot connect to MySQL database! Check configuration.", exception);
			return null;
		}
	}

	/**
	 * Adds recommended common settings to a HikariConfig instance for performance.
	 *
	 * @param config The HikariConfig instance to modify.
	 */
	private void addCommonHikariSettings(HikariConfig config) {
		config.setMaximumPoolSize(10);
		config.setMinimumIdle(2);
		config.setConnectionTimeout(30000);
		config.setIdleTimeout(600000);
		config.setMaxLifetime(1800000);

		// MySQL-specific optimizations
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", "true");
		config.addDataSourceProperty("useLocalSessionState", "true");
		config.addDataSourceProperty("rewriteBatchedStatements", "true");
		config.addDataSourceProperty("cacheResultSetMetadata", "true");
		config.addDataSourceProperty("cacheServerConfiguration", "true");
		config.addDataSourceProperty("elideSetAutoCommits", "true");
		config.addDataSourceProperty("maintainTimeStats", "false");
	}

	/**
	 * Tests the connection pool by attempting to get and validate a connection.
	 */
	private void testConnection() {
		if (hikariDataSource == null) {
			logger.severe("Database initialization failed. HikariDataSource is null.");
			return;
		}

		try (Connection connection = getConnection()) {
			if (connection == null || !connection.isValid(1)) {
				logger.severe("Failed to establish a valid database connection!");
			} else {
				logger.info("Database connection test successful.");
			}
		} catch (SQLException exception) {
			logger.log(Level.SEVERE, "Database connection test failed!", exception);
		}
	}

	/**
	 * WARNING: This method is NOT SAFE and is vulnerable to SQL INJECTION.
	 * Only use this for static queries that do not involve user input (e.g., CREATE TABLE).
	 *
	 * @param query The SQL query to execute.
	 * @deprecated Use {@link #executePreparedUpdate} to prevent SQL injection vulnerabilities.
	 */
	@Deprecated
	public void executeUpdate(String query) {
		try (Connection connection = getConnection();
			 Statement statement = connection.createStatement()) {

			statement.executeUpdate(query);
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Failed to execute update: " + query, exception);
		} catch (NullPointerException exception) {
			logger.log(Level.SEVERE, "Database connection is not available. Cannot execute update.", exception);
		}
	}

	/**
	 * SECURE: Executes an update query using a PreparedStatement to prevent SQL injection.
	 * USE THIS METHOD for INSERT, UPDATE, or DELETE operations.
	 *
	 * @param query  The SQL query to execute (e.g., "INSERT INTO players (uuid, name) VALUES (?, ?)")
	 * @param params The parameters to be set for the '?' placeholders, in order.
	 */
	public void executePreparedUpdate(String query, Object... params) {
		try (Connection connection = getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {

			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}

			statement.executeUpdate();

		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Failed to execute prepared update: " + query, exception);
		} catch (NullPointerException exception) {
			logger.log(Level.SEVERE, "Database connection is not available. Cannot execute prepared update.", exception);
		}
	}

	/**
	 * Safely shuts down the connection pool.
	 */
	public void shutdownConnPool() {
		if (hikariDataSource != null && !hikariDataSource.isClosed()) {
			try {
				logger.info("Shutting down connection pool...");
				hikariDataSource.close();
				logger.info("Pool successfully shutdown.");
			} catch (Exception exception) {
				logger.log(Level.SEVERE, "Error shutting down connection pool", exception);
			}
		}
	}

	/**
	 * Retrieves a database connection from the connection pool.
	 *
	 * @return A SQL Connection object.
	 * @throws SQLException If a connection cannot be obtained.
	 */
	public Connection getConnection() throws SQLException {
		if (hikariDataSource == null) {
			throw new SQLException("Database is not initialized (HikariDataSource is null).");
		}

		return hikariDataSource.getConnection();
	}

	/**
	 * Sets a new logger instance.
	 *
	 * @param logger The new logger to use.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}