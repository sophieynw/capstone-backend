package ca.sheridancollege.restfulhousekeeping.repositories;

import ca.sheridancollege.restfulhousekeeping.beans.Invitation;
import ca.sheridancollege.restfulhousekeeping.beans.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findByCleanerIdAndStatus(
        Long cleanerId,
        InvitationStatus status
    );

    List<Invitation> findByOrganizationIdAndStatus(
        Long organizationId,
        InvitationStatus status
    );

    List<Invitation> findByOrganizationId(Long organizationId);
    List<Invitation> findByCleanerId(Long cleanerId);
}
