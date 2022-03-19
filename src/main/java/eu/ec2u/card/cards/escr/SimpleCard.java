package eu.ec2u.card.cards.escr;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SimpleCard {

	private String europeanStudentCardNumber;
	private String europeanStudentIdentifier;
	private int picInstitutionCode;
	private String emailAddress;
	private LocalDateTime expiryDate;
	private String name;
	private int academicLevel;

}
