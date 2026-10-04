package ca.sheridancollege.restfulhousekeeping.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.sheridancollege.restfulhousekeeping.beans.Property;
import ca.sheridancollege.restfulhousekeeping.exceptions.CalendarImportException;
import ca.sheridancollege.restfulhousekeeping.models.CalendarImportResponse;
import ca.sheridancollege.restfulhousekeeping.models.CalendarParseResult;
import ca.sheridancollege.restfulhousekeeping.models.CalendarPersistenceResult;
import ca.sheridancollege.restfulhousekeeping.models.ImportedReservation;
import ca.sheridancollege.restfulhousekeeping.repositories.PropertyRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CalendarImportService {

	private final PropertyRepository propertyRepository;
	private final CalendarDownloadService downloadService;
	private final AirbnbCalendarParser calendarParser;
	private final CalendarImportPersistenceService persistenceService;

	public CalendarImportResponse importCalendar(
		Long propertyId,
		String icalUrl,
		String authenticatedUsername
	) {
		if (icalUrl == null || icalUrl.isBlank()) {
			throw new CalendarImportException(
				HttpStatus.BAD_REQUEST,
				"icalUrl is required"
			);
		}
		if (icalUrl.length() > 2000) {
			throw new CalendarImportException(
				HttpStatus.BAD_REQUEST,
				"icalUrl must not exceed 2000 characters"
			);
		}

		Property property = propertyRepository.findById(propertyId)
			.orElseThrow(() -> new CalendarImportException(
				HttpStatus.NOT_FOUND,
				"Property not found: " + propertyId
			));
		if (property.getManager() == null
			|| !Objects.equals(property.getManager().getUsername(), authenticatedUsername)) {
			throw new CalendarImportException(
				HttpStatus.FORBIDDEN,
				"You can only import a calendar for a property you manage"
			);
		}

		byte[] calendarBytes = downloadService.download(icalUrl);
		CalendarParseResult parsed = calendarParser.parse(calendarBytes);
		LocalDate today = LocalDate.now();
		List<ImportedReservation> currentReservations = parsed.reservations().stream()
			.filter(reservation -> !reservation.checkOutDate().isBefore(today))
			.toList();
		CalendarPersistenceResult persisted = persistenceService.saveReservations(
			propertyId,
			currentReservations
		);

		int reservationCount = parsed.reservations().size();
		int pastReservationCount = reservationCount - currentReservations.size();
		return new CalendarImportResponse(
			propertyId,
			parsed.eventsFound(),
			reservationCount,
			persisted.created(),
			persisted.skipped(),
			pastReservationCount,
			parsed.eventsFound() - reservationCount,
			persisted.cleaningIds()
		);
	}
}
