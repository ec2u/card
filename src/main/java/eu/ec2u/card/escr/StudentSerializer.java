package eu.ec2u.card.escr;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class StudentSerializer extends StdSerializer<StudentTransfer> {

	public StudentSerializer(Class<StudentTransfer> t) {

		super(t);

	}

	@Override
	public void serialize(StudentTransfer student, JsonGenerator generator, SerializerProvider provider) throws IOException {

		generator.writeStartObject();
		generator.writeStringField("europeanStudentIdentifier", student.getEuropeanStudentIdentifier());
		generator.writeNumberField("picInstitutionCode", student.getPicInstitutionCode());
		generator.writeStringField("emailAddress", student.getEmailAddress());
		generator.writeStringField("expiryDate", student.getExpiryDate().toString());

		if (student.getName().isPresent()) {

			generator.writeStringField("name", student.getName().get());

        }

		if (student.getPhoneNumber().isPresent()) {

			generator.writeStringField("phoneNumber", student.getPhoneNumber().get());

		}

		if (student.getAcademicLevel().isPresent()) {

			generator.writeNumberField("academicLevel", student.getAcademicLevel().get());

		}

		generator.writeEndObject();

	}

}
