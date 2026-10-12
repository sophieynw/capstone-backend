package ca.sheridancollege.restfulhousekeeping.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ca.sheridancollege.restfulhousekeeping.beans.Property;
import ca.sheridancollege.restfulhousekeeping.beans.PropertyCalendarIntegration;
import ca.sheridancollege.restfulhousekeeping.models.PropertyResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.PropertyCalendarIntegrationRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.PropertyRepository;

@SpringBootTest
@Transactional
class PropertyCalendarIntegrationTests {

	@Autowired
	private PropertyRepository propertyRepository;

	@Autowired
	private PropertyCalendarIntegrationRepository integrationRepository;

	@Autowired
	private PropertyResponseService propertyResponseService;

	@Test
	void reportsConnectionAndDeletesIntegrationWithProperty() {
		Property property = propertyRepository.findById(1L).orElseThrow();
		PropertyCalendarIntegration integration = integrationRepository.saveAndFlush(
			PropertyCalendarIntegration.builder()
				.property(property)
				.calendarUrl("https://www.airbnb.com/test.ics")
				.build()
		);
		property.setCalendarIntegration(integration);

		PropertyResponse response = propertyResponseService
			.getPropertyById(property.getId())
			.orElseThrow();
		assertThat(response.getAirbnbCalendarConnected()).isTrue();

		propertyRepository.delete(property);
		propertyRepository.flush();

		assertThat(integrationRepository.existsById(integration.getId())).isFalse();
	}
}
