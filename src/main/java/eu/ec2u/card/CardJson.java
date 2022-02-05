

/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Configuration
public class CardJson {

    @Bean
    public ObjectMapper mapper() {
        return new Jackson2ObjectMapperBuilder()

                .indentOutput(true)
                .defaultViewInclusion(true)
                .failOnUnknownProperties(true)

                .serializationInclusion(NON_NULL)

                .serializersByType(Map.of(
                        Enum.class, new EnumSerializer()
                ))

                .featuresToEnable(
                        MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS
                )

                .featuresToDisable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
                )

                .build();
    }


    //// Views /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final class Catalog {
        private Catalog() { }
    }

    public static final class Details {
        private Details() { }
    }


    //// Codecs ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final class EnumSerializer extends JsonSerializer<Enum<?>> {

        @Override public void serialize(
                final Enum<?> value, final JsonGenerator generator, final SerializerProvider provider
        ) throws IOException {

            generator.writeString(value.name().toLowerCase());

        }

    }

}
