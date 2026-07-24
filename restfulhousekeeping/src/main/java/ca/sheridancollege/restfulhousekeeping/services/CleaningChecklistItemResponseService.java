package ca.sheridancollege.restfulhousekeeping.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.sheridancollege.restfulhousekeeping.models.CleaningChecklistItemResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningChecklistItemRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CleaningChecklistItemResponseService {

	private final CleaningChecklistItemRepository cleaningChecklistItemRepository;

	public List<CleaningChecklistItemResponse> getItemsForCleaning(Long cleaningId) {

		return cleaningChecklistItemRepository.findAllByCleaning_Id(cleaningId)
	            .stream()
	            .map(cci -> new CleaningChecklistItemResponse(
	            		cci.getId(),
	            		cci.getChecklistItem().getDescription(),
	            		cci.getChecklistItem().getFrequencyDays(),
	            		cci.getChecklistItem().getLastCompleted(),
	                    cci.getIsComplete()
	            ))
	            .toList();

	}

}
