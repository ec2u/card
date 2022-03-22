/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.work;

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
