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

package eu.ec2u.card.services;

import com.google.inject.Inject;
import eu.ec2u.card.Profile.*;
import eu.ec2u.card.Setup;
import lombok.Getter;

import java.io.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.String.format;
import static java.net.http.HttpClient.newHttpClient;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

public final class Fetcher {

    @Inject private Setup setup;
    @Inject private Vault vault;
    @Inject private Codec codec;


    public List<Card> fetch(final User user) {

        if ( user == null ) {
            throw new NullPointerException("null esi");
        }

        final String uni="unipv.it"; // !!! from user

        final HEI tenant=setup.getTenants().get(uni); // !!! handle missing elements

        final String key=tenant.getKey();

        final HttpRequest request=HttpRequest.newBuilder().GET()
                .uri(URI.create(format("%s/v1/students/%s", setup.getEsc().getApi(), user.getEsi())))
                .header("Key", vault.get(key).orElseThrow(() ->
                        new NoSuchElementException(format("undefined <%s> key", key))
                ))
                .build();

        try {

            final HttpResponse<InputStream> response=newHttpClient().send(request, BodyHandlers.ofInputStream());

            // !!! network error handling / return null on 404

            try (
                    final InputStream input=response.body();
                    final Reader reader=new InputStreamReader(input, UTF_8)
            ) {

                final ESCStudent student=codec.decode(reader, ESCStudent.class);

                // !!! assert required fields
                // !!! assert student.getPicInstitutionCode() == tenant.pic

                return student.cards.stream()

                        .map(card -> new Card()

                                .setCode(card.europeanStudentCardNumber)
                                .setTest(format("%s/%s", setup.getEsc().getTst(), card.europeanStudentCardNumber))

                                .setExpiry(student.getExpiryDate().toLocalDate())

                                .setEsi(student.getEuropeanStudentIdentifier())
                                .setLevel(student.getAcademicLevel())
                                .setName(student.getName())
                                .setPhoto(null) // !!!

                                .setHei(tenant)

                        )

                        .collect(toList());


            } catch ( final ParseException e ) {  // !!! handle

                throw new RuntimeException(e);

            }

        } catch ( final IOException e ) {  // !!! handle

            throw new UncheckedIOException(e);


        } catch ( final InterruptedException e ) {  // !!! handle

            throw new RuntimeException(e);

        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    private static final class ESCStudent {

        private String europeanStudentIdentifier;
        private int picInstitutionCode;
        private String emailAddress;

        private LocalDateTime expiryDate;
        private String name;
        private int academicLevel;

        private List<ESCCard> cards;


    }

    @Getter
    private static final class ESCCard {

        private String europeanStudentCardNumber;

    }

}
