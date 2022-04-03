/*
 * Copyright © 2020-2022 EC2U Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.ec2u.card;

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
final class CardNotary {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    private final Duration timeout=Duration.ofMinutes(60);


    CardNotary(final byte[] key) {

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
