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
import eu.ec2u.card.Setup;
import eu.ec2u.card.handlers.Root.Card;
import lombok.Getter;

import java.io.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.net.http.HttpClient.newHttpClient;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class Fetcher {

    @Inject private Setup setup;
    @Inject private Vault vault;
    @Inject private Codec codec;


    public Stream<Card> fetch(final String esi) throws IOException {

        if ( esi == null ) {
            throw new NullPointerException("null esi");
        }

        final HttpRequest request=HttpRequest.newBuilder().GET()
                .uri(URI.create(format("%s/v1/students/%s", setup.getEsc().getApi(), esi)))
                .header("Key", vault.get(setup.getEsc().getKey()).orElseThrow())
                .build();

        try {

            final HttpResponse<InputStream> response=newHttpClient().send(request, BodyHandlers.ofInputStream());

            // !!! network error handling

            try (
                    final InputStream input=response.body();
                    final Reader reader=new InputStreamReader(input, UTF_8)
            ) {

                final ESCStudent student=codec.decode(reader, ESCStudent.class);

                // !!! required fields

                return student.cards.stream().map(card -> new Card()

                        .setCode(card.europeanStudentCardNumber)
                        .setTest(format("%s/%s", setup.getEsc().getTst(), card.europeanStudentCardNumber))

                        .setExpiry(student.getExpiryDate().toLocalDate())

                        .setEsi(student.getEuropeanStudentIdentifier())
                        .setPic(student.getPicInstitutionCode())
                        .setLevel(student.getAcademicLevel())

                        .setName(student.getName())
                        //.setPhoto(student.???)
                        .setEmail(student.getEmailAddress())

                );


            } catch ( final ParseException e ) {  // !!! handle

                return Stream.empty();

            }

        } catch ( final InterruptedException e ) {  // !!! handle

            return Stream.empty();

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
