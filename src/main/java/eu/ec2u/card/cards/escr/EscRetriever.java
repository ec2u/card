package eu.ec2u.card.cards.escr;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
public class EscRetriever {

	HttpClient client;
	HttpRequest request;
	
	final String basePath = "https://api-sandbox.europeanstudentcard.eu/v1/students";

	public String retrieveCard(final String esi, final String escNumber) {

		String cardString = "";

		try {
			cardString = httpGetter(basePath +
					"/" + esi + "/cards/" + escNumber);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return cardString;

	}

	public String retrieveAllStudents() {

		String studentsString = "";

		try {
			studentsString = httpGetter(basePath);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return studentsString;

	}

	public String httpGetter(final String uri) throws IOException, InterruptedException {

		client = HttpClient.newHttpClient();

		request = HttpRequest.newBuilder()
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
