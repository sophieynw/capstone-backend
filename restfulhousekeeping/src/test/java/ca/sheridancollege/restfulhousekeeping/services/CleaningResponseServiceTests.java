package ca.sheridancollege.restfulhousekeeping.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ca.sheridancollege.restfulhousekeeping.beans.Cleaning;
import ca.sheridancollege.restfulhousekeeping.beans.Organization;
import ca.sheridancollege.restfulhousekeeping.beans.Property;
import ca.sheridancollege.restfulhousekeeping.beans.Role;
import ca.sheridancollege.restfulhousekeeping.beans.User;
import ca.sheridancollege.restfulhousekeeping.models.CleaningResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.OrganizationRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.PropertyRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CleaningResponseServiceTests {

    @Autowired MockMvc mvc;
    @Autowired CleaningResponseService service;
    @Autowired CleaningRepository cleaningRepository;
    @Autowired OrganizationRepository organizationRepository;
    @Autowired PropertyRepository propertyRepository;
    @Autowired UserRepository userRepository;

    Organization managerOrganization;
    User manager;
    User teamCleaner;
    Cleaning cleaning;

    @BeforeEach
    void setup() {
        managerOrganization = organizationRepository.save(
                Organization.builder().name("Assignment Test Team").build()
        );
        manager = userRepository.save(User.builder()
                .username("assignment-manager")
                .role(Role.MANAGER)
                .organization(managerOrganization)
                .build());
        teamCleaner = userRepository.save(User.builder()
                .username("assignment-cleaner")
                .role(Role.CLEANER)
                .organization(managerOrganization)
                .build());
        Property property = propertyRepository.save(Property.builder()
                .name("Assignment Test Property")
                .manager(manager)
                .build());
        cleaning = cleaningRepository.save(Cleaning.builder()
                .manager(manager)
                .property(property)
                .isComplete(false)
                .build());
    }

    @Test
    void assignsCleanerFromManagersTeam() {
        CleaningResponse response = service.updateCleaner(
                cleaning.getId(), teamCleaner.getId(), manager);

        assertThat(response.getCleanerId()).isEqualTo(teamCleaner.getId());
        assertThat(cleaningRepository.findById(cleaning.getId()).orElseThrow()
                .getCleaner().getId()).isEqualTo(teamCleaner.getId());
    }

    @Test
    void allowsManagerToUnassignCleaner() {
        cleaning.setCleaner(teamCleaner);
        cleaningRepository.save(cleaning);

        CleaningResponse response = service.updateCleaner(
                cleaning.getId(), null, manager);

        assertThat(response.getCleanerId()).isNull();
        assertThat(cleaningRepository.findById(cleaning.getId()).orElseThrow()
                .getCleaner()).isNull();
    }

    @Test
    void rejectsCleaningManagedBySomeoneElse() {
        User otherManager = userRepository.save(User.builder()
                .username("other-assignment-manager")
                .role(Role.MANAGER)
                .organization(managerOrganization)
                .build());
        cleaning.setManager(otherManager);
        cleaningRepository.save(cleaning);

        assertForbidden(() -> service.updateCleaner(
                cleaning.getId(), teamCleaner.getId(), manager));

        assertThat(cleaning.getCleaner()).isNull();
    }

    @Test
    void rejectsCleanerFromAnotherTeam() {
        Organization otherOrganization = organizationRepository.save(
                Organization.builder().name("Other Assignment Team").build()
        );
        User outsider = userRepository.save(User.builder()
                .username("outsider-assignment-cleaner")
                .role(Role.CLEANER)
                .organization(otherOrganization)
                .build());

        assertForbidden(() -> service.updateCleaner(
                cleaning.getId(), outsider.getId(), manager));

        assertThat(cleaning.getCleaner()).isNull();
    }

    @Test
    void rejectsNonManagerEvenIfTheirIdMatches() {
        User cleanerPrincipal = User.builder()
                .id(manager.getId())
                .role(Role.CLEANER)
                .build();

        assertForbidden(() -> service.updateCleaner(
                cleaning.getId(), teamCleaner.getId(), cleanerPrincipal));

        assertThat(cleaning.getCleaner()).isNull();
    }

    @Test
    void rejectsReassignmentAfterCleaningIsComplete() {
        cleaning.setIsComplete(true);
        cleaningRepository.save(cleaning);

        assertThatThrownBy(() -> service.updateCleaner(
                cleaning.getId(), teamCleaner.getId(), manager))
                .isInstanceOfSatisfying(ResponseStatusException.class, exception ->
                        assertThat(exception.getStatusCode())
                                .isEqualTo(HttpStatus.CONFLICT));

        assertThat(cleaning.getCleaner()).isNull();
    }

    @Test
    void cleanerRoleCannotAccessAssignmentEndpoint() throws Exception {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        teamCleaner,
                        null,
                        teamCleaner.getAuthorities()
                );

        mvc.perform(patch("/cleanings/{cleaningId}/cleaner", cleaning.getId())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cleanerId\":null}"))
                .andExpect(status().isForbidden());

        assertThat(cleaning.getCleaner()).isNull();
    }

    @Test
    void managerCanDeleteIncompleteCleaning() throws Exception {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        manager,
                        null,
                        manager.getAuthorities()
                );

        mvc.perform(delete("/cleanings/{cleaningId}", cleaning.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isNoContent());

        assertThat(cleaningRepository.existsById(cleaning.getId())).isFalse();
    }

    @Test
    void managerCannotDeleteCompletedCleaning() throws Exception {
        cleaning.setIsComplete(true);
        cleaningRepository.save(cleaning);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        manager,
                        null,
                        manager.getAuthorities()
                );

        mvc.perform(delete("/cleanings/{cleaningId}", cleaning.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isConflict());

        assertThat(cleaningRepository.existsById(cleaning.getId())).isTrue();
    }

    private void assertForbidden(Runnable action) {
        assertThatThrownBy(action::run)
                .isInstanceOfSatisfying(ResponseStatusException.class, exception ->
                        assertThat(exception.getStatusCode())
                                .isEqualTo(HttpStatus.FORBIDDEN));
    }
}
