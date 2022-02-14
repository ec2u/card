
/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card.users;

import com.fasterxml.jackson.annotation.JsonView;
import eu.ec2u.card.Tool.Container;
import eu.ec2u.card.ToolSecurity.Profile;
import eu.ec2u.card.users.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static eu.ec2u.card.ToolConfiguration.ContainerSize;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(Users.Id)
public final class UsersController {

    @Autowired private UsersService users;


    @GetMapping("")
    @JsonView(Container.class)
    ResponseEntity<Object> get(

            @AuthenticationPrincipal
            final Profile profile,

            @Valid
            @Min(0)
            @RequestParam(required=false, defaultValue="0")
            final int page,

            @Valid
            @Min(1)
            @Max(ContainerSize)
            @RequestParam(required=false, defaultValue="25")
            final int size

    ) {


        //if ( !profile.isReader(Users.Path) ) {
        //
        //    return status(FORBIDDEN).build();
        //
        //} else {

        return ok().body(users.browse(PageRequest.of(page, size, Sort.by("surname"))));

        //}

    }


    @PostMapping("")
    ResponseEntity<Void> post(

            @AuthenticationPrincipal
            final Profile profile,

            @Valid
            @RequestBody
            final User user

    ) {

        // restrict admin creation to admins

        //if ( !profile.isEditor(Users.ID) || user.isAdmin() && !profile.isAdmin() ) {
        //    throw new HttpException(FORBIDDEN);
        //}

        return created(URI.create(users.create(profile, user))).build();

    }


}