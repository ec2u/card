
/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card.users;

import com.fasterxml.jackson.annotation.JsonView;
import eu.ec2u.card.Tool.Container;
import eu.ec2u.card.Tool.Resource;
import eu.ec2u.card.ToolSecurity.Profile;
import eu.ec2u.card.events.EventsService;
import eu.ec2u.card.users.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static eu.ec2u.card.ToolConfiguration.ContainerSize;
import static eu.ec2u.card.events.Events.Action.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(Users.Id)
public final class UsersController {

    @Autowired private UsersService users;
    @Autowired private EventsService events;


    @GetMapping("")
    @JsonView(Container.class)
    ResponseEntity<Object> get(

            @AuthenticationPrincipal final Profile profile,
            @Valid @RequestParam(required=false, defaultValue="0") @Min(0) final int page,
            @Valid @RequestParam(required=false, defaultValue="25") @Min(1) @Max(ContainerSize) final int size

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
    ResponseEntity<Object> post(

            @AuthenticationPrincipal final Profile profile,
            @Validated(Resource.class) @RequestBody final User state

    ) {

        // restrict admin creation to admins

        //if ( !profile.isEditor(Users.ID) || user.isAdmin() && !profile.isAdmin() ) {
        //    throw new HttpException(FORBIDDEN);
        //}

        return users.create(state)
                .map(user -> events.trace(profile, create, user))
                .map(user -> created(URI.create(user.getId())).build())
                .orElseGet(() -> status(CONFLICT).build());

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("{id}")
    @JsonView(Resource.class)
    ResponseEntity<User> get(

            @AuthenticationPrincipal final Profile profile,
            @PathVariable final long id

    ) {

        //if ( !profile.isReader(Users.ID+id) ) {
        //    throw new HttpException(FORBIDDEN);
        //}

        return users.relate(id)
                .map(user -> ok().body(user))
                .orElseGet(() -> status(events.gone(Users.Id+id) ? GONE : NOT_FOUND).build());

    }

    @PatchMapping("{id}")
    ResponseEntity<Object> patch(

            @AuthenticationPrincipal final Profile profile,
            @PathVariable final long id,
            @Validated @RequestBody final User patch

    ) {

        // restrict admin granting to admins

        //if ( !profile.isEditor(Users.ID+id) || user.isAdmin() && !profile.isAdmin() ) {
        //    throw new HttpException(FORBIDDEN);
        //}

        return users.update(id, patch)
                .map(user -> events.trace(profile, update, user))
                .map(user -> noContent().build())
                .orElseGet(() -> status(events.gone(Users.Id+id) ? GONE : NOT_FOUND).build());

    }

    @DeleteMapping("{id}")
    ResponseEntity<Object> delete(

            @AuthenticationPrincipal final Profile profile,
            @PathVariable final long id

    ) {

        //if ( !profile.isEditor(Users.ID+id) ) {
        //    throw new HttpException(FORBIDDEN);
        //}

        return users.delete(id)
                .map(user -> events.trace(profile, delete, user))
                .map(user -> noContent().build())
                .orElseGet(() -> status(events.gone(Users.Id+id) ? GONE : NOT_FOUND).build());

    }

}