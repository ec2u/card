/*
 *   Copyright © 2021 Luxottica. All rights reserved.
 */

package work;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Payload;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

import static com.metreeca.rest.Toolbox.service;
import static com.metreeca.rest.services.Vault.vault;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * JWT token manager.
 *
 * @see <a href="https://github.com/auth0/java-jwt">auth0/java-jwt</a>
 */
public final class Notary {

    private static final String KEY="key-jwt";


    public static Supplier<Notary> notary() {
        return () -> new Notary(service(vault()).get(KEY)
                .map(key -> key.getBytes(UTF_8))
                .orElseThrow(() -> new NoSuchElementException(format("undefined <%s> key", KEY)))
        );
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
