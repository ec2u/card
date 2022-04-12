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

import com.onelogin.saml2.exception.Error;
import com.onelogin.saml2.settings.Saml2Settings;
import com.onelogin.saml2.settings.SettingsBuilder;

import java.io.IOException;
import java.io.UncheckedIOException;

public final class Settings {

    private Saml2Settings settings;


    public Saml2Settings get() {
        try {

            final Saml2Settings settings=
                    new SettingsBuilder().fromFile("eu/ec2u/card/services/Settings.properties").build();

            //final List<String> settingsErrors = settings.checkSettings();
            //if (!settingsErrors.isEmpty()) {
            //    String errorMsg = "Invalid settings: ";
            //    errorMsg += StringUtils.join(settingsErrors, ", ");
            //    LOGGER.error(errorMsg);
            //    throw new SettingsException(errorMsg, SettingsException.SETTINGS_INVALID);
            //}
            //LOGGER.debug("Settings validated");

            return settings;

        } catch ( final Error e ) {

            throw new RuntimeException(e); // !!!

        } catch ( final IOException e ) {

            throw new UncheckedIOException(e);

        }
    }

}
