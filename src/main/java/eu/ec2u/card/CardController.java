
/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import com.fasterxml.jackson.annotation.JsonView;
import eu.ec2u.card.Card.Container;
import eu.ec2u.card.CardSecurity.Credentials;
import eu.ec2u.card.CardSecurity.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(Card.Path)
public final class CardController implements ErrorController {

    @Autowired private CardService session;


    @GetMapping("")
    @JsonView(Container.class)
    ResponseEntity<Card> get(

            @AuthenticationPrincipal final Profile profile

    ) {

        return ok(session.retrieve(profile));

    }

    @PostMapping("")
    ResponseEntity<Object> post(

            @Valid
            @RequestBody(required=false) final Credentials credentials

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