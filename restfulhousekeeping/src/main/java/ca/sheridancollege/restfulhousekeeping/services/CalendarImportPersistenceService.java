package ca.sheridancollege.restfulhousekeeping.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.sheridancollege.restfulhousekeeping.beans.ChecklistItem;
import ca.sheridancollege.restfulhousekeeping.beans.Cleaning;
import ca.sheridancollege.restfulhousekeeping.beans.CleaningChecklistItem;
import ca.sheridancollege.restfulhousekeeping.beans.CleaningSource;
import ca.sheridancollege.restfulhousekeeping.beans.Property;
import ca.sheridancollege.restfulhousekeeping.exceptions.CalendarImportException;
import ca.sheridancollege.restfulhousekeeping.models.CalendarPersistenceResult;
import ca.sheridancollege.restfulhousekeeping.models.ImportedReservation;
import ca.sheridancollege.restfulhousekeeping.repositories.ChecklistItemRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.PropertyRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CalendarImportPersistenceService {

	private static final LocalTime DEFAULT_CHECKOUT_TIME = LocalTime.of(11, 0);
	private static final LocalTime DEFAULT_CHECKIN_TIME = LocalTime.of(16, 0);

	private final PropertyRepository propertyRepository;
	private final CleaningRepository cleaningRepository;
	private final ChecklistItemRepository checklistItemRepository;

	@Transactional
	public CalendarPersistenceResult saveReservations(
		Long propertyId,
		List<ImportedReservation> reservations
	) {
		Property property = propertyRepository.findById(propertyId)
			.orElseThrow(() -> new CalendarImportException(
				HttpStatus.NOT_FOUND,
				"Property not found: " + propertyId
			));

		LocalTime checkoutTime = property.getCheckoutTime() == null
			? DEFAULT_CHECKOUT_TIME
			: property.getCheckoutTime();
		LocalTime checkinTime = property.getCheckinTime() == null
			? DEFAULT_CHECKIN_TIME
			: property.getCheckinTime();
		if (!checkinTime.isAfter(checkoutTime)) {
			throw new CalendarImportException(
				HttpStatus.BAD_REQUEST,
				"Property check-in time must be after its checkout time"
			);
		}

		List<ChecklistItem> checklistTemplates =
			checklistItemRepository.findAllByProperty_Id(propertyId);
		List<Long> cleaningIds = new ArrayList<>();
		int skipped = 0;

		for (ImportedReservation reservation : reservations) {
			boolean exists = cleaningRepository
				.existsByProperty_IdAndSourceAndExternalEventUid(
					propertyId,
					CleaningSource.AIRBNB_ICAL,
					reservation.eventUid()
				);

			if (exists) {
				skipped++;
				continue;
			}

			Cleaning cleaning = Cleaning.builder()
				.manager(property.getManager())
				.cleaner(null)
				.property(property)
				.dateTimeStart(reservation.checkOutDate().atTime(checkoutTime))
				.dateTimeEnd(reservation.checkOutDate().atTime(checkinTime))
				.dateTimeStarted(null)
				.dateTimeCompleted(null)
				.notes("Created from Airbnb reservation " + reservation.reservationCode())
				.isComplete(false)
				.source(CleaningSource.AIRBNB_ICAL)
				.externalEventUid(reservation.eventUid())
				.externalReservationCode(reservation.reservationCode())
				.reservationUrl(reservation.reservationUrl())
				.importedCheckInDate(reservation.checkInDate())
				.importedCheckOutDate(reservation.checkOutDate())
				.build();

			List<CleaningChecklistItem> cleaningItems = checklistTemplates.stream()
				.map(template -> CleaningChecklistItem.builder()
					.cleaning(cleaning)
					.checklistItem(template)
					.isComplete(false)
					.build())
				.toList();
			cleaning.setCleaningChecklistItems(new ArrayList<>(cleaningItems));

			Cleaning saved = cleaningRepository.save(cleaning);
			cleaningIds.add(saved.getId());
		}

		return new CalendarPersistenceResult(
			cleaningIds.size(),
			skipped,
			List.copyOf(cleaningIds)
		);
	}
}
