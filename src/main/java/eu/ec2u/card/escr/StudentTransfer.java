package eu.ec2u.card.escr;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class StudentTransfer {

	private String europeanStudentIdentifier;
	private Long picInstitutionCode;
	private String emailAddress;
	private LocalDateTime expiryDate;
	private Set<String> europeanStudentCardNumbers;
	Optional<String> name;
	Optional<String> phoneNumber;
	Optional<Integer> academicLevel;

}
