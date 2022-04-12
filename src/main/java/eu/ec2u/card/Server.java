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

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

public final class Server {

    private static final String host="localhost";
    private static final int port=8080;

    private static final int backlog=128;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final String PortVariable="PORT";


    public static void main(final String... args) {
        Guice.createInjector(new Loader()).getInstance(Server.class).start();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Inject private Launcher launcher;
    @Inject private Router router;
    @Inject private Logger logger;


    private void start() {
        try {

            final HttpServer server=HttpServer.create(new InetSocketAddress(host,
                    Optional.ofNullable(System.getenv().get(PortVariable))

                            .map(value -> {

                                try { return Integer.parseInt(value); } catch ( final NumberFormatException e1 ) {
                                    return null;
                                }

                            })

                            .orElse(port)), backlog);

            server.setExecutor(Executors.newCachedThreadPool());

            server.createContext("/", exchange -> {
                try {

                    router.handle(exchange);

                } catch ( final RuntimeException e ) {

                    logger.log(SEVERE, "unhandled exception", e);

                    exchange.sendResponseHeaders(500, -1);

                }
            });


            Runtime.getRuntime().addShutdownHook(new Thread(() -> {

                logger.info("server stopping");

                try { server.stop(0); } catch ( final RuntimeException e ) {
                    logger.log(SEVERE, "unhandled exception while stopping server", e);
                }

                logger.info("server stopped");

            }));

            logger.info("server starting");

            server.start();

            logger.info(format("server listening at <http://%s:%d/>", host, Optional

                    .ofNullable(System.getenv().get(PortVariable))

                    .map(value -> {

                        try { return Integer.parseInt(value); } catch ( final NumberFormatException e1 ) { return null; }

                    })

                    .orElse(port)

            ));

        } catch ( final IOException e ) {
            throw new UncheckedIOException(e);
        }

    }

}
