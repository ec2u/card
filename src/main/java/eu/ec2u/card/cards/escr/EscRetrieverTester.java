package eu.ec2u.card.cards.escr;

import javax.json.JsonObject;

public class EscRetrieverTester {

	public static void main(final String[] args) {

		final EscRetriever escRetriever = new EscRetriever();
		final String retrievedCard = "";

		final JsonObject jsonObject = escRetriever.stringToJson(escRetriever.retrieveCard("urn:schac" +
						":personalUniqueCode" +
						":int" +
						":esi:unipv.it:999001",
				"fc3a1f80-93fb-103a-9b31-000999893752"));

		System.out.println(jsonObject);

		System.out.println("\n");

		System.out.println(escRetriever.parseCardJson(jsonObject).toString());

	}

}
