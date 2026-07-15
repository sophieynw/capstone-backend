package ca.sheridancollege.restfulhousekeeping.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CleaningResponse {
        private Long id;
        private LocalDateTime dateTimeStart;
        private LocalDateTime dateTimeEnd;
        private LocalDateTime dateTimeStarted;
        private LocalDateTime dateTimeEnded;
        private String cleanerName;
        private String propertyName;
        private String address;
        private String notes;
        private Boolean isComplete;
}
