package eu.ec2u.card.cards.escr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class EscLinkerTest {

	private EscLinker escLinker;
	private final String ESI = "urn:schac:personalUniqueCode:int:esi:unipv.it:999001";
	private final String ESC = "fc3a1f80-93fb-103a-9b31-000999893752";

	@BeforeEach
	protected void setUp() throws Exception {

		escLinker = new EscLinker(new EscrHttpManager());

	}

	@Test
	public void noTestHere() {

		final String retrievedCard = "";

		/* List students */
		System.out.println(escLinker.listStudents());
		System.out.println("---");

		/* Retrieve student's details */
		try {
			System.out.println(escLinker.retrieveStudentDetails(ESI));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println("---");

		/* List cards */
		System.out.println(escLinker.listCards());
		System.out.println("---");

		/* Retrieve card's details */
		try {
			System.out.println(escLinker.retrieveCardDetails(ESI, "fc3a1f80-93fb-103a-9b31-000999893752"));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println("---");

	}

	@Test
	public void listStudents() {

		assertThat(escLinker.listStudents().toString())
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

		assertThat(escLinker.listCards().toString())
				.contains("europeanStudentCardNumber")
				.contains("cardType")
				.contains("cardUid")
				.contains("student");

	}

	@Test
	public void retrieveStudentsDetails() {

		try {

			assertThat(escLinker.retrieveStudentDetails(ESI).toString())
					.contains("europeanStudentIdentifier")
					.contains("picInstitutionCode")
					.contains("emailAddress")
					.contains("expiryDate")
					.contains("name")
					.contains("academicLevel")
					.contains("cards");

		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void retrieveCardsDetails() {

		try {

			assertThat(escLinker.retrieveCardDetails(ESI, ESC).toString())
					.contains("europeanStudentCardNumber")
					.contains("student")
					.contains("europeanStudentIdentifier")
					.contains("picInstitutionCode")
					.contains("emailAddress")
					.contains("expiryDate")
					.contains("name")
					.contains("academicLevel");

		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

}
