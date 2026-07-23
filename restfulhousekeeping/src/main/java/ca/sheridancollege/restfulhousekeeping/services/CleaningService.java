//package ca.sheridancollege.restfulhousekeeping.services;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import ca.sheridancollege.restfulhousekeeping.beans.Cleaning;
//import ca.sheridancollege.restfulhousekeeping.beans.Role;
//import ca.sheridancollege.restfulhousekeeping.beans.User;
//import ca.sheridancollege.restfulhousekeeping.models.CleaningResponse;
//import ca.sheridancollege.restfulhousekeeping.repositories.CleaningRepository;
//import ca.sheridancollege.restfulhousekeeping.repositories.UserRepository;
//import lombok.AllArgsConstructor;
//
//@Service
//@AllArgsConstructor
//public class CleaningService {
//
//    private final CleaningRepository cleaningRepository;
//    private final UserRepository userRepository;
//
//    public List<CleaningResponse> getMyUpcomingCleanings(Long userId) {
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        LocalDateTime now = LocalDateTime.now();
//        List<Cleaning> cleanings;
//
//        if (user.getRole() == Role.MANAGER) {
//            cleanings = cleaningRepository
//                    .findByManagerIdAndDateTimeStartGreaterThanEqual(
//                            user.getId(),
//                            now
//                    );
//
//        } else if (user.getRole() == Role.CLEANER) {
//            cleanings = cleaningRepository
//                    .findByCleanerIdAndDateTimeStartGreaterThanEqual(
//                            user.getId(),
//                            now
//                    );
//
//        } else {
//            throw new RuntimeException("Invalid user role");
//        }
//
//        return cleanings.stream()
//                .map(c -> new CleaningResponse(
//                        c.getId(),
//                        c.getDateTimeStart(),
//                        c.getDateTimeEnd(),
//                        c.getDateTimeStarted(),
//                        c.getDateTimeCompleted(),
//                        c.getCleaner().getFirstName() + " "
//                                + c.getCleaner().getLastName(),
//                        c.getProperty().getName(),
//                        c.getProperty().getStreet(),
//                        c.getNotes(),
//                        c.getIsComplete()
//                ))
//                .toList();
//    }
//}