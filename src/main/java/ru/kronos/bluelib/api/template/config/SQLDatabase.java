package ru.kronos.bluelib.api.template.config;

import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.LogEngine;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLDatabase {

	private MySQLConfiguration config;
	private Connection c;
	private Statement s;
	private boolean connected;
	
	public SQLDatabase() {
		
	}
	
	public void installConfig(MySQLConfiguration config) throws Exception {
		if (this.config != null)
			throw new Exception("MySQLConfiguration already configured.");
		
		this.config = config;
		if (config.sqlite) {
			Class.forName("org.sqlite.JDBC").newInstance();
		} else {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
	}
	
	public boolean connected() {
		return connected;
	}
	
	public void closeConnection() {
		try {
			s.close();
			c.close();
		} catch (SQLException | NullPointerException e) { e.printStackTrace(); } finally {
			connected = false;
		}
	}
	
	public boolean openConnection() {
		try {
			if (config == null)
				throw new NullPointerException("You must install a database settings.");
			
			if (config.sqlite)
				s = (c = DriverManager.getConnection(config.url)).createStatement();
			else
				s = (c = DriverManager.getConnection(config.url, config.user, config.password)).createStatement();


			LogEngine.debugMsg(LoggingLevel.INFO, "Соединение с базой данных ( Url = ", config.url," ) установлено.");
			return (connected = true);
		} catch (SQLException e) {
			if (config.sqlite)
				LogEngine.debugMsg(LoggingLevel.CRITICAL, SQLDatabase.class.getSimpleName(), " | Ошибка соединения с базой данных SQL. Url = ", config.url, ".");
			else
				LogEngine.debugMsg(LoggingLevel.CRITICAL, SQLDatabase.class.getSimpleName(), " | Ошибка соединения с базой данных SQL. Url = ", config.url, ", User = ", config.user, ", Password = ", config.password, ".");
			e.printStackTrace();
			return (connected = false);
		}
	}
	
	public ResultSet executeQuery(String sql, String... args) {
		try {
			return s.executeQuery(String.format(sql, (Object) args));
		} catch (SQLException | NullPointerException e) { e.printStackTrace(); return null; }
	}
	
	public void executeUpdate(String sql, String... args) {
		try {
			s.executeUpdate(String.format(sql, (Object) args));
		} catch (SQLException | NullPointerException e) { e.printStackTrace(); }
	}
	
	
	
	public static class MySQLConfiguration {
		String user;
		String password;
		String url;
		boolean sqlite;
		
		public MySQLConfiguration(String host, int port, String dbname, String user, String password) {
			this.user = user;
			this.password = password;
			this.url = "jdbc:mysql://"
					.concat(host).concat(":")
					.concat(String.valueOf(port)).concat("/")
					.concat(dbname);
			this.sqlite = false;
		}
		
		public MySQLConfiguration(String dataFolder, String dbname) {
			File file = new File(dataFolder.concat(File.separator).concat(dbname));
			
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) { e.printStackTrace(); }
			}
			
			this.url = "jdbc:sqlite:".concat(file.getAbsolutePath());
			this.sqlite = true;
		}

		public MySQLConfiguration(File dataFolder, String dbname) {
			this(dataFolder.getAbsolutePath(), dbname);
		}
	}
}


