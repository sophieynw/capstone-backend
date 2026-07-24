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
public class CleaningChecklistItemResponse {
	
	private Long id;
	private String description;
	private Integer frequencyDays;
	private LocalDateTime lastCompleted;
	private Boolean isComplete;

}
