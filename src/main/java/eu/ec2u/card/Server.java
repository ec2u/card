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
import eu.ec2u.card.handlers.Root;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.logging.*;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.logging.Level.*;

public final class Server {

    private static final String host="localhost";

    private static final int port=Optional.ofNullable(System.getenv().get("PORT"))

            .map(value -> {

                try { return Integer.parseInt(value); } catch ( final NumberFormatException e ) { return null; }

            })

            .orElse(8080);


    private static final int backlog=128;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final Pattern EOLPattern=Pattern.compile("\n");

    static {  // logging not configured: reset and load compact console configuration ;(unless on GAE)

        if ( System.getProperty("java.util.logging.config.file") == null
                && System.getProperty("java.util.logging.config.class") == null
                && !"Production".equals(System.getProperty("com.google.appengine.runtime.environment"))
        ) {

            final Logger logger=Logger.getLogger("");

            for (final Handler h : logger.getHandlers()) { logger.removeHandler(h); } // clear existing handlers

            logger.setLevel(INFO);

            final ConsoleHandler handler=new ConsoleHandler();

            handler.setLevel(ALL); // enable detailed reporting from children loggers

            handler.setFormatter(new Formatter() {

                @Override public String format(final LogRecord record) {

                    return String.format(Locale.ROOT, "%3s %-15s %s%s\n",
                            level(record.getLevel()),
                            name(record.getLoggerName()),
                            message(record.getMessage()),
                            trace(record.getThrown()));

                }


                private String level(final Level level) {
                    return level.equals(Level.SEVERE) ? "!!!"
                            : level.equals(Level.WARNING) ? "!!"
                            : level.equals(INFO) ? "!"
                            : level.equals(Level.FINE) ? "?"
                            : level.equals(Level.FINER) ? "??"
                            : level.equals(Level.FINEST) ? "???"
                            : "";
                }

                private String name(final String name) {
                    return name == null ? "<global>" : name.substring(name.lastIndexOf('.')+1);
                }

                private String message(final CharSequence message) {
                    return message == null ? "" : EOLPattern.matcher(message).replaceAll("\n    ");
                }

                private String trace(final Throwable cause) {
                    if ( cause == null ) { return ""; } else {
                        try (
                                final StringWriter writer=new StringWriter();
                                final PrintWriter printer=new PrintWriter(writer.append(' '))
                        ) {

                            printer.append("caused by ");

                            cause.printStackTrace(printer);

                            return writer.toString();

                        } catch ( final IOException unexpected ) {
                            throw new UncheckedIOException(unexpected);
                        }
                    }
                }

            });

            logger.addHandler(handler);

        }

    }


    public static void main(final String... args) {
        Guice.createInjector(new Services()).getInstance(Server.class).start();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Inject private Root.Handler handler;

    @Inject private Logger logger;


    private void start() {
        try {

            final HttpServer server=HttpServer.create(new InetSocketAddress(host, port), backlog);

            server.setExecutor(Executors.newCachedThreadPool());

            server.createContext("/", exchange -> {
                try {

                    handler.handle(exchange);

                } catch ( final RuntimeException e ) {

                    logger.log(SEVERE, "unhandled exception", e);

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

            logger.info(format("server listening at <http://%s:%d/>", host, port));

        } catch ( final IOException e ) {
            throw new UncheckedIOException(e);
        }

    }

}
