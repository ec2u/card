/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card;


import com.metreeca.gcp.GCPServer;
import com.metreeca.gcp.services.GCPVault;

import lombok.*;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

import javax.validation.constraints.*;

import static com.metreeca.rest.Handler.asset;
import static com.metreeca.rest.MessageException.status;
import static com.metreeca.rest.Response.OK;
import static com.metreeca.rest.Wrapper.preprocessor;
import static com.metreeca.rest.formats.JSONLDFormat.keywords;
import static com.metreeca.rest.handlers.Publisher.publisher;
import static com.metreeca.rest.handlers.Router.router;
import static com.metreeca.rest.services.Logger.Level.debug;
import static com.metreeca.rest.services.Vault.vault;
import static com.metreeca.rest.wrappers.CORS.cors;
import static com.metreeca.rest.wrappers.Server.server;

import static java.time.temporal.ChronoField.*;
import static java.util.Map.entry;

@Getter
@Builder
public final class Card {

    public static final String Id="/";

    public static final int ContainerSize=100;

    public static final int URLSize=100;
    public static final String RelativePattern="/(\\w+/)*\\w+/?";
    public static final String AbsolutePattern="\\w+:\\S+";

    public static final int LineSize=100;
    public static final String LinePattern="\\S+( \\S+)*";

    public static final int TextSize=2000;
    public static final String TextPattern="\\S+(\\s+\\S+)*";

    static final DateTimeFormatter InstantFormat=new DateTimeFormatterBuilder()

            .appendValue(YEAR, 4)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)

            .appendLiteral("T")

            .appendValue(HOUR_OF_DAY, 2)
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendValue(SECOND_OF_MINUTE, 2)

            .parseStrict()
            .toFormatter(Locale.ROOT);

    static {
        debug.log("com.metreeca");
    }


    public static void main(final String... args) {
        new GCPServer().delegate(toolbox -> toolbox

                .set(vault(), GCPVault::new)

                .set(keywords(), () -> Map.ofEntries(
                        entry("@id", "id"),
                        entry("@type", "type")
                ))

                .get(() -> server()

                        .with(cors())
                        //.with(bearer(token(), RootRole))

                        .with(preprocessor(request -> // disable language negotiation
                                request.header("Accept-Language", "")
                        ))

                        .wrap(router()


                                .path("/*", asset(

                                        publisher("/static")

                                                .fallback("/index.html"),

                                        router()

                                                .path("/", request -> request.reply(status(OK)))

                                ))

                        )
                )

        ).start();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    Instant revision() {

        throw new UnsupportedOperationException(";(  be implemented"); // !!!

        //return LocalDateTime.from(Card.InstantFormat.parse(revision)).toInstant(ZoneOffset.UTC);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final String id;

    private final Instant revision;
    //private final Profile profile;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        //@JsonProperty("id")
        private String path;

        @Size(max=LineSize)
        @Pattern(regexp=LinePattern)
        private String label;

        @Size(max=LineSize)
        @Pattern(regexp=AbsolutePattern)
        private String image;

        @Size(max=TextSize)
        @Pattern(regexp=TextPattern)
        private String brief;

    }

    public abstract static class ResourceData {

        protected abstract Optional<String> getPath();

        protected abstract Optional<String> getLabel();


        protected Long id;

        protected String image;

        protected String brief;


        protected void transfer(final Resource resource, final ResourceData data) {

            data.getPath().ifPresent(resource::setPath);
            data.getLabel().ifPresent(resource::setLabel);

            resource.setImage(data.image);
            resource.setBrief(data.brief);

        }

        protected static void transfer(final ResourceData data, final Resource resource) {

            if ( !data.getPath().equals(Optional.ofNullable(resource.getPath())) ) {
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
