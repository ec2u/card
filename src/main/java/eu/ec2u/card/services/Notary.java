/*
 *   Copyright © 2021 Luxottica. All rights reserved.
 */

package eu.ec2u.card.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Payload;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * JWT token manager.
 *
 * @see <a href="https://github.com/auth0/java-jwt">auth0/java-jwt</a>
 */
public final class Notary {


    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    private final Duration timeout=Duration.ofMinutes(60);


    public Notary(final byte[] key) {

        if ( key == null ) {
            throw new NullPointerException("null key");
        }

        this.algorithm=Algorithm.HMAC256(key);
        this.verifier=JWT.require(algorithm).build();

    }


    public String create(final String subject) {

        if ( subject == null ) {
            throw new NullPointerException("null subject");
        }

        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(Date.from(Instant.now().plus(timeout)))
                .sign(algorithm);
    }

    public Optional<String> verify(final String token) throws SecurityException {

        if ( token == null ) {
            throw new NullPointerException("null token");
        }

        try {

            return Optional.of(token)
                    .map(verifier::verify)
                    .filter(jwt -> jwt.getExpiresAt().toInstant().compareTo(Instant.now()) > 0)
                    .map(Payload::getSubject);

        } catch ( final JWTVerificationException e ) {

            throw new SecurityException(e.getMessage(), e);

        }

    }

}
