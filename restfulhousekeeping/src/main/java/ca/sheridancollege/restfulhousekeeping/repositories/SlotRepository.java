package ca.sheridancollege.restfulhousekeeping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.restfulhousekeeping.beans.AvailabilitySlot;

import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<AvailabilitySlot,Long> {
    List<AvailabilitySlot> findByCleanerId(Long cleanerId);
    Optional<AvailabilitySlot> findByIdAndCleanerId(Long id, Long cleanerId);
}
