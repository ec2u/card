package eu.ec2u.card.escr;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StudentDeserializer extends StdDeserializer<StudentTransfer> {

	public StudentDeserializer() {

		this(null);

	}

	public StudentDeserializer(Class<?> t) {

		super(t);

	}

	@Override
	public StudentTransfer deserialize(JsonParser parser, DeserializationContext context) throws IOException {

		JsonNode studentNode = parser.getCodec().readTree(parser);

		Set<String> cardsNumbersSet = new HashSet<>();

		ArrayNode cardsNode = (ArrayNode) studentNode.get("cards");

		Iterator<JsonNode> iterator = cardsNode.elements();

		while (iterator.hasNext()) {

			cardsNumbersSet.add(iterator.next().asText());

        }

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		return new StudentTransfer(

				studentNode.get("europeanStudentIdentifier").textValue(),
				studentNode.get("picInstitutionCode").longValue(),
				studentNode.get("emailAddress").textValue(),
				LocalDateTime.parse(studentNode.get("expiryDate").textValue(), inputFormatter),
				cardsNumbersSet,
				null,
				null,
				null

		);

	}
}


