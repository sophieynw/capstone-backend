package ca.sheridancollege.restfulhousekeeping.services;

import ca.sheridancollege.restfulhousekeeping.beans.Cleaning;
import ca.sheridancollege.restfulhousekeeping.beans.CleaningChecklistItem;
import ca.sheridancollege.restfulhousekeeping.beans.Role;
import ca.sheridancollege.restfulhousekeeping.beans.User;
import ca.sheridancollege.restfulhousekeeping.models.CleaningChecklistItemResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningChecklistItemRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.CleaningRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CleaningChecklistItemService {

    private final CleaningRepository cleaningRepository;
    private final CleaningChecklistItemRepository itemRepository;
    private final CleaningChecklistItemResponseService responseService;

    @Transactional
    public CleaningChecklistItemResponse addCustomItem(
            Long cleaningId,
            String description,
            User manager) {
        Cleaning cleaning = getManagedCleaning(cleaningId, manager);

        CleaningChecklistItem item = CleaningChecklistItem.builder()
                .cleaning(cleaning)
                .customDescription(description.trim())
                .isComplete(false)
                .build();

        return responseService.toResponse(itemRepository.save(item));
    }

    @Transactional
    public void deleteItem(Long cleaningId, Long itemId, User manager) {
        getManagedCleaning(cleaningId, manager);

        CleaningChecklistItem item = itemRepository
                .findByIdAndCleaning_Id(itemId, cleaningId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cleaning checklist item not found"
                ));

        itemRepository.delete(item);
    }

    private Cleaning getManagedCleaning(Long cleaningId, User manager) {
        if (manager == null || manager.getRole() != Role.MANAGER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only managers can edit cleaning checklist items"
            );
        }

        Cleaning cleaning = cleaningRepository.findById(cleaningId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cleaning not found"
                ));

        if (!cleaning.getManager().getId().equals(manager.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This cleaning is not managed by the current manager"
            );
        }

        if (Boolean.TRUE.equals(cleaning.getIsComplete())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Completed cleaning checklist items cannot be changed"
            );
        }

        return cleaning;
    }
}
