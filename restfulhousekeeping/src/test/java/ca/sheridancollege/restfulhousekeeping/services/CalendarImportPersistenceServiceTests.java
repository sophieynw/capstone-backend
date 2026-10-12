package ca.sheridancollege.restfulhousekeeping.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ca.sheridancollege.restfulhousekeeping.beans.Cleaning;
import ca.sheridancollege.restfulhousekeeping.beans.CleaningSource;
import ca.sheridancollege.restfulhousekeeping.models.CalendarPersistenceResult;
import ca.sheridancollege.restfulhousekeeping.models.ImportedReservation;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningRepository;

@SpringBootTest
@Transactional
class CalendarImportPersistenceServiceTests {

	@Autowired
	private CalendarImportPersistenceService persistenceService;

	@Autowired
	private CleaningRepository cleaningRepository;

	@Test
	void createsCleaningWithChecklistAndSkipsDuplicateEventUid() {
		ImportedReservation reservation = new ImportedReservation(
			"test-reservation-uid@airbnb.com",
			"TESTCODE123",
			"https://www.airbnb.com/hosting/reservations/details/TESTCODE123",
			LocalDate.of(2026, 10, 31),
			LocalDate.of(2026, 11, 6)
		);

		CalendarPersistenceResult first = persistenceService.saveReservations(
			1L,
			List.of(reservation)
		);
		CalendarPersistenceResult second = persistenceService.saveReservations(
			1L,
			List.of(reservation)
		);

		assertThat(first.created()).isEqualTo(1);
		assertThat(first.skipped()).isZero();
		assertThat(second.created()).isZero();
		assertThat(second.skipped()).isEqualTo(1);

		Cleaning cleaning = cleaningRepository.findById(first.cleaningIds().getFirst())
			.orElseThrow();
		assertThat(cleaning.getSource()).isEqualTo(CleaningSource.AIRBNB_ICAL);
		assertThat(cleaning.getCleaner()).isNull();
		assertThat(cleaning.getIsComplete()).isFalse();
		assertThat(cleaning.getDateTimeStart())
			.isEqualTo(LocalDateTime.of(2026, 11, 6, 11, 0));
		assertThat(cleaning.getDateTimeEnd())
			.isEqualTo(LocalDateTime.of(2026, 11, 6, 16, 0));
		assertThat(cleaning.getCleaningChecklistItems()).isNotEmpty();
	}
}
