package com.foxminded.university.utils;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foxminded.university.exception.UnreadableConfigException;

public class ConnectionProvider {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionProvider.class);
	private String url;
	private String userName;
	private String password;
	private Properties properties;

	public ConnectionProvider() {
		properties = getProperties();
		url = getKeyProperty("url");
		userName = getKeyProperty("userName");
		password = getKeyProperty("password");
	}

	public Connection getConnection() throws SQLException {
		logger.debug("try get connection");
		return DriverManager.getConnection(url, userName, password);
	}

	private String getKeyProperty(String key) {
		return properties.getProperty(key);
	}

	private Properties getProperties() {
		Properties properties = new Properties();
		URL url = ConnectionProvider.class.getClassLoader().getResource("db.properties");
		logger.debug("try get properties");
		try (FileInputStream fileInputStream = new FileInputStream(new File(url.getPath()))) {
			properties.load(fileInputStream);
			return properties;
		} catch (Exception e) {
			throw new UnreadableConfigException("Can not read configuration file", e);
		}
	}
}
