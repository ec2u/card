package eu.ec2u.card.escr;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class CardSerializer extends StdSerializer<CardTransfer> {

	public CardSerializer(Class<CardTransfer> t) {

		super(t);

	}

	@Override
	public void serialize(
			CardTransfer card, JsonGenerator generator, SerializerProvider provider) throws IOException {

		generator.writeStartObject();
		generator.writeStringField("europeanStudentCardNumber", card.getEuropeanStudentCardNumber());

		if (card.getCardType().isPresent()) {

			generator.writeNumberField("cardType", card.getCardType().get());

		}

		if (card.getCardUid().isPresent()) {

			generator.writeStringField("cardUid", card.getCardUid().get());

		}

		generator.writeEndObject();

	}

}
