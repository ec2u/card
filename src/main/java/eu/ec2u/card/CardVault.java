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

import com.google.cloud.ServiceOptions;
import com.google.cloud.secretmanager.v1.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

import static java.util.stream.StreamSupport.stream;

public final class CardVault {

    public Optional<String> get(final String key) {

        if ( key == null ) {
            throw new NullPointerException("null key");
        }

        if ( key.isEmpty() ) {
            throw new IllegalArgumentException("empty key");
        }

        try {

            final SecretManagerServiceClient client=SecretManagerServiceClient.create();

            final String project=ServiceOptions.getDefaultProjectId();
            final Iterable<Secret> secrets=client.listSecrets(ProjectName.of(project)).iterateAll();

            if ( stream(secrets.spliterator(), false).anyMatch(secret ->
                    SecretName.parse(secret.getName()).getSecret().equals(key)
            ) ) {

                final AccessSecretVersionRequest request=AccessSecretVersionRequest.newBuilder()
                        .setName(SecretVersionName.of(project, key, "latest").toString())
                        .build();

                return Optional.of(client.accessSecretVersion(request).getPayload().getData().toStringUtf8());

            } else {

                return Optional.empty();

            }

        } catch ( final IOException e ) {
            throw new UncheckedIOException(e); // !!! handle
        }

    }

}
