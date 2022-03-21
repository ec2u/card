/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card;


import com.metreeca.gcp.GCPServer;
import com.metreeca.gcp.services.GCPStore;
import com.metreeca.gcp.services.GCPVault;
import com.metreeca.rest.handlers.Router;
import com.metreeca.rest.services.Cache.FileCache;
import com.metreeca.rest.services.Fetcher.CacheFetcher;

import eu.ec2u.card.cards.Cards;
import eu.ec2u.card.cards.Cards.Card;
import eu.ec2u.card.tools.SAML;
import eu.ec2u.card.users.Users;
import eu.ec2u.card.users.Users.User;
import eu.ec2u.card.work.*;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

import javax.json.*;
import javax.validation.constraints.*;

import static com.metreeca.gcp.GCPServer.development;
import static com.metreeca.rest.Handler.asset;
import static com.metreeca.rest.Response.OK;
import static com.metreeca.rest.Toolbox.storage;
import static com.metreeca.rest.formats.JSONFormat.json;
import static com.metreeca.rest.formats.JSONLDFormat.keywords;
import static com.metreeca.rest.handlers.Router.router;
import static com.metreeca.rest.services.Cache.cache;
import static com.metreeca.rest.services.Fetcher.fetcher;
import static com.metreeca.rest.services.Logger.Level.debug;
import static com.metreeca.rest.services.Store.store;
import static com.metreeca.rest.services.Vault.vault;
import static com.metreeca.rest.wrappers.Bearer.bearer;
import static com.metreeca.rest.wrappers.CORS.cors;
import static com.metreeca.rest.wrappers.Server.server;

import static eu.ec2u.card.users.ESC.cards;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.ofHours;
import static java.util.Map.entry;

import static javax.json.JsonValue.NULL;

@Getter
@Setter
public final class Root {

    public static final String Id="/";

    public static final int ContainerSize=100;

    public static final int URLSize=100;
    public static final String RelativePattern="/(\\w+/)*\\w+/?";
    public static final String AbsolutePattern="\\w+:\\S+";
    public static final String ESIPattern="urn:schac:personalUniqueCode:int:esi:[-:.\\w]+";

    public static final int LineSize=100;
    public static final String LinePattern="\\S+( \\S+)*";

    public static final int TextSize=2000;
    public static final String TextPattern="\\S+(\\s+\\S+)*";


    public static final byte[] JWTKey="BB9BD1C95FBCD778BDB66ACD5158D".getBytes(UTF_8); // !!! to vault


    static {

        debug.log("com.metreeca");

        if ( development() ) { SSL.untrusted(); } // disable SSL checks while developing

    }


    static Instant revision() {

        throw new UnsupportedOperationException(";(  be implemented"); // !!!

        //return LocalDateTime.from(Card.InstantFormat.parse(revision)).toInstant(ZoneOffset.UTC);
    }


    public static void main(final String... args) {
        new GCPServer().delegate(toolbox -> toolbox

                .set(vault(), GCPVault::new)
                .set(store(), GCPStore::new)

                .set(storage(), () -> Paths.get("data"))
                .set(fetcher(), CacheFetcher::new)
                .set(cache(), () -> new FileCache().ttl(ofHours(1)))

                .set(keywords(), () -> Map.ofEntries(
                        entry("@id", "id"),
                        entry("@type", "type")
                ))

                .get(() -> server()

                        .with(cors())

                        // !!! error handling

                        .with(bearer((token, request) -> new Notary(JWTKey).verify(token)
                                .map(Notary::decode)
                                .map(JsonValue::asJsonObject)
                                .map(Users::decode)
                                .map(request::user)
                        ))

                        .wrap(router()

                                .path(SAML.Pattern, new SAML())

                                .path("/*", asset(new Fallback(), router()

                                        .path("/", card())

                                ))


                        )
                )

        ).start();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static Router card() {
        return router()
                .get(request -> request.user(User.class)

                        .map(user -> request.reply(OK)

                                .body(json(), encode(new Root()

                                        .setProfile(new Profile()
                                                .setUser(user)
                                                .setCard(cards(user.getEsi()).findFirst().orElse(null)) // !!! multiple
                                        )

                                ))

                        )

                        .orElseGet(() -> request.reply(OK)

                                .header("WWW-Authenticate", format("Bearer realm=\"%s\"", request.base()))
                                .header("Location", SAML.Session)

                                .body(json(), encode(new Root()))

                        ));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private Profile profile;


    private static JsonObject encode(final Root root) {
        return Json.createObjectBuilder()

                .add("profile", encode(root.profile))

                .build();
    }

    private static JsonValue encode(final Profile profile) {
        return profile == null ? NULL : Json.createObjectBuilder()

                .add("user", Users.encode(profile.user))
                .add("card", Cards.encode(profile.card))

                .build();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    public static final class Profile {

        private User user;
        private Card card;

    }

    @Getter
    @Setter
    public abstract static class Container<T extends Resource> extends Resource {

        @NotNull
        @Size(max=ContainerSize)
        private List<T> contains;

    }

    @Getter
    @Setter
    public abstract static class Resource {

        @Size(max=URLSize)
        @Pattern(regexp=RelativePattern)
        private String id;

        @Size(max=LineSize)
        @Pattern(regexp=LinePattern)
        private String label;

        @Size(max=TextSize)
        @Pattern(regexp=TextPattern)
        private String brief;

        @Size(max=4096)
        @Pattern(regexp=AbsolutePattern)
        private String image;

    }

    public abstract static class ResourceData {

        protected abstract Optional<String> getPath();

        protected abstract Optional<String> getLabel();


        protected Long id;

        protected String image;

        protected String brief;


        protected void transfer(final Resource resource, final ResourceData data) {

            data.getPath().ifPresent(resource::setId);
            data.getLabel().ifPresent(resource::setLabel);

            resource.setImage(data.image);
            resource.setBrief(data.brief);

        }

        protected static void transfer(final ResourceData data, final Resource resource) {

            if ( !data.getPath().equals(Optional.ofNullable(resource.getId())) ) {
                throw new IllegalStateException("mutated value for read-only field <id>");
            }

            if ( !data.getLabel().equals(Optional.ofNullable(resource.getLabel())) ) {
                throw new IllegalStateException("mutated value for read-only field <brief>");
            }

            data.image=resource.getImage();
            data.brief=resource.getBrief();

        }

    }

}
