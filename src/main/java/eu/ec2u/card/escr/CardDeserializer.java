package eu.ec2u.card.escr;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CardDeserializer extends StdDeserializer<CardTransfer> {

	public CardDeserializer() {

		this(null);

	}

	public CardDeserializer(Class<?> t) {

		super(t);

	}

	@Override
	public CardTransfer deserialize(JsonParser parser, DeserializationContext context) throws IOException {

		JsonNode cardNode = parser.getCodec().readTree(parser);

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		return new CardTransfer(
				cardNode.get("europeanStudentCardNumber").textValue(),
				cardNode.get("student").get("europeanStudentIdentifier").textValue(),
				cardNode.get("student").get("picInstitutionCode").longValue(),
				cardNode.get("student").get("emailAddress").textValue(),
				LocalDateTime.parse(cardNode.get("student").get("expiryDate").textValue(), inputFormatter),
				null,
				null

		);

	}

}
