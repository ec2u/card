package eu.ec2u.card.escr;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class CardTransfer {

	private String europeanStudentCardNumber;
	private String europeanStudentIdentifier;
	private Long picInstitutionCode;
	private String emailAddress;
	private LocalDateTime expiryDate;
	private Optional<Integer> cardType;
	private Optional<String> cardUid;

}
