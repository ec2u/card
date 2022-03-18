/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card.work;

import com.metreeca.rest.*;
import com.metreeca.rest.services.Fetcher;

import static com.metreeca.rest.formats.DataFormat.data;

public final class Fallback implements Handler {

    private final Fetcher.URLFetcher fetcher=new Fetcher.URLFetcher();

    @Override public Response handle(final Request request) {
        return request

                .reply(response -> fetcher.apply(new Request()

                        .method(request.method())
                        .base(request.base())
                        .path("/index.html")

                        .headers(request.headers())

                        // disable conditional requests

                        .header("If-None-Match", "")
                        .header("If-Modified-Since", "")

                ))

                .map(response -> response.body(data()).fold(
                        error -> { throw error; },
                        value -> response.body(data(), value)
                ));
    }
}
