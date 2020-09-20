package me.despical.commonsbox.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zaxxer.hikari.HikariDataSource;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 * @version 1.0.0
 */
public class MysqlDatabase {

	private HikariDataSource hikariDataSource;
	private final Logger databaseLogger = Logger.getLogger("Commons-Box Database");

	public MysqlDatabase(String user, String password, String jdbcUrl) {
		databaseLogger.log(Level.INFO, "Configuring MySQL connection!");
		configureConnPool(user, password, jdbcUrl);

		try (Connection connection = getConnection()) {
			if (connection == null) {
				databaseLogger.log(Level.SEVERE, "Failed to connect to database!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public MysqlDatabase(String user, String password, String host, String database, int port) {
		databaseLogger.log(Level.INFO, "Configuring MySQL connection!");
		configureConnPool(user, password, host, database, port);

		try (Connection connection = getConnection()) {
			if (connection == null) {
				databaseLogger.log(Level.SEVERE, "Failed to connect to database!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void configureConnPool(String user, String password, String jdbcUrl) {
		try {
			databaseLogger.info("Creating HikariCP Configuration...");
			HikariDataSource config = new HikariDataSource();
			config.setJdbcUrl(jdbcUrl);
			config.addDataSourceProperty("user", user);
			config.addDataSourceProperty("password", password);
			hikariDataSource = config;
			databaseLogger.info("Setting up MySQL Connection pool...");
			databaseLogger.info("Connection pool successfully configured. ");
		} catch (Exception e) {
			e.printStackTrace();
			databaseLogger.warning("Cannot connect to MySQL database!");
			databaseLogger.warning("Check configuration of your database settings!");
		}
	}

	private void configureConnPool(String user, String password, String host, String database, int port) {
		try {
			databaseLogger.info("Creating HikariCP Configuration...");
			HikariDataSource config = new HikariDataSource();
			config.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
			config.addDataSourceProperty("serverName", host);
			config.addDataSourceProperty("portNumber", port);
			config.addDataSourceProperty("databaseName", database);
			config.addDataSourceProperty("user", user);
			config.addDataSourceProperty("password", password);
			hikariDataSource = config;
			databaseLogger.info("Setting up MySQL Connection pool...");
			databaseLogger.info("Connection pool successfully configured. ");
		} catch (Exception e) {
			e.printStackTrace();
			databaseLogger.warning("Cannot connect to MySQL database!");
			databaseLogger.warning("Check configuration of your database settings!");
		}
	}

	public void executeUpdate(String query) {
		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(query);
			}
		} catch (SQLException e) {
			databaseLogger.warning("Failed to execute update: " + query);
		}
	}

	public ResultSet executeQuery(String query) {
		throw new UnsupportedOperationException("Queries should be created with own auto-closeable connection!");
	}

	public void shutdownConnPool() {
		try {
			databaseLogger.info("Shutting down connection pool. Trying to close all connections.");

			if (!hikariDataSource.isClosed()) {
				hikariDataSource.close();
				databaseLogger.info("Pool successfully shutdown.");
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
}