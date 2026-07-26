package ca.sheridancollege.restfulhousekeeping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.restfulhousekeeping.beans.Property;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long> {

    List<Property> findByManagerId(Long id, LocalDateTime now);
}
