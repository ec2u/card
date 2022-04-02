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

package eu.ec2u.card.works;

import com.metreeca.rest.Xtream;
import com.metreeca.rest.actions.*;

import eu.ec2u.card.cards.Cards.Card;
import eu.europeanstudentcard.esc.EscnFactory;
import eu.europeanstudentcard.esc.EscnFactoryException;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.metreeca.rest.Toolbox.service;
import static com.metreeca.rest.formats.JSONFormat.json;
import static com.metreeca.rest.services.Vault.vault;

import static java.lang.String.format;


public final class ESC {

    private static final String API="https://api-sandbox.europeanstudentcard.eu";
    private static final String TST="http://pp.esc.gg";
    private static final String KEY="key-esc-sandbox";


    public static Supplier<ESC> esc() {
        return ESC::new;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final String key=service(vault()).get(KEY).orElseThrow(() ->
            new NoSuchElementException(format("undefined <%s> key", KEY))
    );


    private String code(final String pic) {
        try {

            return EscnFactory.getEscn(0, pic); // !!! prefix?

        } catch ( final EscnFactoryException|InterruptedException e ) {
            throw new RuntimeException(e); // !!!
        }
    }

    public Stream<Card> cards(final String esi) {

        if ( esi == null ) {
            throw new NullPointerException("null esi");
        }

        return Xtream.of(esi)

                .flatMap(new Fill<>()

                        .model("{api}/v1/students/{esi}")

                        .value("api", API)
                        .value("esi")

                )

                .optMap(new GET<>(json(), request -> request.header("Key", key)))

                .flatMap(new JSONPath<>(json -> json.strings("cards.*.europeanStudentCardNumber").map(code -> new Card() // !!! error handling

                        .setCode(code)
                        .setTest(format("%s/%s", TST, code))

                        .setExpiry(json.string("expiryDate")
                                .map(ZonedDateTime::parse)
                                .map(ZonedDateTime::toLocalDate)
                                .orElseThrow()
                        )

                        .setEsi(json.string("europeanStudentIdentifier").orElseThrow())
                        .setPic(json.integer("picInstitutionCode").map(BigInteger::intValue).orElseThrow())
                        .setLevel(json.integer("academicLevel").map(BigInteger::intValue).orElseThrow())

                        .setName(json.string("name").orElseThrow())
                        .setPhoto(json.string("photoID").orElse(null)) // !!!
                        .setEmail(json.string("emailAddress").orElse(null)))

                ));
    }

}
