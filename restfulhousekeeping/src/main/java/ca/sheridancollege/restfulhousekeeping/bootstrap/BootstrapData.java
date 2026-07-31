package ca.sheridancollege.restfulhousekeeping.bootstrap;

import java.time.LocalDate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ca.sheridancollege.restfulhousekeeping.beans.Availability;
import ca.sheridancollege.restfulhousekeeping.beans.AvailabilitySlot;
import ca.sheridancollege.restfulhousekeeping.beans.ChecklistItem;
import ca.sheridancollege.restfulhousekeeping.beans.Cleaning;
import ca.sheridancollege.restfulhousekeeping.beans.CleaningChecklistItem;
import ca.sheridancollege.restfulhousekeeping.beans.DayOfWeek;
import ca.sheridancollege.restfulhousekeeping.beans.Organization;
import ca.sheridancollege.restfulhousekeeping.beans.Property;
import ca.sheridancollege.restfulhousekeeping.beans.Role;
import ca.sheridancollege.restfulhousekeeping.beans.User;
import ca.sheridancollege.restfulhousekeeping.repositories.AvailabilityRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.ChecklistItemRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningChecklistItemRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.OrganizationRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.PropertyRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.SlotRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {

	private final OrganizationRepository organizationRepository;
	private final UserRepository userRepository;
	private final PropertyRepository propertyRepository;
	private final CleaningRepository cleaningRepository;
	private final AvailabilityRepository availabilityRepository;
	private final ChecklistItemRepository checklistItemRepository;
	private final CleaningChecklistItemRepository cleaningChecklistItemRepository;
	private final SlotRepository slotRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		if (organizationRepository.count() > 0) {
			return;
		}

		Organization organization1 = Organization.builder().name("The Everything Company")
				.description("We specialize in literally everything.").build();
		User manager1 = User.builder().firstName("Sophie").lastName("Wang").username("sophie").email("sophie@test.com")
				.password(passwordEncoder.encode("password")).phoneNumber("6471231234").role(Role.MANAGER).build();
		User cleaner1 = User.builder().firstName("Robert").lastName("Fleming").username("robert")
				.email("robert@test.com").password(passwordEncoder.encode("password")).phoneNumber("9056083833")
				.role(Role.CLEANER).build();
		User cleaner2 = User.builder().firstName("Katie").lastName("McEwan").username("katie").email("katie@test.com")
				.password(passwordEncoder.encode("password")).phoneNumber("2262240336").role(Role.CLEANER).build();
		Property property1 = Property.builder().name("Wayward Pines").street("1234").unit("123").city("Mississauga")
				.province("Ontario").postalCode("L6D 7N4").country("Canada")
				.accessInstructions("Door locked. Use key under mat.").build();
		Property property2 = Property.builder().name("Union St.").street("101").unit("292").city("Oakville")
				.province("Ontario").postalCode("L4V 1V1").country("Canada")
				.accessInstructions("Jump over the fence and take a right.").build();

		// Cleanings
		Cleaning cleaning1 = Cleaning.builder().dateTimeStart(LocalDateTime.of(2026, 7, 21, 8, 30))
				.dateTimeEnd(LocalDateTime.of(2026, 7, 21, 12, 30))
				.notes("Large pile of asbestos found in toaster oven, bring mask.").build();
		Cleaning cleaning2 = Cleaning.builder().dateTimeStart(LocalDateTime.of(2026, 7, 31, 12, 30))
				.dateTimeEnd(LocalDateTime.of(2026, 7, 31, 14, 30)).notes("Upcoming cleaning 1 with Manager = Sophie.")
				.build();
		Cleaning cleaning3 = Cleaning.builder().dateTimeStart(LocalDateTime.of(2026, 7, 31, 12, 30))
				.dateTimeEnd(LocalDateTime.of(2026, 7, 31, 14, 30)).notes("Upcoming cleaning 2 with Manager = Sophie.")
				.build();
		Cleaning cleaning4 = Cleaning.builder().dateTimeStart(LocalDateTime.of(2026, 7, 31, 12, 30))
				.dateTimeEnd(LocalDateTime.of(2026, 7, 31, 14, 30)).notes("Upcoming cleaning 3 with Manager = Sophie.")
				.build();
		AvailabilitySlot slot1 = AvailabilitySlot.builder().dayOfWeek(DayOfWeek.TUESDAY).startTime(LocalTime.of(9, 0))
				.endTime(LocalTime.of(17, 0)).build();
		AvailabilitySlot slot2 = AvailabilitySlot.builder().dayOfWeek(DayOfWeek.WEDNESDAY).startTime(LocalTime.of(9, 0))
				.endTime(LocalTime.of(17, 0)).build();

		organization1 = organizationRepository.save(organization1);

		// Managers
		manager1.setOrganization(organization1);
		manager1 = userRepository.save(manager1);

		// Cleaners
		cleaner1.setOrganization(organization1);
		cleaner1 = userRepository.save(cleaner1);
		cleaner2.setOrganization(organization1);
		cleaner2 = userRepository.save(cleaner2);

		// Properties
		property1.setManager(manager1); // sophie's property
		property1 = propertyRepository.save(property1);
		property2.setManager(manager1);
		property2 = propertyRepository.save(property2);

		// Cleanings
		cleaning1.setProperty(property1);
		cleaning1.setCleaner(cleaner1);
		cleaning1.setManager(manager1);
		cleaning1 = cleaningRepository.save(cleaning1);
		cleaning2.setProperty(property2);
		cleaning2.setCleaner(cleaner2);
		cleaning2.setManager(manager1);
		cleaning2 = cleaningRepository.save(cleaning2);
		cleaning3.setProperty(property2);
		cleaning3.setCleaner(cleaner1);
		cleaning3.setManager(manager1);
		cleaning3 = cleaningRepository.save(cleaning3);
		cleaning4.setProperty(property1);
		cleaning4.setCleaner(cleaner1);
		cleaning4.setManager(manager1);
		cleaning4 = cleaningRepository.save(cleaning4);

		// Property 1 checklist items
		ChecklistItem kitchen = ChecklistItem.builder().property(property1)
				.description("Clean kitchen counters and sink").frequencyDays(1).build();
		ChecklistItem bathroom = ChecklistItem.builder().property(property1).description("Clean and disinfect bathroom")
				.frequencyDays(1).build();
		ChecklistItem bedding = ChecklistItem.builder().property(property1).description("Change bed linens")
				.frequencyDays(1).build();
		ChecklistItem supplies = ChecklistItem.builder().property(property1)
				.description("Check if any supplies need to be replaced").frequencyDays(15).build();
		checklistItemRepository.saveAll(List.of(kitchen, bathroom, bedding, supplies));

		// Property 2 checklist items
		ChecklistItem kitchen2 = ChecklistItem.builder().property(property2)
				.description("Clean kitchen counters and sink").frequencyDays(1).build();
		ChecklistItem bathroom2 = ChecklistItem.builder().property(property2)
				.description("Clean and disinfect bathroom").frequencyDays(1).build();
		ChecklistItem bedding2 = ChecklistItem.builder().property(property2).description("Change bed linens")
				.frequencyDays(1).build();
		ChecklistItem supplies2 = ChecklistItem.builder().property(property2)
				.description("Check if any supplies need to be replaced").frequencyDays(15).build();
		ChecklistItem hottub = ChecklistItem.builder().property(property2).description("Check water level in hot tub")
				.frequencyDays(7).build();
		checklistItemRepository.saveAll(List.of(kitchen2, bathroom2, bedding2, supplies2, hottub));

		// CleaningChecklistItems
		CleaningChecklistItem cleaning1Kitchen = CleaningChecklistItem.builder().cleaning(cleaning1)
				.checklistItem(kitchen).isComplete(false).build();

		CleaningChecklistItem cleaning1Bathroom = CleaningChecklistItem.builder().cleaning(cleaning1)
				.checklistItem(bathroom).isComplete(false).build();

		CleaningChecklistItem cleaning1Bedding = CleaningChecklistItem.builder().cleaning(cleaning1)
				.checklistItem(bedding).isComplete(false).build();

		CleaningChecklistItem cleaning1Supplies = CleaningChecklistItem.builder().cleaning(cleaning1)
				.checklistItem(supplies).isComplete(false).build();

		cleaningChecklistItemRepository
				.saveAll(List.of(cleaning1Kitchen, cleaning1Bathroom, cleaning1Bedding, cleaning1Supplies));

		cleaningChecklistItemRepository
				.saveAll(List.of(CleaningChecklistItem.builder().cleaning(cleaning2).checklistItem(kitchen2).build(),
						CleaningChecklistItem.builder().cleaning(cleaning2).checklistItem(bathroom2).build(),
						CleaningChecklistItem.builder().cleaning(cleaning2).checklistItem(bedding2).build(),
						CleaningChecklistItem.builder().cleaning(cleaning2).checklistItem(supplies2).build(),
						CleaningChecklistItem.builder().cleaning(cleaning2).checklistItem(hottub).build()));

		// Availabilities
		Availability availability1 = new Availability();
		availability1.setCleaner(cleaner1);
		availability1 = availabilityRepository.save(availability1);
		slot1.setAvailability(availability1);
		slot2.setAvailability(availability1);
		slot1 = slotRepository.save(slot1);
		slot2 = slotRepository.save(slot2);

		// Generate 30 cleanings over the next two weeks
		LocalDate today = LocalDate.now();
		ThreadLocalRandom random = ThreadLocalRandom.current();

		List<Property> properties = List.of(property1, property2);
		List<User> cleaners = List.of(cleaner1, cleaner2);

		for (int i = 0; i < 30; i++) {
			// Random date from tomorrow through 14 days from today
			LocalDate cleaningDay = today.plusDays(random.nextInt(1, 15));

			// Random start time between 8:00 AM and 4:30 PM
			int hour = random.nextInt(8, 17);
			int minute = random.nextBoolean() ? 0 : 30;

			LocalDateTime start = cleaningDay.atTime(hour, minute);
			LocalDateTime end = start.plusHours(random.nextInt(2, 5));

			Cleaning cleaning = Cleaning.builder().dateTimeStart(start).dateTimeEnd(end)
					.notes("Automatically generated cleaning " + (i + 1)).build();

			// Alternate between the two existing properties
			cleaning.setProperty(properties.get(i % properties.size()));
			cleaning.setManager(manager1);

			// Assign 20 cleanings and leave every third cleaning unassigned
			if (i % 3 != 0) {
				cleaning.setCleaner(cleaners.get(random.nextInt(cleaners.size())));
			}

			cleaningRepository.save(cleaning);
		}

		// Generate 5 cleanings for today
		LocalDate currentDate = LocalDate.now();
		ThreadLocalRandom todayRandom = ThreadLocalRandom.current();

		List<Property> todayProperties = List.of(property1, property2);
		List<User> availableCleaners = List.of(cleaner1, cleaner2);

		for (int i = 0; i < 5; i++) {
			// Random start between 8:00 AM and 5:30 PM
			int hour = todayRandom.nextInt(8, 18);
			int minute = todayRandom.nextBoolean() ? 0 : 30;

			LocalDateTime start = currentDate.atTime(hour, minute);
			LocalDateTime end = start.plusHours(todayRandom.nextInt(2, 4));

			Cleaning cleaning = Cleaning.builder().dateTimeStart(start).dateTimeEnd(end)
					.notes("Automatically generated cleaning for today " + (i + 1)).build();

			cleaning.setProperty(todayProperties.get(i % todayProperties.size()));
			cleaning.setManager(manager1);

			// Assign cleanings 1, 3, and 5; leave 2 and 4 unassigned
			if (i % 2 == 0) {
				cleaning.setCleaner(availableCleaners.get(todayRandom.nextInt(availableCleaners.size())));
			}

			cleaningRepository.save(cleaning);
		}
	}
}
