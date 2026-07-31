package ca.sheridancollege.restfulhousekeeping.models;

import java.time.LocalDateTime;
import java.util.List;

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
	private Long managerId;
	private Long cleanerId;
	private Long propertyId;
	private LocalDateTime dateTimeStart;
	private LocalDateTime dateTimeEnd;
	private LocalDateTime dateTimeStarted;
	private LocalDateTime dateTimeCompleted;
	private String notes;
	private List<CleaningChecklistItemResponse> cleaningChecklistItems;
	private Boolean isComplete;

}
