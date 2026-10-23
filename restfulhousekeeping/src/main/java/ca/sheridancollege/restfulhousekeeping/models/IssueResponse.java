package ca.sheridancollege.restfulhousekeeping.models;

import ca.sheridancollege.restfulhousekeeping.beans.Issue;
import ca.sheridancollege.restfulhousekeeping.beans.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueResponse {

    private Long id;
    private Long cleaningId;
    private Long reportedById;
    private String reportedByName;
    private String issueType;
    private String description;

    public static IssueResponse fromIssue(Issue issue) {
        User reporter = issue.getReportedBy();

        return IssueResponse.builder()
                .id(issue.getId())
                .cleaningId(issue.getCleaning() != null ? issue.getCleaning().getId() : null)
                .reportedById(reporter != null ? reporter.getId() : null)
                .reportedByName(reporter != null
                        ? reporter.getFirstName() + " " + reporter.getLastName()
                        : null)
                .issueType(issue.getIssueType())
                .description(issue.getDescription())
                .build();
    }
}