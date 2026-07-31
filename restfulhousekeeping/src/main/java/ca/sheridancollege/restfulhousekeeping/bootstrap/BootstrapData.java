package ca.sheridancollege.restfulhousekeeping.bootstrap;

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
	
	// the following two functions can be used to generate a "random" completedDaysAgo number from 1 to 60 days
	
	private int randomInt(int min, int max) {
	    return ThreadLocalRandom.current()
	        .nextInt(min, max + 1);
	}
	
	private ChecklistItem generateChecklistItem(Property property, String description, Integer frequencyDays, Integer completedDaysAgo) {
		return ChecklistItem.builder()
				.property(property)
				.description(description)
				.frequencyDays(frequencyDays)
				.lastCompleted(LocalDate.now().minusDays(randomInt(1, 60)).atStartOfDay())
				.build();
	}
		
//	private ChecklistItem generateChecklistItem(Property property, String description, Integer frequencyDays, Integer completedDaysAgo) {
//		return ChecklistItem.builder()
//				.property(property)
//				.description(description)
//				.frequencyDays(frequencyDays)
//				.lastCompleted(LocalDate.now().minusDays(completedDaysAgo).atStartOfDay())
//				.build();
//	}

	@Override
	public void run(String... args) throws Exception {
		if (organizationRepository.count() > 0) {
			return;
		}
		
		
		
		// Organizations
		Organization organization1 = Organization.builder().name("The Everything Company")
				.description("We specialize in literally everything.").build();
		organization1 = organizationRepository.save(organization1);
		
		
		
		// People
		User manager1 = User.builder()
				.organization(organization1)
				.firstName("Sophie")
				.lastName("Wang")
				.username("sophie")
				.email("sophie@test.com")
				.password(passwordEncoder.encode("password"))
				.phoneNumber("6471231234")
				.role(Role.MANAGER)
				.build();
		User cleaner1 = User.builder()
				.organization(organization1)
				.firstName("Robert")
				.lastName("Fleming")
				.username("robert")
				.email("robert@test.com")
				.password(passwordEncoder.encode("password"))
				.phoneNumber("9056083833")
				.role(Role.CLEANER)
				.build();
		User cleaner2 = User.builder()
				.organization(organization1)
				.firstName("Katie")
				.lastName("McEwan")
				.username("katie")
				.email("katie@test.com")
				.password(passwordEncoder.encode("password"))
				.phoneNumber("2260001234")
				.role(Role.CLEANER)
				.build();
		manager1 = userRepository.save(manager1);
		cleaner1 = userRepository.save(cleaner1);
		cleaner2 = userRepository.save(cleaner2);
		
		
		
		// Sophie's Properties
		Property property1 = Property.builder()
				.manager(manager1)
			    .name("Wayward Pines House")
			    .street("1234 Wayward Pines")
			    .unit("123")
			    .city("Mississauga")
			    .province("Ontario")
			    .postalCode("L6D 7N4")
			    .country("Canada")
			    .accessInstructions("Door locked. Use key under mat.")
			    .build();
		Property property2 = Property.builder()
				.manager(manager1)
			    .name("Union Condo")
			    .street("101 Union St.")
			    .unit("292")
			    .city("Oakville")
			    .province("Ontario")
			    .postalCode("L4V 1V1")
			    .country("Canada")
			    .accessInstructions(
			        "Jump over the fence and take a right."
			    )
			    .build();
		Property property3 = Property.builder()
				.manager(manager1)
			    .name("Maple Ridge House")
			    .street("47 Maple Ridge Drive")
			    .city("Burlington")
			    .province("Ontario")
			    .postalCode("L7M 2P8")
			    .country("Canada")
			    .accessInstructions(
			        "Use the side entrance. Lockbox is attached to the railing."
			    )
			    .build();
		Property property4 = Property.builder()
				.manager(manager1)
			    .name("Harbourview Condo")
			    .street("2500 Lakeshore Road West")
			    .unit("604")
			    .city("Oakville")
			    .province("Ontario")
			    .postalCode("L6L 1H8")
			    .country("Canada")
			    .accessInstructions(
			        "Check in with the concierge and ask for the unit key."
			    )
			    .build();
		Property property5 = Property.builder()
				.manager(manager1)
			    .name("Downtown Loft")
			    .street("80 King Street West")
			    .unit("1205")
			    .city("Toronto")
			    .province("Ontario")
			    .postalCode("M5H 1J9")
			    .country("Canada")
			    .accessInstructions(
			        "Enter through the west lobby. The access code is in the booking notes."
			    )
			    .build();
		property1 = propertyRepository.save(property1);
		property2 = propertyRepository.save(property2);
		property3 = propertyRepository.save(property3);
		property4 = propertyRepository.save(property4);
		property5 = propertyRepository.save(property5);
		
		
		
		// Checklist Items
		// Property 1 checklist items
		ChecklistItem kitchen = generateChecklistItem(property1, "Clean kitchen counters and sink", 1, 1);
		ChecklistItem bathroom = generateChecklistItem(property1, "Clean and disinfect bathroom", 1, 3);
		ChecklistItem bedding = generateChecklistItem(property1, "Change bed linens", 1, 7);
		ChecklistItem supplies = generateChecklistItem(property1, "Check if supplies need to be replaced", 15, 4);
		ChecklistItem furnaceFilter = generateChecklistItem(property1, "Replace furnace filter", 45, 60);
		ChecklistItem ceilingCorners = generateChecklistItem(property1, "Dust ceiling corners", 15, 5);
		ChecklistItem baseboards = generateChecklistItem(property1, "Clean baseboards", 30, 35);
		checklistItemRepository
				.saveAll(List.of(kitchen, bathroom, bedding, supplies, furnaceFilter, ceilingCorners, baseboards));

		// Property 2 checklist items
		ChecklistItem kitchen2 = generateChecklistItem(property2, "Clean kitchen counters and sink", 1, 2);
		ChecklistItem bathroom2 = generateChecklistItem(property2, "Clean and disinfect bathroom", 1, 2);
		ChecklistItem bedding2 = generateChecklistItem(property2, "Change bed linens", 1, 6);
		ChecklistItem supplies2 = generateChecklistItem(property2, "Check if supplies need to be replaced", 20, 4);
		ChecklistItem hottub = generateChecklistItem(property2, "Check water level in hot tub", 7, 7);
		ChecklistItem windows2 = generateChecklistItem(property2, "Clean windows and balcony doors", 15, 3);
		ChecklistItem vents2 = generateChecklistItem(property2, "Dust vents and air returns", 45, 5);
		checklistItemRepository.saveAll(List.of(kitchen2, bathroom2, bedding2, supplies2, hottub, windows2, vents2));

		// Property 3 checklist items
		ChecklistItem floors3 = generateChecklistItem(property3, "Vacuum carpets and mop floors", 1, 1);
		ChecklistItem bathroom3 = generateChecklistItem(property3, "Clean and disinfect bathrooms", 1, 4);
		ChecklistItem bedding3 = generateChecklistItem(property3, "Change bed linens", 1, 5);
		ChecklistItem ceilingFans3 = generateChecklistItem(property3, "Dust ceiling fans", 15, 6);
		ChecklistItem furnaceFilter3 = generateChecklistItem(property3, "Replace furnace filter", 45, 7);
		ChecklistItem windows3 = generateChecklistItem(property3, "Clean interior windows", 20, 3);
		ChecklistItem sinks3 = generateChecklistItem(property3, "Check under sinks for leaks", 15, 2);
		checklistItemRepository
				.saveAll(List.of(floors3, bathroom3, bedding3, ceilingFans3, furnaceFilter3, windows3, sinks3));

		// Property 4 checklist items
		ChecklistItem kitchen4 = generateChecklistItem(property4, "Clean kitchen surfaces", 1, 1);
		ChecklistItem bathroom4 = generateChecklistItem(property4, "Clean bathroom fixtures", 1, 3);
		ChecklistItem towels4 = generateChecklistItem(property4, "Replace towels and linens", 1, 4);
		ChecklistItem appliances4 = generateChecklistItem(property4, "Wipe appliance exteriors", 7, 5);
		ChecklistItem windows4 = generateChecklistItem(property4, "Clean windows and mirrors", 14, 6);
		ChecklistItem balcony4 = generateChecklistItem(property4, "Sweep and wash balcony", 30, 7);
		ChecklistItem smokeDetectors4 = generateChecklistItem(property4, "Test smoke detectors", 60, 2);

		checklistItemRepository
				.saveAll(List.of(kitchen4, bathroom4, towels4, appliances4, windows4, balcony4, smokeDetectors4));

		// Property 5 checklist items
		ChecklistItem floors5 = generateChecklistItem(property5, "Sweep and mop floors", 1, 2);
		ChecklistItem shower5 = generateChecklistItem(property5, "Clean glass shower doors", 1, 1);
		ChecklistItem surfaces5 = generateChecklistItem(property5, "Wipe counters and surfaces", 1, 4);
		ChecklistItem shelves5 = generateChecklistItem(property5, "Dust shelves and exposed pipes", 7, 6);
		ChecklistItem rangeHood5 = generateChecklistItem(property5, "Clean range hood filter", 30, 7);
		ChecklistItem sofa5 = generateChecklistItem(property5, "Vacuum sofa and cushions", 14, 5);
		ChecklistItem alarms5 = generateChecklistItem(property5, "Test smoke and carbon monoxide alarms", 60, 3);
		checklistItemRepository.saveAll(List.of(floors5, shower5, surfaces5, shelves5, rangeHood5, sofa5, alarms5));	
		

		
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
