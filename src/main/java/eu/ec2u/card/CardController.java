
/*
 * Copyright © 2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import com.fasterxml.jackson.annotation.JsonView;
import eu.ec2u.card.Card.Container;
import eu.ec2u.card.CardSecurity.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import javax.servlet.http.*;

import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(Card.Id)
public final class CardController implements ErrorController {

    @Autowired private CardService session;


    @GetMapping("")
    @JsonView(Container.class)
    ResponseEntity<Object> get(

            @AuthenticationPrincipal final Saml2AuthenticatedPrincipal principal

    ) {

        return ok(session.retrieve(principal == null ? null : Profile.builder()
                .code(principal.getFirstAttribute("schacPersonalUniqueCode"))
                .email(principal.getName())
                .build()
        ));

    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletRequest request) {

        SecurityContextHolder.clearContext();
        Optional.ofNullable(request.getSession(false)).ifPresent(HttpSession::invalidate);

        return status(FOUND).location(URI.create("/")).build();
    }

    @RequestMapping("/error")
    public ResponseEntity<Void> error(final HttpServletResponse response) {
        return status(response.getStatus()).build();
    }

}