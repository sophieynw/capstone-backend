package ca.sheridancollege.restfulhousekeeping.models;

import ca.sheridancollege.restfulhousekeeping.beans.InvitationStatus;
import java.time.LocalDateTime;

public record InvitationResponse(
    Long id,
    Long cleanerId,
    String cleanerName,
    Long organizationId,
    String organizationName,
    Long invitedByUserId,
    InvitationStatus status,
    LocalDateTime createdAt,
    LocalDateTime expiresAt
) {}