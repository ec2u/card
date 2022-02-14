

/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Component
@EnableWebSecurity
public class CardSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http

                .csrf()
                .disable()

                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()

                .authorizeRequests()
                .antMatchers(GET, "/").permitAll()
                .antMatchers(POST, "/").permitAll()
                .anyRequest().permitAll(); // !!!
        //.anyRequest().authenticated();

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Jacksonized
    @SuperBuilder
    public static final class Credentials implements Serializable {

        private static final long serialVersionUID=-5184703157125247153L;


        @NonNull private final String code;

    }

    @Getter
    @Jacksonized
    @SuperBuilder
    public static final class Profile implements Serializable {

        private static final long serialVersionUID=-7793479050451108354L;


        private final boolean admin;

        @NonNull private final String id;
        @NonNull private final String code;
        @NonNull private final String label;

        @NonNull private final String forename;
        @NonNull private final String surname;
        @NonNull private final String email;

    }

}
