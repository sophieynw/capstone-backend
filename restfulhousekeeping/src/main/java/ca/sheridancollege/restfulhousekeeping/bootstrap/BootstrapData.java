package ca.sheridancollege.restfulhousekeeping.bootstrap;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ca.sheridancollege.restfulhousekeeping.beans.Person;
import ca.sheridancollege.restfulhousekeeping.repositories.PersonRepository;

@Component
public class BootstrapData implements CommandLineRunner {
	@Autowired
	private PersonRepository personRepository;

	@Override
	public void run(String... args) throws Exception {
		if (personRepository.count() > 0) {
			return;
		}
		
		Person testUser = Person.builder()
				.name("Luke Skywalker")
				.email("fathersday66@gmail.com")
				.password("Password123")
				.role("Owner")
				.available(true)
				.createdAt(LocalDateTime.now())
				.build();
		testUser = personRepository.save(testUser);
	}
}
