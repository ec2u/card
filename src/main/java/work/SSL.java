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

package work;

import java.security.*;
import java.security.cert.X509Certificate;

import javax.net.ssl.*;

public final class SSL {

    public static void untrusted() {
        try {
            final SSLContext context=SSLContext.getInstance("TLS");

            context.init(null, new TrustManager[]{ new DummyTrustManager() }, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

        } catch ( final NoSuchAlgorithmException|KeyManagementException e ) {

            throw new RuntimeException(e);

        }
    }

    private static final class DummyTrustManager implements X509TrustManager {

        private static final X509Certificate[] certificates={};


        public X509Certificate[] getAcceptedIssuers() { return certificates; }


        @Override public void checkClientTrusted(final X509Certificate[] chain, final String authType) { }

        @Override public void checkServerTrusted(final X509Certificate[] chain, final String authType) { }

    }

}
