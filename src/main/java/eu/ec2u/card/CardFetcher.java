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

import com.google.gson.Gson;
import com.google.inject.Inject;
import eu.ec2u.card.CardData.Card;
import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.net.http.HttpClient.newHttpClient;

public final class CardFetcher {

    @Inject private CardSetup setup;
    @Inject private CardVault vault;

    @Inject private Gson gson;


    Stream<Card> fetch(final String esi) throws IOException {

        if ( esi == null ) {
            throw new NullPointerException("null esi");
        }

        try {
            // !!! error handling

            final String json=newHttpClient().send(

                    HttpRequest.newBuilder().GET()
                            .uri(URI.create(format("%s/v1/students/%s", setup.getEsc().getApi(), esi)))
                            .header("Key", vault.get(setup.getEsc().getKey()).orElseThrow())
                            .build(),

                    HttpResponse.BodyHandlers.ofString()

            ).body();

            final ESCStudent student=gson.fromJson(json, ESCStudent.class);

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
