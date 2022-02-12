/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.*;

/**
 * SPA fallback loader
 */
final class CardLoader implements HandlerInterceptor {

    private static final Pattern ExtensionPattern=Pattern.compile("\\.(\\w+)$");


    @Override public boolean preHandle(

            final HttpServletRequest request, final HttpServletResponse response, final Object handler

    ) throws ServletException, IOException {

        final boolean interactive=parseMediaTypes(request.getHeader("Accept")).contains(TEXT_HTML);
        final boolean route=!ExtensionPattern.matcher(request.getRequestURI()).find();
        final boolean original=request.getAttribute("javax.servlet.include.request_uri") == null;

        if ( interactive && route && original ) {

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(TEXT_HTML_VALUE);
            response.setCharacterEncoding("UTF8");

            request.getRequestDispatcher("/index.html").include(request, response);

            return false;

        } else {

            return true;

        }

    }

}
