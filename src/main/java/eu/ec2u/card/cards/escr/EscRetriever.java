package eu.ec2u.card.cards.escr;

import lombok.NoArgsConstructor;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

@NoArgsConstructor
public class EscRetriever {

	private final String basePath = "https://api-sandbox.europeanstudentcard.eu/v1";

	/**
	 * Lists students or retrieves single student's details.
	 */
	public String retrieveStudents(final String... pathBlocks) throws IOException, InterruptedException {

		return httpGetter(basePath + "/students/" + String.join("", pathBlocks));

	}

	/**
	 * Lists cards or retrieves single card's details.
	 */
	public String retrieveCards(final String... pathBlocks) throws IOException, InterruptedException {

		String completePath = basePath;

		if (pathBlocks.length == 0) {
			completePath += "/cards/";
		} else {
			completePath = basePath + "/students/" + pathBlocks[0] + "/cards/" + pathBlocks[1];
		}

		return httpGetter(completePath);

	}

	public String httpGetter(final String uri) throws IOException, InterruptedException {

		final HttpClient client = HttpClient.newHttpClient();

		final HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.header("Key", System.getenv("API_SANDBOX_KEY"))
				.header("Accept", "application/json")
				.uri(URI.create(uri))
				.build();

		final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return response.body();

	}

	public JsonObject stringToJson(final String jsonString) {

		final JsonReader jsonReader = Json.createReader(new StringReader(jsonString));

		return jsonReader.readObject();

	}

	public SimpleCard parseCardJson(final JsonObject cardJson) {

		final JsonObject studentObject = cardJson.getJsonObject("student");

		return new SimpleCard(

				cardJson.getString("europeanStudentCardNumber"),
				studentObject.getString("europeanStudentIdentifier"),
				studentObject.getInt("picInstitutionCode"),
				studentObject.getString("emailAddress"),
				LocalDateTime.parse(studentObject.getString("expiryDate").substring(0, 19)),
				studentObject.getString("name"),
				studentObject.getInt("academicLevel")

		);

	}

}
