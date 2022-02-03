
/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CardConfiguration {


    @Data
    @Configuration
    @ConfigurationProperties(prefix="card")
    public static class Build {

        private String version;
        private String instant;

    }

}
