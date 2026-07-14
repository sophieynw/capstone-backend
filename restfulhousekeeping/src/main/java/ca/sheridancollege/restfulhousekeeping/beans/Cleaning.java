package ca.sheridancollege.restfulhousekeeping.beans;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cleaning {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="managerId", nullable=false)
	private User manager;
	
	@ManyToOne
	@JoinColumn(name="cleanerId", nullable=false)
	private User cleaner;
	
	@OneToOne
	@JoinColumn(name="propertyId", nullable=false)
	private Property property;
	
	private LocalDateTime dateTimeStart;
	private LocalDateTime dateTimeEnd;
	private String notes;
	private Boolean isComplete;
}
