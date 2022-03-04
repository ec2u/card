/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import org.springframework.web.servlet.HandlerInterceptor;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.http.MediaType.parseMediaTypes;

import static java.lang.String.format;
import static java.util.Collections.list;

/**
 * SPA fallback loader
 */
final class ToolLoader implements HandlerInterceptor {

    private static final Pattern ExtensionPattern=Pattern.compile("\\.(\\w+)$");

    private static final Pattern HostPattern=Pattern.compile("\\bhost\\s*=\\s*(?<host>.*)\\s*([,;]|$)");
    private static final Pattern ProtoPattern=Pattern.compile("\\bproto\\s*=\\s*(?<proto>.*)\\s*([,;]|$)");


    @Override public boolean preHandle(

            final HttpServletRequest request, final HttpServletResponse response, final Object handler

    ) throws IOException {

        final boolean safe=Set.of("GET", "HEAD").contains(request.getMethod());
        final boolean interactive=parseMediaTypes(request.getHeader("Accept")).contains(TEXT_HTML);
        final boolean route=!ExtensionPattern.matcher(request.getRequestURI()).find();

        if ( safe && interactive && route ) {

            response(response, request(request));

            return false;

        } else {

            return true;

        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private HttpURLConnection request(final HttpServletRequest request) throws IOException {

        final String proto=Optional.ofNullable(request.getHeader("Forwarded"))

                .map(ProtoPattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> matcher.group("proto"))

                .or(() -> Optional.ofNullable(request.getHeader("X-Forwarded-Proto")))

                .orElseGet(request::getScheme);

        final String host=Optional.ofNullable(request.getHeader("Forwarded"))

                .map(HostPattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> matcher.group("host"))

                .or(() -> Optional.ofNullable(request.getHeader("X-Forwarded-Host")))

                .or(() -> Optional.ofNullable(request.getHeader("Host")))

                .orElseThrow(() ->
                        new IllegalArgumentException("undefined http <host> header") // unexpected
                );


        final URL url=new URL(format("%s://%s%s", proto, host, "/index.html"));

        final HttpURLConnection connection=(HttpURLConnection)url.openConnection();

        connection.setDoInput(true);
        connection.setDoOutput(false);

        connection.setRequestMethod(request.getMethod());

        list(request.getHeaderNames()).forEach(name -> list(request.getHeaders(name)).forEach(value ->
                connection.addRequestProperty(name, value)
        ));

        return connection;
    }

    private void response(final HttpServletResponse response, final HttpURLConnection connection) throws IOException {

        connection.connect();

        response.setStatus(connection.getResponseCode());

        connection.getHeaderFields().forEach((name, values) -> values.forEach(value ->
                response.addHeader(name, value)
        ));

        try (
                final InputStream input=connection.getInputStream();
                final OutputStream output=response.getOutputStream();
        ) {

            input.transferTo(output);

        }
    }

}
