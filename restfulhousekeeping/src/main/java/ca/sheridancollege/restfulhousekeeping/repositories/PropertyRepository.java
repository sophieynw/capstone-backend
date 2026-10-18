package ca.sheridancollege.restfulhousekeeping.repositories;

import ca.sheridancollege.restfulhousekeeping.beans.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long> {

    List<Property> findAllByManager_Id(Long managerId);

    Optional<Property> findByIdAndManager_Id(Long propertyId, Long managerId);

}
