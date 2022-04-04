package com.foxminded.university.dao.jdbc;

import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foxminded.university.dao.GenericDao;
import com.foxminded.university.entity.Audience;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.utils.ConnectionProvider;

public class AudienceJdbcDao implements GenericDao<Audience> {

	private static final Logger logger = LoggerFactory.getLogger(AudienceJdbcDao.class);
	private static final String INSERT = "INSERT INTO audiences (audience_id, number) VALUES(?,?)";
	private static final String SELECT = "SELECT * FROM audiences WHERE audience_id = ?";
	private static final String UPDATE = "UPDATE audiences SET number=? WHERE audience_id=?";
	private static final String DELETE = "DELETE FROM audiences WHERE audience_id=?";
	private static final String SELECT_ALL = "SELECT audience_id, number FROM audiences";
	private ConnectionProvider connectionProvider;

	public AudienceJdbcDao(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	@Override
	public void add(Audience audience) {
		logger.debug("trying add new audience={}", audience);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
			audience.setId(NextIdGenerator.getNextId("audience_sequence", connection));
			preparedStatement.setInt(1, audience.getId());
			preparedStatement.setInt(2, audience.getNumber());
			preparedStatement.executeUpdate();
			logger.debug("audience was added");
		} catch (SQLException e) {
			throw new DaoException("Error when creating an audience", e);
		}
	}

	@Override
	public Audience getById(int id) {
		logger.debug("trying get audience by id={}", id);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				logger.debug("audience was gotten by id={}", id);
				return mapToAudience(resultSet);
			}
		} catch (SQLException e) {
			throw new DaoException("Error when getting audience by id", e);
		}
		return null;
	}

	@Override
	public void update(Audience audience) {
		logger.debug("trying update audience={}", audience);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setInt(1, audience.getNumber());
			preparedStatement.setInt(2, audience.getId());
			preparedStatement.executeUpdate();
			logger.debug("audience was updated");
		} catch (SQLException e) {
			throw new DaoException("Error when updating audience", e);
		}
	}

	@Override
	public void remove(Audience audience) {
		logger.debug("trying remove audience={}", audience);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
			preparedStatement.setInt(1, audience.getId());
			preparedStatement.executeUpdate();
			logger.debug("audience={} was removed", audience);
		} catch (SQLException e) {
			throw new DaoException("Error when removing audience", e);
		}
	}

	@Override
	public List<Audience> getAll() {
		List<Audience> audiences = new ArrayList<>();
		logger.debug("trying get all audiences");
		try (Connection connection = connectionProvider.getConnection();
				ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
			while (resultSet.next()) {
				audiences.add(mapToAudience(resultSet));
			}
			logger.debug("audiences were gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting all audiences", e);

		}
		return audiences;
	}

	private Audience mapToAudience(ResultSet resultSet) throws SQLException {
		return new Audience(resultSet.getInt("audience_id"), resultSet.getInt("number"));
	}
}
