package eu.ec2u.card.cards.escr;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.json.JsonObject;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
public final class EscRetrieverTester {

	private EscRetriever escRetriever;

	@BeforeEach
	protected void setUp() throws Exception {
		escRetriever = new EscRetriever();
	}

	@Test
	public void testExecute() {

		final String retrievedCard = "";

		/* List students */
		try {
			System.out.println(escRetriever.retrieveStudents());
		} catch (final IOException | InterruptedException e) {
			e.printStackTrace();
		}

		/* Retrieve student's details */
		try {
			System.out.println(escRetriever.retrieveStudents(
					"urn:schac:personalUniqueCode:int:esi:unipv.it:999001"));
		} catch (final IOException | InterruptedException e) {
			e.printStackTrace();
		}

		/* List cards */
		try {
			System.out.println(escRetriever.retrieveCards());
		} catch (final IOException | InterruptedException e) {
			e.printStackTrace();
		}

		String cardDetails = "";

		/* Retrieve card's details */
		try {
			cardDetails = escRetriever.retrieveCards(
					"urn:schac:personalUniqueCode:int:esi:unipv.it:999001",
					"fc3a1f80-93fb-103a-9b31-000999893752"
			);
		} catch (final IOException | InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(cardDetails);

		final JsonObject jsonObject = escRetriever.stringToJson(cardDetails);
		System.out.println(jsonObject);

		System.out.println("\n");

		System.out.println(escRetriever.parseCardJson(jsonObject).toString());

	}

	@Test
	public void listStudents() {

		try {
			String responseRetrieveStudents = escRetriever.retrieveStudents();
			assertThat(escRetriever.retrieveStudents())
					.contains("europeanStudentIdentifier")
					.contains("picInstitutionCode")
					.contains("emailAddress")
					.contains("expiryDate")
					.contains("name")
					.contains("phoneNumber")
					.contains("academicLevel")
					.contains("cards")
			;
		} catch (final IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
