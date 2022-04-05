package eu.ec2u.card.cards.escr;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.ec2u.card.escr.EscrLinker;
import eu.ec2u.card.escr.EscrHttpManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class EscrLinkerTest {

	private EscrLinker escrLinker;
	private final String ESI = "urn:schac:personalUniqueCode:int:esi:unipv.it:999001";
	private final String ESC = "fc3a1f80-93fb-103a-9b31-000999893752";

	@BeforeEach
	protected void setUp() {

		escrLinker = new EscrLinker(new EscrHttpManager());

	}

	@Test
	public void listStudents() {

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

		assertThat(escrLinker.listCards())
				.contains("europeanStudentCardNumber")
				.contains("cardType")
				.contains("cardUid")
				.contains("student");

	}

	@Test
	public void listStudentsWithTransferObject() {

		try {

			assertThat(escrLinker.listStudentsWithTransferObject().toString())
					.contains("europeanStudentIdentifier")
					.contains("picInstitutionCode")
					.contains("emailAddress")
					.contains("expiryDate")
					.contains("europeanStudentCardNumbers")
					.contains("name")
					.contains("phoneNumber")
					.contains("academicLevel");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void listCardsWithTransferObject() {

		try {

			assertThat(escrLinker.listCardsWithTransferObject().toString())
					.contains("europeanStudentCardNumber")
					.contains("europeanStudentIdentifier")
					.contains("picInstitutionCode")
					.contains("emailAddress")
					.contains("expiryDate")
					.contains("cardType")
					.contains("cardUid");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void retrieveStudent() {

		try {

			assertThat(escrLinker.retrieveStudent(ESI))
					.contains("europeanStudentIdentifier")
					.contains("picInstitutionCode")
					.contains("emailAddress")
					.contains("expiryDate")
					.contains("name")
					.contains("academicLevel")
					.contains("cards");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void retrieveCard() {

		try {

			assertThat(escrLinker.retrieveCard(ESI, ESC))
					.contains("europeanStudentCardNumber")
					.contains("student")
					.contains("europeanStudentIdentifier")
					.contains("picInstitutionCode")
					.contains("emailAddress")
					.contains("expiryDate")
					.contains("name")
					.contains("academicLevel");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void retrieveStudentWithTransferObject() {

		try {

			assertThat(escrLinker.retrieveStudentWithTransferObject(ESI).toString())
					.contains("europeanStudentIdentifier")
					.contains("picInstitutionCode")
					.contains("emailAddress")
					.contains("expiryDate")
					.contains("europeanStudentCardNumbers")
					.contains("name")
					.contains("phoneNumber")
					.contains("academicLevel");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void retrieveCardWithTransferObject() {

		try {

			assertThat(escrLinker.retrieveCardWithTransferObject(ESI, ESC).toString())
					.contains("europeanStudentCardNumber")
					.contains("europeanStudentIdentifier")
					.contains("picInstitutionCode")
					.contains("emailAddress")
					.contains("expiryDate")
					.contains("cardType")
					.contains("cardUid");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
