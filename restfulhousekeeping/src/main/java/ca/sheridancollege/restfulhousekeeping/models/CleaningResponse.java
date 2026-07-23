package ca.sheridancollege.restfulhousekeeping.models;

import java.time.LocalDateTime;
import java.util.List;

import ca.sheridancollege.restfulhousekeeping.beans.ChecklistItem;
import ca.sheridancollege.restfulhousekeeping.beans.Property;
import ca.sheridancollege.restfulhousekeeping.beans.User;
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
        private User cleaner;
        private User manager;
        private Property property;
        private LocalDateTime dateTimeStart;
        private LocalDateTime dateTimeEnd;
        private LocalDateTime dateTimeStarted;
        private LocalDateTime dateTimeCompleted;
        private String notes;
        private List<ChecklistItem> checklistItems;
        private Boolean isComplete;
}
