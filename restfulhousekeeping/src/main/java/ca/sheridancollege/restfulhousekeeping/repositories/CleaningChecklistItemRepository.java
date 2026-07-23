package ca.sheridancollege.restfulhousekeeping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.restfulhousekeeping.beans.CleaningChecklistItem;

public interface CleaningChecklistItemRepository extends JpaRepository<CleaningChecklistItem, Long> {

}
