package ca.sheridancollege.restfulhousekeeping.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {
    private Long id;
    private Long managerId;
    private String name;
    private String street;
    private String unit;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String accessInstructions;
}
