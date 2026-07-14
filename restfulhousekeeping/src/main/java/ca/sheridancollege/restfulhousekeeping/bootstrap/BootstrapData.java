package ca.sheridancollege.restfulhousekeeping.bootstrap;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ca.sheridancollege.restfulhousekeeping.beans.Availability;
import ca.sheridancollege.restfulhousekeeping.beans.AvailabilitySlot;
import ca.sheridancollege.restfulhousekeeping.beans.Cleaning;
import ca.sheridancollege.restfulhousekeeping.beans.DayOfWeek;
import ca.sheridancollege.restfulhousekeeping.beans.Organization;
import ca.sheridancollege.restfulhousekeeping.beans.Property;
import ca.sheridancollege.restfulhousekeeping.beans.Role;
import ca.sheridancollege.restfulhousekeeping.beans.User;
import ca.sheridancollege.restfulhousekeeping.repositories.AvailabilityRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.OrganizationRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.PropertyRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.SlotRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.UserRepository;

@Component
public class BootstrapData implements CommandLineRunner {
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PropertyRepository propertyRepository;
	
	@Autowired
	private CleaningRepository cleaningRepository;
	
	@Autowired
	private AvailabilityRepository availabilityRepository;
	
	@Autowired
	private SlotRepository slotRepository;
	
	@Override
	public void run(String... args) throws Exception {
		if (organizationRepository.count() > 0) {
			return;
		}
		
		Organization organization1 = Organization.builder()
				.name("The Everything Company")
				.description("We specialize in literally everything.")
				.build();
		User manager1 = User.builder()
				.firstName("Luke")
				.lastName("Skywalker")
				.userName("JediMaster")
				.email("fathersday66@gmail.com")
				.passwordHash("NotAHashYet")
				.phoneNumber("416-888-6666")
				.role(Role.MANAGER)
				.build();
		User manager2 = User.builder()
				.firstName("Kaladin")
				.lastName("Stormblessed")
				.userName("H1ghst0rm$")
				.email("spears_and_storms#@gmail.com")
				.passwordHash("NotAHashYet")
				.phoneNumber("905-608-9000")
				.role(Role.MANAGER)
				.build();
		User cleaner1 = User.builder()
				.firstName("John")
				.lastName("Smith")
				.userName("GenericUsername")
				.email("bubblyfresh123@gmail.com")
				.passwordHash("Password123")
				.role(Role.CLEANER)
				.build();
		User cleaner2 = User.builder()
				.firstName("Jane")
				.lastName("Smith")
				.userName("GenericUsername")
				.email("bubblyfresh456@gmail.com")
				.passwordHash("Password456")
				.role(Role.CLEANER)
				.build();
		Property property1 = Property.builder()
				.name("Wayward Pines")
				.street("1234 ")
				.unit("123")
				.city("Mississauga")
				.province("Ontario")
				.postalCode("L6D 7N4")
				.country("Canada")
				.accessInstructions("Just break in, honestly")
				.build();
		Cleaning cleaning1 = Cleaning.builder()
				.dateTimeStart(LocalDateTime.of(2026, 7, 21, 8, 30))
				.dateTimeEnd(LocalDateTime.of(2026, 7, 21, 12, 30))
				.notes("Large pile of asbestos found in toaster oven, bring mask.")
				.build();
		AvailabilitySlot slot1 = AvailabilitySlot.builder()
				.dayOfWeek(DayOfWeek.TUESDAY)
				.startTime(LocalTime.of(9, 0))
				.endTime(LocalTime.of(17, 0))
				.build();
		AvailabilitySlot slot2 = AvailabilitySlot.builder()
				.dayOfWeek(DayOfWeek.WEDNESDAY)
				.startTime(LocalTime.of(9, 0))
				.endTime(LocalTime.of(17, 0))
				.build();
		
		organization1 = organizationRepository.save(organization1);

		// Managers
		manager1.setOrganization(organization1);
		manager1 = userRepository.save(manager1);
		manager2.setOrganization(organization1);
		manager2 = userRepository.save(manager2);
		
		//Cleaners
		cleaner1.setOrganization(organization1);
		cleaner1 = userRepository.save(cleaner1);
		cleaner2.setOrganization(organization1);
		cleaner2 = userRepository.save(cleaner2);
		
		//Properties
		property1.setManager(manager1);
		property1 = propertyRepository.save(property1);
		
		//Cleanings
		cleaning1.setProperty(property1);
		cleaning1.setCleaner(cleaner1);
		cleaning1.setManager(manager1);
		cleaning1 = cleaningRepository.save(cleaning1);
		
		//Availabilities
		Availability availability1 = new Availability();
		availability1.setCleaner(cleaner2);
		availability1 = availabilityRepository.save(availability1);
		slot1.setAvailability(availability1);
		slot2.setAvailability(availability1);
		slot1 = slotRepository.save(slot1);
		slot2 = slotRepository.save(slot2);
		
	}
}
