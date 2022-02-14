
/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card.users;

import com.fasterxml.jackson.annotation.JsonView;
import eu.ec2u.card.Card.Container;
import eu.ec2u.card.CardSecurity.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(Users.Path)
public final class UsersController {

    @Autowired private UsersService users;


    @GetMapping("")
    @JsonView(Container.class)
    ResponseEntity<Object> get(

            @AuthenticationPrincipal final Profile profile,

            @Valid
            @Min(0)
            @RequestParam(required=false, defaultValue="0") final int page,

            @Valid
            @Min(1)
            @Max(100)
            @RequestParam(required=false, defaultValue="25") final int size

    ) {


        //if ( !profile.isReader(Users.Path) ) {
        //
        //    return status(FORBIDDEN).build();
        //
        //} else {

        return ok().body(users.browse(PageRequest.of(page, size, Sort.by("label"))));

        //}

    }

}