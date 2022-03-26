/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package work;

import com.metreeca.rest.*;
import com.metreeca.rest.services.Fetcher;
import com.metreeca.rest.services.Fetcher.URLFetcher;

import static com.metreeca.rest.Handler.handler;
import static com.metreeca.rest.Response.BadGateway;
import static com.metreeca.rest.formats.DataFormat.data;
import static com.metreeca.rest.formats.TextFormat.text;

/**
 * SPA launcher.
 */
public final class Launcher implements Wrapper {

    private final String path;

    private final Fetcher fetcher=new URLFetcher(); // !!! factor to static method


    public Launcher(final String path) { // !!! well-formedness

        if ( path == null ) {
            throw new NullPointerException("null path");
        }

        this.path=path;
    }


    @Override public Handler wrap(final Handler handler) { // !!! caching (disabled on localhost)
        return handler(Request::route,

                request -> new Request()

                        .method(request.method())
                        .base(request.base())
                        .path(path)

                        .headers(request.headers())

                        // disable conditional requests

                        .header("If-None-Match", "")
                        .header("If-Modified-Since", "")

                        .map(fetcher)

                        // convert incoming into outgoing response

                        .map(response -> response.body(data()).fold(

                                error -> request.reply(BadGateway)

                                        .body(text(), error.getMessage()),

                                value -> request.reply(response.status())

                                        .header("Content-Type", response.header("Content-Type").orElse(""))
                                        .header("Cache-Control", "no-store") // don't shadow future API responses

                                        .body(data(), response.body(data()).fold(error -> { throw error; }))

                        )),

                handler

        );
    }

}
