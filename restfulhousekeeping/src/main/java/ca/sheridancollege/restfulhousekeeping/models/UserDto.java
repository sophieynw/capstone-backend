package ca.sheridancollege.restfulhousekeeping.models;

import ca.sheridancollege.restfulhousekeeping.beans.Organization;
import ca.sheridancollege.restfulhousekeeping.beans.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private Organization organization;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private Role role;

}
