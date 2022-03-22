/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package work;

import java.util.Map;

import static com.metreeca.core.Identifiers.encode;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public final class Query {

    public static String query(final Map<String, String> parameters) { // !!! factor
        return parameters.entrySet().stream()
                .map(p -> format("%s=%s", encode(p.getKey()), encode(p.getValue())))
                .collect(joining("&"));
    }

    public static String query(final String prefix, final Map<String, String> parameters) {
        return prefix+(prefix.contains("?") ? "&" : "?")+query(parameters);
    }

}
