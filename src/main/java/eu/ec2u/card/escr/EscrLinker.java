package eu.ec2u.card.escr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
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
public class EscrLinker {

	private static final String BASE_PATH = "https://api-sandbox.europeanstudentcard.eu/v1";

	@Autowired EscrHttpManager httpManager;

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
				System.err.println(e.getMessage());
				studentsList.clear();
			}

		});

		return studentsList;

	}

	public String retrieveStudent(final String esi)
			throws IllegalArgumentException {

		if (checkEsi(esi)) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		return httpManager.httpGetter(BASE_PATH + "/students/" + esi);

	}

	public Optional<StudentTransfer> retrieveStudentWithTransferObject(final String esi)
			throws IllegalArgumentException, JsonProcessingException {

		if (checkEsi(esi)) {

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

		if (checkEsi(studentTransfer.getEuropeanStudentIdentifier())) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

			httpManager.httpPutter(
					BASE_PATH + "/students/" + studentTransfer.getEuropeanStudentIdentifier(),
					studentTransferToStudentJson(studentTransfer)
			);

	}

	public void deleteStudent(final StudentTransfer studentTransfer)
			throws IllegalArgumentException {

		if (checkEsi(studentTransfer.getEuropeanStudentIdentifier())) {

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
				System.err.println(e.getMessage());
				cardsList.clear();
			}

		});

		return cardsList;

	}

	public String retrieveCard(final String esi, final String esc)
			throws IllegalArgumentException {

		if (checkEsi(esi)) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		if (checkEsc(esc)) {

			throw new IllegalArgumentException("European Student Card doesn't match the correct format");

		}

		return httpManager.httpGetter(BASE_PATH + "/students/" + esi + "/cards/" + esc);

	}

	public Optional<CardTransfer> retrieveCardWithTransferObject(final String esi, final String esc)
			throws IllegalArgumentException, JsonProcessingException {

		if (checkEsi(esi)) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		if (checkEsc(esc)) {

			throw new IllegalArgumentException("European Student Card doesn't match the correct format");

		}

		System.out.println(httpManager.httpGetter(BASE_PATH + "/students/" + esi + "/cards/" + esc));
		return cardJsonToCardTransfer(httpManager.httpGetter(BASE_PATH + "/students/" + esi + "/cards/" + esc));

	}

	public void addCard(final CardTransfer cardTransfer)
			throws IllegalArgumentException, JsonProcessingException {

		if (checkEsi(cardTransfer.getEuropeanStudentIdentifier())) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		httpManager.httpPoster(
				BASE_PATH + "/students/" + cardTransfer.getEuropeanStudentIdentifier() + "/cards",
				cardTransferToCardJson(cardTransfer)
		);

	}

	public void updateCardDetails(final CardTransfer cardTransfer)
			throws IllegalArgumentException, JsonProcessingException {

		if (checkEsi(cardTransfer.getEuropeanStudentIdentifier())) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		httpManager.httpPutter(
				BASE_PATH + "/students/" + cardTransfer.getEuropeanStudentIdentifier(),
				cardTransferToCardJson(cardTransfer)
		);

	}

	public void deleteCard(final CardTransfer cardTransfer)
			throws IllegalArgumentException {

		if (checkEsi(cardTransfer.getEuropeanStudentIdentifier())) {

			throw new IllegalArgumentException("European Student Identifier doesn't match the correct format");

		}

		if (checkEsc(cardTransfer.getEuropeanStudentCardNumber())) {

			throw new IllegalArgumentException("esc or esc doesn't match the correct format");

		}

			httpManager.httpDeleter(BASE_PATH + "/students/" + cardTransfer.getEuropeanStudentIdentifier()
					+ "/cards/" + cardTransfer.getEuropeanStudentCardNumber());

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private boolean checkEsi(final String esi) {

		return !Pattern.compile("^urn:schac:personalUniqueCode:int:esi:[a-zA-Z]+\\.[a-zA-Z]+:[0-9]{6}$")
				.matcher(esi)
				.matches();

	}

	private boolean checkEsc(final String esc) {

		return !Pattern.compile("^[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[0-9]{12}+$")
				.matcher(esc)
				.matches();

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private Optional<StudentTransfer> studentJsonToStudentTransfer(final String studentJson) throws JsonProcessingException {

		if (studentJson.contains("this student does not exist")) {

			return Optional.empty();

		} else {

			return Optional.ofNullable(bindJsonDeserializer("StudentDeserializer").readValue(studentJson, StudentTransfer.class));

		}

	}

	private Optional<CardTransfer> cardJsonToCardTransfer(final String cardJson) throws JsonProcessingException {

		if (cardJson.contains("this student does not exist") || cardJson.contains("this card does not exist")) {

			return Optional.empty();

		} else {

			return Optional.ofNullable(bindJsonDeserializer("CardDeserializer").readValue(cardJson, CardTransfer.class));

		}

	}

	private String cardTransferToCardJson(final CardTransfer cardTransfer) throws JsonProcessingException {

		return bindJsonSerializer("CardSerializer").writeValueAsString(cardTransfer);

	}

	private String studentTransferToStudentJson(final StudentTransfer studentTransfer) throws JsonProcessingException {

		return bindJsonSerializer("StudentSerializer").writeValueAsString(studentTransfer);

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private ObjectMapper bindJsonSerializer(final String serializerName) {

		final ObjectMapper mapper = new ObjectMapper();

		final SimpleModule module =
				new SimpleModule(
						serializerName,
						new Version(1, 0, 0, null, null, null)
				);

		if(serializerName.equalsIgnoreCase("StudentSerializer")) {

			module.addSerializer(StudentTransfer.class, new StudentSerializer(StudentTransfer.class));

		} else if (serializerName.equalsIgnoreCase("CardSerializer")) {

			module.addSerializer(CardTransfer.class, new CardSerializer(CardTransfer.class));

        }

		return mapper.registerModule(module);

	}

	private ObjectMapper bindJsonDeserializer(final String type) {

		final String deserializerName = "";

		final ObjectMapper mapper = new ObjectMapper();

		final SimpleModule module =
				new SimpleModule(
						deserializerName,
						new Version(1, 0, 0, null, null, null)
				);

		if(type.contains("Student")) {

			module.addDeserializer(StudentTransfer.class, new StudentDeserializer());

		} else if (type.contains("Card")) {

			module.addDeserializer(CardTransfer.class, new CardDeserializer());

		}

		return mapper.registerModule(module);

	}

}
