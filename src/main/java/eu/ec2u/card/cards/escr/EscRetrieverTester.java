package eu.ec2u.card.cards.escr;

import javax.json.JsonObject;
import java.io.IOException;

public class EscRetrieverTester {

	public static void main(final String[] args) {

		final EscRetriever escRetriever = new EscRetriever();
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

}
