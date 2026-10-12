package ca.sheridancollege.restfulhousekeeping.controllers;

import ca.sheridancollege.restfulhousekeeping.services.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    public record SendEmailRequest(
            String to,
            String subject,
            String text
    ) {}

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<Void> sendEmail(@RequestBody SendEmailRequest request) {
        emailService.sendEmail(request.to, request.subject, request.text);
        return ResponseEntity.ok().build();
    }
}
