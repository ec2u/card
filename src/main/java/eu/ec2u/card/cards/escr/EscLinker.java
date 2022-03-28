package eu.ec2u.card.cards.escr;

import lombok.AllArgsConstructor;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Pattern;

@AllArgsConstructor
public class EscLinker {

	private static final String BASE_PATH = "https://api-sandbox.europeanstudentcard.eu/v1";
	private final EscrHttpManager httpManager;

	public void addStudent(final JsonObject studentJson) {

		try {
			httpManager.httpPoster(BASE_PATH + "/students", studentJson);
		} catch (final IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public JsonArray listStudents() {

		JsonArray studentsJsonArray = null;

		try {
			studentsJsonArray = stringToJsonArray(httpManager.httpGetter(BASE_PATH + "/students"));
		} catch (final IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return studentsJsonArray;

	}

	public JsonObject retrieveStudentDetails(final String esi) throws IOException {

		JsonObject studentsDetailsJson = null;

		if (!checkEsi(esi)) {

			throw new IOException("European Student Identifier doesn't match the correct format");

		}

		try {
			studentsDetailsJson = stringToJsonObject(httpManager.httpGetter(BASE_PATH + "/students/" + esi));
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		return studentsDetailsJson;

	}

	public void updateStudentDetails(final String esi, final JsonObject studentJson) throws IOException {

		if (!checkEsi(esi)) {

			throw new IOException("European Student Identifier doesn't match the correct format");

		}

		try {
			httpManager.httpPutter(BASE_PATH + "/students/" + esi, studentJson);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void deleteStudent(final String esi) throws IOException {

		if (!checkEsi(esi)) {

			throw new IOException("European Student Identifier doesn't match the correct format");

		}

		try {
			httpManager.httpDeleter(BASE_PATH + "/students/" + esi);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void addCard(final String esi, final JsonObject cardJson) throws IOException {

		if (!checkEsi(esi)) {

			throw new IOException("European Student Identifier doesn't match the correct format");

		}

		try {
			httpManager.httpPoster(BASE_PATH + "/students/" + esi + "/cards", cardJson);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

	}

	public JsonArray listCards() {

		JsonArray cardsJsonArray = null;

		try {
			cardsJsonArray = stringToJsonArray(httpManager.httpGetter(BASE_PATH + "/cards"));
		} catch (final IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return cardsJsonArray;

	}

	public JsonObject retrieveCardDetails(final String esi, final String esc) throws IOException {

		JsonObject cardsDetailsJson = null;

		if (!checkEsi(esi)) {

			throw new IOException("European Student Identifier doesn't match the correct format");

		}

		if (!checkEsc(esc)) {

			throw new IOException("European Student Card doesn't match the correct format");

		}

		try {
			cardsDetailsJson =
					stringToJsonObject(httpManager.httpGetter(BASE_PATH + "/students/" + esi + "/cards/" + esc));
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		return cardsDetailsJson;

	}

	public void deleteCard(final String esi, final String esc) throws IOException {

		if (!checkEsi(esi)) {

			throw new IOException("esi doesn't match the correct format");

		}

		if (!checkEsc(esc)) {

			throw new IOException("esc or esc doesn't match the correct format");

		}

		try {
			httpManager.httpDeleter(BASE_PATH + "/students/" + esi + "/cards/" + esc);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

	}

	private JsonObject stringToJsonObject(final String jsonString) {

		return Json.createReader(new StringReader(jsonString)).readObject();

	}

	private JsonArray stringToJsonArray(final String jsonString) {

		return Json.createReader(new StringReader(jsonString)).readArray();

	}

	private boolean checkEsi(final String esi) {

		return Pattern.compile("^urn:schac:personalUniqueCode:int:esi:[a-zA-Z]+\\.[a-zA-Z]+:[0-9]{6}$")
				.matcher(esi)
				.matches();

	}

	private boolean checkEsc(final String esc) {

		return Pattern.compile("^[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[0-9]{12}+$")
				.matcher(esc)
				.matches();

	}

}
