package ca.sheridancollege.restfulhousekeeping.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.sheridancollege.restfulhousekeeping.beans.ChecklistItem;
import ca.sheridancollege.restfulhousekeeping.models.CleaningChecklistItemResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningChecklistItemRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CleaningChecklistItemResponseService {

	private final CleaningChecklistItemRepository cleaningChecklistItemRepository;

	public List<CleaningChecklistItemResponse> getItemsForCleaning(
	        Long cleaningId) {

	    return cleaningChecklistItemRepository
            .findAllByCleaning_Id(cleaningId)
            .stream()
            .map(cci -> {

                ChecklistItem checklistItem =
                        cci.getChecklistItem();

                return new CleaningChecklistItemResponse(
                        cci.getId(),

                        checklistItem != null
                                ? checklistItem.getDescription()
                                : cci.getCustomDescription(),

                        checklistItem != null
                                ? checklistItem.getFrequencyDays()
                                : null,

                        checklistItem != null
                                ? checklistItem.getLastCompleted()
                                : null,

                        cci.getIsComplete()
                );
            })
            .toList();
	}

}
