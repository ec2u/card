
/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import com.fasterxml.jackson.annotation.JsonView;
import eu.ec2u.card.Tool.Container;
import eu.ec2u.card.ToolSecurity.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(Tool.Id)
public final class ToolController implements ErrorController {

    @Autowired private ToolService session;


    @GetMapping("")
    @JsonView(Container.class)
    ResponseEntity<Object> get(

            @AuthenticationPrincipal final Saml2AuthenticatedPrincipal principal

    ) {


        return ok().body(principal == null ? "ciao!" : "ciao"+principal.getName()+"!");

        //return ok(session.retrieve(profile));

    }

    @PostMapping("")
    ResponseEntity<Object> post(

            @Valid @RequestBody(required=false) final Credentials credentials

    ) {

        if ( credentials != null ) { // login

            return session.lookup(credentials)

                    .map(profile -> session.login(profile))

                    .map(cookie -> noContent()
                            .header("Set-Cookie", cookie)
                            .build()
                    )

                    .orElseGet(() -> status(FORBIDDEN)
                            .build()
                    );

        } else { // logout

            return noContent()
                    .header("Set-Cookie", session.logout())
                    .build();

        }

    }


    @RequestMapping("/error")
    public ResponseEntity<Void> error(final HttpServletResponse response) {
        return status(response.getStatus()).build();
    }

}