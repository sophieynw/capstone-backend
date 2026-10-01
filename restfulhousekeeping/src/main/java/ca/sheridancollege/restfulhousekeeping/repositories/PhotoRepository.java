package ca.sheridancollege.restfulhousekeeping.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ca.sheridancollege.restfulhousekeeping.beans.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByCleaningId(Long cleaningId);
}