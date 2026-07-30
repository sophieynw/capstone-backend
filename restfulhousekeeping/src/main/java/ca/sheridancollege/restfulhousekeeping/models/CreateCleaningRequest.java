package ca.sheridancollege.restfulhousekeeping.models;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CreateCleaningRequest {
    private Long managerId;
    private Long cleanerId;
    private Long propertyId;
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;
    private String notes;
}