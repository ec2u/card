
/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="card")
public class ToolConfiguration {

    public static final int IdSizeLimit=100;
    public static final int LabelSizeLimit=100;
    public static final int NotesSizeLimit=2000;
    public static final int ContainerSizeLimit=100;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String revision;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Bean
    public ObjectMapper getObjectMapper() {
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


    private static final class EnumSerializer extends JsonSerializer<Enum<?>> {

        @Override public void serialize(
                final Enum<?> value, final JsonGenerator generator, final SerializerProvider provider
        ) throws IOException {

            generator.writeString(value.name().toLowerCase());

        }

    }

}
