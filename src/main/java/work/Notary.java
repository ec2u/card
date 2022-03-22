/*
 *   Copyright © 2021 Luxottica. All rights reserved.
 */

package work;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Payload;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import javax.json.*;
import javax.json.stream.JsonParsingException;

/**
 * JWT token manager.
 *
 * @see <a href="https://github.com/auth0/java-jwt">auth0/java-jwt</a>
 */
public final class Notary {

    public static String encode(final JsonValue value) {

        if ( value == null ) {
            throw new NullPointerException("null value");
        }

        try (
                final Writer buffer=new StringWriter();
                final JsonWriter writer=Json.createWriter(buffer)
        ) {

            writer.write(value);

            return buffer.toString();

        } catch ( final IOException e ) {

            throw new UncheckedIOException(e);

        }

    }

    public static JsonValue decode(final String json) {

        if ( json == null ) {
            throw new NullPointerException("null json");
        }

        try (
                final Reader buffer=new StringReader(json);
                final JsonReader reader=Json.createReader(buffer)
        ) {

            return reader.readValue();

        } catch ( final JsonParsingException e ) {

            throw new RuntimeException(e.getMessage(), e);

        } catch ( final IOException e ) {

            throw new UncheckedIOException(e);

        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    private final Duration timeout=Duration.ofDays(1);


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
