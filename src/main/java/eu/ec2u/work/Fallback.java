/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.work;

import com.metreeca.rest.*;
import com.metreeca.rest.services.Fetcher;
import com.metreeca.rest.services.Fetcher.URLFetcher;

import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import static com.metreeca.rest.Response.BadGateway;
import static com.metreeca.rest.formats.DataFormat.data;
import static com.metreeca.rest.formats.TextFormat.text;

import static java.util.stream.Collectors.toMap;

public final class Fallback implements Handler {

    private final Fetcher fetcher=new URLFetcher(); // !!! factor to static method


    @Override public Response handle(final Request request) { // !!! caching (disabled on localhost)
        return new Request()

                .method(request.method())
                .base(request.base())
                .path("/index.html")

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

                                .headers(response.headers().entrySet().stream()
                                        .filter(entry -> Set.of("cache-control", "etag", "expires", "date", "content"
                                                + "-type").contains(entry.getKey().toLowerCase(Locale.ROOT)))
                                        .collect(toMap(Entry::getKey, Entry::getValue))
                                )

                                .body(data(), response.body(data()).fold(error -> { throw error; }))

                ));
    }
}
