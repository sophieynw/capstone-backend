package ca.sheridancollege.restfulhousekeeping.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ca.sheridancollege.restfulhousekeeping.beans.Cleaning;
import ca.sheridancollege.restfulhousekeeping.beans.Role;
import ca.sheridancollege.restfulhousekeeping.beans.User;
import ca.sheridancollege.restfulhousekeeping.models.CleaningResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CleaningResponseService {

    private final CleaningRepository cleaningRepository;
    private final UserRepository userRepository;
    private final CleaningChecklistItemResponseService cciResponseService;

    public List<CleaningResponse> getMyUpcomingCleanings(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime now = LocalDateTime.now();
        List<Cleaning> cleanings;

        if (user.getRole() == Role.MANAGER) {
            cleanings = cleaningRepository
                    .findByManagerIdAndDateTimeStartGreaterThanEqual(
                            user.getId(),
                            now
                    );
        } else if (user.getRole() == Role.CLEANER) {
            cleanings = cleaningRepository
                    .findByCleanerIdAndDateTimeStartGreaterThanEqual(
                            user.getId(),
                            now
                    );

        }  else {
            throw new RuntimeException("Invalid user role");    
        }
        
        return cleanings.stream()
        		.map(this::toCleaningResponse)
        		.toList();
        
    }

    public CleaningResponse getFirstCleaningByProperty(Long propertyId) {
        Cleaning cleaning;

        cleaning = cleaningRepository.findFirstByPropertyIdOrderByDateTimeStartAsc(propertyId);
        return toCleaningResponse(cleaning);
    }
    
    public CleaningResponse toCleaningResponse(Cleaning cleaning) {
    	return CleaningResponse.builder()
    			.id(cleaning.getId())
    			.managerId(cleaning.getManager().getId())
    			.cleanerId(
					cleaning.getCleaner() == null
						? null
						: cleaning.getCleaner().getId()
    			)
    			.propertyId(cleaning.getProperty().getId())
    			.dateTimeStart(cleaning.getDateTimeStart())
                .dateTimeEnd(cleaning.getDateTimeEnd())
                .dateTimeStarted(cleaning.getDateTimeStarted())
                .dateTimeCompleted(cleaning.getDateTimeCompleted())
                .notes(cleaning.getNotes())
                .cleaningChecklistItems(
                        cciResponseService
                                .getItemsForCleaning(cleaning.getId())
                )
                .isComplete(cleaning.getIsComplete())
                .build();
    }
    
    
    
}
