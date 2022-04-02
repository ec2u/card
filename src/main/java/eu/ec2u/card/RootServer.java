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

import com.metreeca.gcp.GCPServer;
import com.metreeca.gcp.services.GCPStore;
import com.metreeca.gcp.services.GCPVault;
import com.metreeca.rest.services.Cache.FileCache;
import com.metreeca.rest.services.Fetcher.CacheFetcher;

import eu.ec2u.card.cards.CardsHandler;
import eu.ec2u.card.keys.KeysHandler;
import eu.ec2u.card.users.UsersHandler;
import eu.ec2u.card.works.ESC;
import work.Launcher;

import java.nio.file.Paths;

import static com.metreeca.gcp.GCPServer.development;
import static com.metreeca.gcp.GCPServer.production;
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
                        .with(new Launcher("/index.html"))

                        .wrap(router()
                                .path("/", new RootHandler())
                                .path("/cards/*", new CardsHandler())
                                .path("/users/*", new UsersHandler())
                                .path("/keys/*", new KeysHandler())
                        )

                )

        ).start();
    }

}
