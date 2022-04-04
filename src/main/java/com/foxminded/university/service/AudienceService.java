package com.foxminded.university.service;

import static java.util.Objects.isNull;
import java.util.*;
import com.foxminded.university.dao.jdbc.AudienceJdbcDao;
import com.foxminded.university.entity.Audience;
import com.foxminded.university.exception.EntityNotFoundException;

public class AudienceService {

	private AudienceJdbcDao audienceJdbcDao;

	public AudienceService(AudienceJdbcDao audienceJdbcDao) {
		this.audienceJdbcDao = audienceJdbcDao;
	}

	public void add(Audience audience) {
		audienceJdbcDao.add(audience);
	}

	public Audience getById(int id) {
		Audience audience = audienceJdbcDao.getById(id);
		if (isNull(audience)) {
			throw new EntityNotFoundException("Audience by id=" + id + " was not found");
		} else {
			return audience;
		}
	}

	public void update(Audience audience) {
		audienceJdbcDao.update(audience);
	}

	public void remove(Audience audience) {
		audienceJdbcDao.remove(audience);
	}

	public List<Audience> getAudiences() {
		return audienceJdbcDao.getAll();
	}
}
