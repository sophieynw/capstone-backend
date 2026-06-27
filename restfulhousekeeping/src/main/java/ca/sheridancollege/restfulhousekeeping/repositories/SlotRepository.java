package ca.sheridancollege.restfulhousekeeping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.restfulhousekeeping.beans.AvailabilitySlot;

public interface SlotRepository extends JpaRepository<AvailabilitySlot,Long> {

}
