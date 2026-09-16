package ca.sheridancollege.restfulhousekeeping.beans;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ChecklistItem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="propertyId", nullable=false)
	@JsonIgnore
	private Property property;
	
	private String description;
	private Integer frequencyDays;
    private LocalDateTime lastCompleted;
}
