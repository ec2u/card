package eu.ec2u.card.escr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.json.Json;
import javax.json.JsonArray;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor
public class NewEscrLinker {

	private static final String BASE_PATH = "https://api-sandbox.europeanstudentcard.eu/v1";

	@Autowired
	EscrHttpManager httpManager;

	@Autowired
	ObjectMapper mapper;

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public String listStudents() {

		return httpManager.httpGetter(BASE_PATH + "/students");

	}

	public List<StudentTransfer> listStudentsWithTransferObject() {

		final List<StudentTransfer> studentsList = new ArrayList<>();

		final JsonArray studentsJsonArray = Json.createReader(new StringReader(listStudents())).readArray();

		studentsJsonArray.forEach(jsonValue -> {

			try {

				if (studentJsonToStudentTransfer(jsonValue.toString()).isPresent()) {
					studentsList.add(studentJsonToStudentTransfer(jsonValue.toString()).get());
				}

			} catch (final JsonProcessingException e) {
				e.printStackTrace();
			}

		});

		return studentsList;

	}

	public String retrieveStudent(final String esi)
			throws IllegalArgumentException {

		if (isEsiValid(esi)) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		return httpManager.httpGetter(BASE_PATH + "/students/" + esi);


	}

	public Optional<StudentTransfer> retrieveStudentWithTransferObject(final String esi)
			throws IllegalArgumentException, JsonProcessingException {

		if (isEsiValid(esi)) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		return studentJsonToStudentTransfer(httpManager.httpGetter(BASE_PATH + "/students/" + esi));

	}

	public void addStudent(final StudentTransfer student)
			throws JsonProcessingException {

		httpManager.httpPoster(
				BASE_PATH + "/students",
				studentTransferToStudentJson(student)
		);

	}

	public void updateStudentDetails(final StudentTransfer studentTransfer)
			throws IllegalArgumentException, JsonProcessingException {

		if (isEsiValid(studentTransfer.getEuropeanStudentIdentifier())) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		httpManager.httpPutter(
				BASE_PATH + "/students/" + studentTransfer.getEuropeanStudentIdentifier(),
				studentTransferToStudentJson(studentTransfer)
		);

	}

	public void deleteStudent(final StudentTransfer studentTransfer)
			throws IllegalArgumentException {

		if (isEsiValid(studentTransfer.getEuropeanStudentIdentifier())) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		httpManager.httpDeleter(BASE_PATH + "/students/" + studentTransfer.getEuropeanStudentIdentifier());

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public String listCards() {

		return httpManager.httpGetter(BASE_PATH + "/cards");

	}

	public List<CardTransfer> listCardsWithTransferObject() {

		final List<CardTransfer> cardsList = new ArrayList<>();

		final JsonArray cardsJsonArray = Json.createReader(new StringReader(listCards())).readArray();

		cardsJsonArray.forEach(jsonValue -> {

			try {

				if (cardJsonToCardTransfer(jsonValue.toString()).isPresent()) {
					cardsList.add(cardJsonToCardTransfer(jsonValue.toString()).get());
				}

			} catch (final JsonProcessingException e) {
				e.printStackTrace();
			}

		});

		return cardsList;

	}

	public String retrieveCard(final String esi, final String esc)
			throws IllegalArgumentException {

		if (isEsiValid(esi)) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		if (isEscValid(esc)) {

			throw new IllegalArgumentException("European Student Card doesn't match the correct format");

		}

		return httpManager.httpGetter(BASE_PATH + "/students/" + esi + "/cards/" + esc);

	}

	public Optional<CardTransfer> retrieveCardWithTransferObject(final String esi, final String esc)
			throws IllegalArgumentException, JsonProcessingException {

		if (isEsiValid(esi)) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		if (isEscValid(esc)) {

			throw new IllegalArgumentException("European Student Card doesn't match the correct format");

		}

		return cardJsonToCardTransfer(httpManager.httpGetter(BASE_PATH + "/students/" + esi + "/cards/" + esc));

	}

	public void addCard(final CardTransfer cardTransfer)
			throws IllegalArgumentException, JsonProcessingException {

		if (isEsiValid(cardTransfer.getEuropeanStudentIdentifier())) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		httpManager.httpPoster(
				BASE_PATH + "/students/" + cardTransfer.getEuropeanStudentIdentifier() + "/cards",
				cardTransferToCardJson(cardTransfer)
		);

	}

	public void updateCardDetails(final CardTransfer cardTransfer)
			throws IllegalArgumentException, JsonProcessingException {

		if (isEsiValid(cardTransfer.getEuropeanStudentIdentifier())) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		httpManager.httpPutter(
				BASE_PATH + "/students/" + cardTransfer.getEuropeanStudentIdentifier(),
				cardTransferToCardJson(cardTransfer)
		);

	}

	public void deleteCard(final CardTransfer cardTransfer)
			throws IllegalArgumentException {

		if (isEsiValid(cardTransfer.getEuropeanStudentIdentifier())) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		if (isEscValid(cardTransfer.getEuropeanStudentCardNumber())) {

			throw new IllegalArgumentException("European Student Card doesn't match the correct format");

		}

		httpManager.httpDeleter(BASE_PATH + "/students/" + cardTransfer.getEuropeanStudentIdentifier()
				+ "/cards/" + cardTransfer.getEuropeanStudentCardNumber());

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private boolean isEsiValid(final String esi) {

		return !Pattern.compile("^urn:schac:personalUniqueCode:int:esi:[a-zA-Z]+\\.[a-zA-Z]+:[0-9]{6}$")
				.matcher(esi)
				.matches();

	}

	private boolean isEscValid(final String esc) {

		return !Pattern.compile("^[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[0-9]{12}+$")
				.matcher(esc)
				.matches();

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private Optional<StudentTransfer> studentJsonToStudentTransfer(final String studentJson) throws JsonProcessingException {

		if(studentJson.contains("this student does not exist")) {

			return Optional.empty();

		} else {

			return Optional.of(mapper.readValue(studentJson, StudentTransfer.class));

		}

	}

	private Optional<CardTransfer> cardJsonToCardTransfer(final String cardJson) throws JsonProcessingException {

		if (cardJson.contains("this student does not exist") || cardJson.contains("this card does not exist")) {

			return Optional.empty();

		} else {

			return Optional.of(mapper.readValue(cardJson, CardTransfer.class));

		}

	}

	private String studentTransferToStudentJson(final StudentTransfer studentTransfer) throws JsonProcessingException {

		return mapper.writeValueAsString(studentTransfer);

	}

	private String cardTransferToCardJson(final CardTransfer cardTransfer) throws JsonProcessingException {

		return mapper.writeValueAsString(cardTransfer);

	}

}
