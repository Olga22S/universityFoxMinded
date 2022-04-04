package com.foxminded.university.dao.jdbc;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NextIdGenerator {

	private static final Logger logger = LoggerFactory.getLogger(NextIdGenerator.class);

	public static int getNextId(String sequence, Connection connection) throws SQLException {
		logger.debug("trying generate id");
		int nextId = 0;
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT nextval(?)");
		preparedStatement.setString(1, sequence);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			nextId = resultSet.getInt("nextval");
		}
		logger.debug("Id was generated");
		return nextId;
	}
}
