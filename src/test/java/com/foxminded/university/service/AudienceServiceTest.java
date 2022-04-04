package com.foxminded.university.service;

import static org.junit.Assert.assertEquals;
import static com.foxminded.university.service.AudienceServiceTest.TestData.*;
import static org.mockito.Mockito.*;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import com.foxminded.university.dao.jdbc.AudienceJdbcDao;
import com.foxminded.university.entity.Audience;

@RunWith(MockitoJUnitRunner.class)
public class AudienceServiceTest {

	@Mock
	private AudienceJdbcDao audienceJdbcDao;

	@InjectMocks
	private AudienceService audienceService;

	@Test
	public void givenAudienceId_whenGetById_thenAudienceReturned() {
		Audience expected = audience;
		when(audienceJdbcDao.getById(1)).thenReturn(expected);

		Audience actual = audienceService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	public void givenAudience_whenUpdate_thenAudienceUpdated() {
		audienceService.update(audience);
		verify(audienceJdbcDao).update(audience);
	}

	@Test
	public void givenAudience_whenAdd_thenAudienceAdded() {
		audienceService.add(audience2);
		verify(audienceJdbcDao).add(audience2);
	}

	@Test
	public void givenAudience_whenRemove_thenAudienceRemoved() {
		audienceService.remove(audience);
		verify(audienceJdbcDao).remove(audience);
	}

	@Test
	public void whenGetAudiences_thenAudiencesReturned() {
		List<Audience> expected = new ArrayList<>();
		expected.add(audience);
		expected.add(audience2);
		when(audienceJdbcDao.getAll()).thenReturn(expected);

		List<Audience> actual = audienceService.getAudiences();

		assertEquals(expected, actual);
	}

	interface TestData {
		Audience audience = new Audience(1, 111);
		Audience audience2 = new Audience(2, 222);
	}
}
