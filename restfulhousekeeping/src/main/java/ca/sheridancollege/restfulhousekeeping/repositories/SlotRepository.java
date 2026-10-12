package ca.sheridancollege.restfulhousekeeping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.restfulhousekeeping.beans.AvailabilitySlot;

import java.util.List;

public interface SlotRepository extends JpaRepository<AvailabilitySlot,Long> {
    List<AvailabilitySlot> findByCleanerId(Long cleanerId);
}
