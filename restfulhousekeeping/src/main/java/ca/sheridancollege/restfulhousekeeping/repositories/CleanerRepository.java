package ca.sheridancollege.restfulhousekeeping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.restfulhousekeeping.beans.Cleaner;

@Repository
public interface CleanerRepository extends JpaRepository<Cleaner,Long> {
	
}
