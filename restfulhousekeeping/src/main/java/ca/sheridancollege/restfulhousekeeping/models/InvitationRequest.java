package ca.sheridancollege.restfulhousekeeping.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InvitationRequest(
    @NotNull @Positive Long cleanerId,
    @NotNull @Positive Long organizationId
) {}
