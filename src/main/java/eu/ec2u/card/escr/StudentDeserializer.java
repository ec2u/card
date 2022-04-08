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

	public StudentDeserializer(final Class<?> t) {

		super(t);

	}

	@Override
	public StudentTransfer deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {

		final JsonNode studentNode = parser.getCodec().readTree(parser);

		final Set<String> cardsNumbersSet = new HashSet<>();

		final ArrayNode cardsNode = (ArrayNode) studentNode.get("cards");

		final Iterator<JsonNode> iterator = cardsNode.elements();

		while (iterator.hasNext()) {

			cardsNumbersSet.add(iterator.next().asText());

        }

		final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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


