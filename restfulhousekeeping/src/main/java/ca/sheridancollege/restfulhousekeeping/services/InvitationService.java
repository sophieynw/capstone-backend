package ca.sheridancollege.restfulhousekeeping.services;

import ca.sheridancollege.restfulhousekeeping.beans.*;
import ca.sheridancollege.restfulhousekeeping.models.InvitationResponse;
import ca.sheridancollege.restfulhousekeeping.repositories.InvitationRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.OrganizationRepository;
import ca.sheridancollege.restfulhousekeeping.repositories.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final EmailService emailService;

    public InvitationService(
            InvitationRepository invitationRepository,
            UserRepository userRepository,
            OrganizationRepository organizationRepository,
            EmailService emailService) {

        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.emailService = emailService;
    }

    @Transactional
    public InvitationResponse createInvitation(
            Long cleanerId,
            Long organizationId,
            User manager) {

        User cleaner = userRepository.findById(cleanerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cleaner not found"));

        Organization organization =
                organizationRepository.findById(organizationId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Organization not found"));

        if (cleaner.getRole() != Role.CLEANER) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only cleaners can be invited");
        }

        if (manager.getOrganization() == null
                || !manager.getOrganization().getId()
                        .equals(organization.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Manager cannot invite cleaners to this organization");
        }

        if (cleaner.getOrganization() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cleaner already belongs to an organization");
        }

        Invitation invitation = new Invitation();
        invitation.setCleaner(cleaner);
        invitation.setOrganization(organization);
        invitation.setInvitedBy(manager);
        invitation.setStatus(InvitationStatus.PENDING);

        LocalDateTime now = LocalDateTime.now();
        invitation.setCreatedAt(now);
        invitation.setExpiresAt(now.plusDays(7));

        Invitation saved = invitationRepository.save(invitation);

        try {
        emailService.sendEmail(
                cleaner.getEmail(),
                "RESTful Housekeeping: Invitation",
                "Hello " + cleaner.getFirstName() + ",\n\n"
                        + "You have been invited to work with "
                        + manager.getFirstName() + " " + manager.getLastName()
                        + " at " + organization.getName() + ".\n\n"
                        + "Please open RESTful Housekeeping to review your invitation.\n\n"
                        + "Regards,\nRESTful Housekeeping");
        } catch (MailException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "Could not send invitation email", e);
    }

    return toResponse(saved);
    }

    @Transactional
    public InvitationResponse getInvitationById(
            Long invitationId,
            User user) {

        Invitation invitation = findInvitation(invitationId);
        authorizeInvitationAccess(invitation, user);

        return toResponse(invitation);
    }

    @Transactional
    public List<InvitationResponse> getCleanerInvitations(
            Long cleanerId,
            InvitationStatus status,
            User user) {

        if (user.getRole() != Role.CLEANER
                || !user.getId().equals(cleanerId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only view your own invitations");
        }

        List<Invitation> invitations =
                status == null
                        ? invitationRepository.findByCleanerId(cleanerId)
                        : invitationRepository.findByCleanerIdAndStatus(
                                cleanerId, status);

        return invitations.stream()
                .map(this::toResponse)
                .toList();
    }

    // Retrieve invitations belonging to an organization.
    @Transactional
    public List<InvitationResponse> getOrganizationInvitations(
            Long organizationId,
            InvitationStatus status,
            User manager) {

        if (manager.getRole() != Role.MANAGER
                || manager.getOrganization() == null
                || !manager.getOrganization().getId()
                        .equals(organizationId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot view invitations for this organization");
        }

        List<Invitation> invitations =
                status == null
                        ? invitationRepository.findByOrganizationId(
                                organizationId)
                        : invitationRepository.findByOrganizationIdAndStatus(
                                organizationId, status);

        return invitations.stream()
                .map(this::toResponse)
                .toList();
    }

    // Accept an invitation.
    @Transactional
    public InvitationResponse acceptInvitation(
            Long invitationId,
            User cleaner) {

        Invitation invitation = findInvitation(invitationId);

        validateInvitedCleaner(invitation, cleaner);
        validatePendingInvitation(invitation);

        User managedCleaner = userRepository.findById(cleaner.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cleaner not found"));

        Organization organization = invitation.getOrganization();

        if (managedCleaner.getOrganization() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cleaner already belongs to an organization");
        }

        managedCleaner.setOrganization(organization);
        userRepository.save(managedCleaner);

        invitation.setStatus(InvitationStatus.ACCEPTED);
        Invitation savedInvite = invitationRepository.save(invitation);

        closeOtherPendingInvitations(managedCleaner.getId(), savedInvite.getId());
        return toResponse(savedInvite);
    }

    private void closeOtherPendingInvitations(Long cleanerId, Long acceptedId) {
    List<Invitation> others = invitationRepository
            .findByCleanerIdAndStatus(cleanerId, InvitationStatus.PENDING)
            .stream()
            .filter(i -> !i.getId().equals(acceptedId))
            .toList();

    others.forEach(i -> i.setStatus(InvitationStatus.EXPIRED));
    invitationRepository.saveAll(others);
}

    // Decline an invitation.
    @Transactional
    public InvitationResponse declineInvitation(
            Long invitationId,
            User cleaner) {

        Invitation invitation = findInvitation(invitationId);

        validateInvitedCleaner(invitation, cleaner);
        validatePendingInvitation(invitation);

        invitation.setStatus(InvitationStatus.DECLINED);

        return toResponse(invitationRepository.save(invitation));
    }

    private Invitation findInvitation(Long invitationId) {
        return invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Invitation not found"));
    }

    private void authorizeInvitationAccess(
            Invitation invitation,
            User user) {

        boolean isInvitedCleaner =
                user.getRole() == Role.CLEANER
                && invitation.getCleaner().getId().equals(user.getId());

        boolean isOrganizationManager =
                user.getRole() == Role.MANAGER
                && user.getOrganization() != null
                && user.getOrganization().getId()
                        .equals(invitation.getOrganization().getId());

        if (!isInvitedCleaner && !isOrganizationManager) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot access this invitation");
        }
    }

    private void validateInvitedCleaner(
            Invitation invitation,
            User cleaner) {

        if (cleaner.getRole() != Role.CLEANER
                || !invitation.getCleaner().getId()
                        .equals(cleaner.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This invitation was not issued to you");
        }
    }

    private void validatePendingInvitation(Invitation invitation) {
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Invitation is no longer pending");
        }

        if (invitation.getExpiresAt() != null
                && invitation.getExpiresAt().isBefore(LocalDateTime.now())) {

            invitation.setStatus(InvitationStatus.EXPIRED);
            invitationRepository.save(invitation);

            throw new ResponseStatusException(
                    HttpStatus.GONE,
                    "Invitation has expired");
        }
    }

    private InvitationResponse toResponse(Invitation invitation) {
        User cleaner = invitation.getCleaner();
        Organization organization = invitation.getOrganization();
        User manager = invitation.getInvitedBy();

        return new InvitationResponse(
                invitation.getId(),
                cleaner.getId(),
                cleaner.getFirstName() + " " + cleaner.getLastName(),
                organization.getId(),
                organization.getName(),
                manager.getId(),
                invitation.getStatus(),
                invitation.getCreatedAt(),
                invitation.getExpiresAt()
        );
    }
}