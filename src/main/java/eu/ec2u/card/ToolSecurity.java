

/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.servlet.filter.Saml2WebSsoAuthenticationFilter;
import org.springframework.security.saml2.provider.service.web.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Component
@EnableWebSecurity
public class ToolSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private RelyingPartyRegistrationRepository relyingPartyRegistrationRepository;


    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        final RelyingPartyRegistrationResolver relyingPartyRegistrationResolver=
                new DefaultRelyingPartyRegistrationResolver(relyingPartyRegistrationRepository);

        http

                .csrf()
                .disable()

                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()

                .authorizeRequests()
                .antMatchers(GET, "/").permitAll()
                .antMatchers(POST, "/").permitAll()
                .anyRequest().authenticated()
                .and()

                .saml2Login(withDefaults())
                .saml2Logout(withDefaults())

                // publish SAML metadata endpoint at /saml2/service-provider-metadata/{registrationId}

                .addFilterBefore(
                        new Saml2MetadataFilter(relyingPartyRegistrationResolver, new OpenSamlMetadataResolver()),
                        Saml2WebSsoAuthenticationFilter.class
                );
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    public static final class Credentials implements Serializable {

        private static final long serialVersionUID=-5184703157125247153L;


        @NotNull private String code;

    }

    @Getter
    @Setter
    public static final class Profile implements Serializable {

        private static final long serialVersionUID=-7793479050451108354L;


        private boolean admin;

        @NotNull private String id;
        @NotNull private String code;
        @NotNull private String label;

        @NotNull private String forename;
        @NotNull private String surname;
        @NotNull private String email;

    }

}