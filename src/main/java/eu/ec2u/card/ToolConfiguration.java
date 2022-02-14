
/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import com.fasterxml.jackson.databind.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="card")
public class ToolConfiguration {

    public static final int IdSize=100;
    public static final int ContainerSize=100;

    public static final int LabelSize=100;
    public static final String LabelPattern="\\S+( \\S+)*";

    public static final int NotesSize=2000;
    public static final String NotesPattern="\\S+(\\s+\\S+)*";


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

                .featuresToEnable(
                        MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS
                )

                .featuresToDisable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
                )

                .build();
    }


}
