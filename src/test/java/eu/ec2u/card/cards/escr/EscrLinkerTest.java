package eu.ec2u.card.cards.escr;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.ec2u.card.escr.EscrHttpManager;
import eu.ec2u.card.escr.EscrLinker;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EscrLinkerTest {

	private final String ESI = "urn:schac:personalUniqueCode:int:esi:unipv.it:999001";
	private final String ESC = "fc3a1f80-93fb-103a-9b31-000999893752";
	private EscrLinker escrLinker;

	@BeforeEach
	protected void setUp() {

		escrLinker = new EscrLinker(new EscrHttpManager());

	}

	@Test
	public void listStudents() {

		System.out.println(escrLinker.listStudents());
		System.out.println("/////");
		assertThat(escrLinker.listStudents())
				.contains("europeanStudentIdentifier")
				.contains("picInstitutionCode")
				.contains("emailAddress")
				.contains("expiryDate")
				.contains("name")
				.contains("phoneNumber")
				.contains("academicLevel")
				.contains("cards");

	}

	@Test
	public void listCards() {

		System.out.println(escrLinker.listCards());
		System.out.println("/////");
		assertThat(escrLinker.listCards())
				.contains("europeanStudentCardNumber")
				.contains("cardType")
				.contains("cardUid")
				.contains("student");

	}

	@Test
	public void listStudentsWithTransferObject() {

		System.out.println(escrLinker.listStudentsWithTransferObject().toString());
		System.out.println("/////");
			assertThat(escrLinker.listStudentsWithTransferObject().toString())
					.contains("europeanStudentIdentifier")
					.contains("picInstitutionCode")
					.contains("emailAddress")
					.contains("expiryDate")
					.contains("europeanStudentCardNumbers")
					.contains("name")
					.contains("phoneNumber")
					.contains("academicLevel");

	}

	@Test
	public void listCardsWithTransferObject() {

		System.out.println(escrLinker.listCardsWithTransferObject().toString());
		System.out.println("/////");
			assertThat(escrLinker.listCardsWithTransferObject().toString())
					.contains("europeanStudentCardNumber")
					.contains("europeanStudentIdentifier")
					.contains("picInstitutionCode")
					.contains("emailAddress")
					.contains("expiryDate")
					.contains("cardType")
					.contains("cardUid");

	}

	@Test
	public void retrieveStudentWithTransferObject() {

		try {
			System.out.println(escrLinker.retrieveStudentWithTransferObject(ESI).get().toString());
			System.out.println("/////");
			if (escrLinker.retrieveStudentWithTransferObject(ESI).isPresent()) {
				assertThat(escrLinker.retrieveStudentWithTransferObject(ESI).get().toString())
						.contains("europeanStudentIdentifier")
						.contains("picInstitutionCode")
						.contains("emailAddress")
						.contains("expiryDate")
						.contains("europeanStudentCardNumbers")
						.contains("name")
						.contains("phoneNumber")
						.contains("academicLevel");
			}

		} catch (final JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void retrieveCardWithTransferObject() {

		try {
			System.out.println(escrLinker.retrieveCardWithTransferObject(ESI, ESC).get().toString());
			System.out.println("/////");
			if (escrLinker.retrieveCardWithTransferObject(ESI, ESC).isPresent()) {
				assertThat(escrLinker.retrieveCardWithTransferObject(ESI, ESC).get().toString())
						.contains("europeanStudentCardNumber")
						.contains("europeanStudentIdentifier")
						.contains("picInstitutionCode")
						.contains("emailAddress")
						.contains("expiryDate")
						.contains("cardType")
						.contains("cardUid");
			}

		} catch (final JsonProcessingException e) {
			e.printStackTrace();
		}

	}

}
