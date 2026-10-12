package ca.sheridancollege.restfulhousekeeping.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_cleaning_property_source_event",
            columnNames = {"propertyId", "source","externalEventUid"}
        )
    }
)
public class Cleaning {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="managerId", nullable=false)
	private User manager;
	
	@ManyToOne
	@JoinColumn(name="cleanerId")
	private User cleaner;
	
	@ManyToOne
	@JoinColumn(name="propertyId", nullable=false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Property property;
	
	private LocalDateTime dateTimeStart;
	private LocalDateTime dateTimeEnd;
	private LocalDateTime dateTimeStarted;
	private LocalDateTime dateTimeCompleted;
	private String notes;
	
	@OneToMany(
		    mappedBy = "cleaning",
		    cascade = CascadeType.ALL,
		    orphanRemoval = true
		)
	@JsonManagedReference
	@Builder.Default
	private List<CleaningChecklistItem> cleaningChecklistItems = new ArrayList<>();
	private Boolean isComplete;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private CleaningSource source = CleaningSource.MANUAL;

	private String externalEventUid;
	private String externalReservationCode;
	@Column(length = 2000)
	private String reservationUrl;
	private LocalDate importedCheckInDate;
	private LocalDate importedCheckOutDate;
}
