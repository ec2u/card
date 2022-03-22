/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card;

import com.metreeca.gcp.GCPServer;
import com.metreeca.gcp.services.GCPStore;
import com.metreeca.gcp.services.GCPVault;
import com.metreeca.rest.services.Cache.FileCache;
import com.metreeca.rest.services.Fetcher.CacheFetcher;

import eu.ec2u.card.cards.CardsHandler;
import eu.ec2u.card.keys.KeysHandler;
import eu.ec2u.card.users.UsersHandler;
import eu.ec2u.card.works.ESC;
import eu.ec2u.card.works.SAML;
import work.Fallback;

import java.nio.file.Paths;
import java.time.Instant;

import static com.metreeca.gcp.GCPServer.development;
import static com.metreeca.gcp.GCPServer.production;
import static com.metreeca.rest.Handler.asset;
import static com.metreeca.rest.Toolbox.storage;
import static com.metreeca.rest.handlers.Router.router;
import static com.metreeca.rest.services.Cache.cache;
import static com.metreeca.rest.services.Fetcher.fetcher;
import static com.metreeca.rest.services.Logger.Level.debug;
import static com.metreeca.rest.services.Store.store;
import static com.metreeca.rest.services.Vault.vault;
import static com.metreeca.rest.wrappers.CORS.cors;
import static com.metreeca.rest.wrappers.Server.server;

import static eu.ec2u.card.works.ESC.esc;

import static java.time.Duration.ofHours;

public final class RootServer implements Runnable {

    public static final int ContainerSize=100;

    public static final int URLSize=100;
    public static final String RelativePattern="/(\\w+/)*\\w+/?";
    public static final String AbsolutePattern="\\w+:\\S+";
    public static final String ESIPattern="urn:schac:personalUniqueCode:int:esi:[-:.\\w]+";

    public static final int LineSize=100;
    public static final String LinePattern="\\S+( \\S+)*";

    public static final int TextSize=2000;
    public static final String TextPattern="\\S+(\\s+\\S+)*";


    static {

        debug.log("com.metreeca");

        if ( development() ) { work.SSL.untrusted(); } // disable SSL checks while developing

    }

    static Instant revision() {

        throw new UnsupportedOperationException(";(  be implemented"); // !!!

        //return LocalDateTime.from(Card.InstantFormat.parse(revision)).toInstant(ZoneOffset.UTC);
    }


    public static void main(final String... args) {
        new RootServer().run();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void run() {
        new GCPServer().delegate(toolbox -> toolbox

                .set(esc(), ESC::new)

                .set(vault(), GCPVault::new)
                .set(store(), GCPStore::new)

                .set(storage(), () -> Paths.get(production() ? "/tmp" : "data"))
                .set(fetcher(), CacheFetcher::new)
                .set(cache(), () -> new FileCache().ttl(ofHours(1)))

                .get(() -> server()

                        .with(cors())

                        .with(new RootKeeper())

                        .wrap(router()

                                .path(SAML.Pattern, new SAML())

                                .path("/*", asset(new Fallback("/index.html"), router()

                                        .path("/", new RootHandler())
                                        .path("/cards/*", new CardsHandler())
                                        .path("/users/*", new UsersHandler())
                                        .path("/keys/*", new KeysHandler())

                                ))

                        )
                )

        ).start();
    }

}
