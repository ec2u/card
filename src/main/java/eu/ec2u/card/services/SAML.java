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
import com.onelogin.saml2.settings.Saml2Settings;
import com.onelogin.saml2.settings.SettingsBuilder;
import eu.ec2u.card.Profile.HEI;
import eu.ec2u.card.Setup;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public final class SAML {

    final Map<String, Object> defaults=Map.ofEntries(

            //#### Security Settings ###########################################################################

            entry("onelogin.saml2.strict", true),
            entry("onelogin.saml2.security.want_xml_validation", true),
            entry("onelogin.saml2.security.reject_deprecated_alg", true),
            entry("onelogin.saml2.security.signature_algorithm", "http://www.w3.org/2001/04/xmldsig-more#rsa"
                    +"-sha256"),
            entry("onelogin.saml2.security.digest_algorithm ", "http://www.w3.org/2001/04/xmlenc#sha256"),

            //#### Service Provider Data #######################################################################

            entry("onelogin.saml2.sp.entityid", "https://127.0.0.1:3000/saml/"),

            entry("onelogin.saml2.sp.assertion_consumer_service.url", "https://127.0.0.1:3000/saml/acs"),
            entry("onelogin.saml2.sp.nameidformat", "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress"),


            //#### Organization ######################################################################

            entry("onelogin.saml2.organization.name", "EC2U Connect Centre"),
            entry("onelogin.saml2.organization.url", "https://www.ec2u.eu/"),
            entry("onelogin.saml2.contacts.technical.email_address", "cc@ml.ec2u.eu")

    );


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Inject private Setup setup;


    public Saml2Settings settings(final String hei) {

        final Map<String, Object> values=new HashMap<>(defaults);

        if ( hei != null ) {

            final HEI tenant=setup.getTenants().get(hei);

            if ( tenant == null ) {
                throw new RuntimeException("unknown tenant"); // !!! report
            }

            values.put("onelogin.saml2.idp.entityid", tenant.getIdp().getEid());
            values.put("onelogin.saml2.idp.single_sign_on_service.url", tenant.getIdp().getSso());
            values.put("onelogin.saml2.idp.x509cert", tenant.getIdp().getCrt());

        }

        final Saml2Settings settings=new SettingsBuilder().fromValues(values).build();

        //final List<String> settingsErrors = settings.checkSettings();
        //if (!settingsErrors.isEmpty()) {
        //    String errorMsg = "Invalid settings: ";
        //    errorMsg += StringUtils.join(settingsErrors, ", ");
        //    LOGGER.error(errorMsg);
        //    throw new SettingsException(errorMsg, SettingsException.SETTINGS_INVALID);
        //}
        //LOGGER.debug("Settings validated");

        return settings;

    }

}
