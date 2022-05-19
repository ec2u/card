
/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card.users;

import com.fasterxml.jackson.annotation.JsonView;
import eu.ec2u.card.Tool.Container;
import eu.ec2u.card.Tool.Resource;
import eu.ec2u.card.ToolApplication;
import static eu.ec2u.card.ToolConfiguration.ContainerSize;
import eu.ec2u.card.ToolSecurity.Profile;
import static eu.ec2u.card.events.Events.Action.*;
import eu.ec2u.card.events.EventsService;
import eu.ec2u.card.users.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("")
public final class UsersController {

	@Autowired
	private UsersService users;

	@Autowired
	private EventsService events;

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@JsonView(Container.class)
	ResponseEntity<Users> get(

			@RequestParam Optional<String> forename,
			@RequestParam Optional<String> surname,
			@RequestParam Optional<String> email,
			@RequestParam Optional<Boolean> isAdmin,
			@Valid @RequestParam(required = false, defaultValue = "0") @Min(0) int page,
			@Valid @RequestParam(required = false, defaultValue = "25") @Min(1) @Max(ContainerSize) int size,
			@RequestParam Optional<String> sortingOrder,
			@RequestParam Optional<String> sortingProperty

	) {

		Map<String, Optional<?>> paramHashMap = Map.of(
				"forenameLowerCase", forename,
				"surnameLowerCase", surname,
				"email", email,
				"isAdmin", isAdmin
		);

		Map<String, Object> notNullParamHashMap = new HashMap<>();
		paramHashMap.forEach((s, o) -> o.ifPresent(value -> notNullParamHashMap.put(s, value)));

		// Avoiding sortingOrder without sortingProperty or sortingProperty without sortingOrder
		if ((sortingOrder.isPresent() && sortingProperty.isEmpty()) ||
				(sortingOrder.isEmpty() && sortingProperty.isPresent())) {

			throw new ToolApplication.WrongQueryArgumentsException(
					"In order to sort you must insert sortingOrder and sortingProperty both!"
			);

		}

		// Checking sortingOrder
		if (!isSortingOrderValid(sortingOrder)) {

			throw new ToolApplication.WrongQueryArgumentsException(
					"sortingOrder is not valid, must be asc or desc!"
			);

		}

		// Checking sortingProperty
		if (!isSortingPropertyValid(sortingProperty)) {

			throw new ToolApplication.WrongQueryArgumentsException(
					"sortingProperty is not valid, must be forenameLowerCase, surnameLowerCase or email!"
			);

		}

		// Avoiding querying and sorting in the same time
		if (!notNullParamHashMap.isEmpty() && sortingOrder.isPresent()) {

			throw new ToolApplication.WrongQueryArgumentsException(
					"Sorting and querying are not allowed in the same get request!"
			);

		}

		return ok().body(users.browse(
				notNullParamHashMap,
				PageRequest.of(page, size),
				sortingOrder,
				sortingProperty
		));

	}

	@RequestMapping(value = "/users/", method = RequestMethod.POST)
	ResponseEntity<Void> post(

			@AuthenticationPrincipal Profile profile,
			@Valid @RequestBody User user

	) {

		// restrict admin creation to admins

		//if ( !profile.isEditor(Users.ID) || user.isAdmin() && !profile.isAdmin() ) {
		//    throw new HttpException(FORBIDDEN);
		//}

		return created(events.trace(profile, create, users.create(user))).build();

	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	@JsonView(Resource.class)
	ResponseEntity<User> get(

			@AuthenticationPrincipal Profile profile,
			@PathVariable long id

	) {

		//if ( !profile.isReader(Users.ID+id) ) {
		//    throw new HttpException(FORBIDDEN);
		//}

		return ok().body(users.relate(id));

	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
	ResponseEntity<Void> put(

			@AuthenticationPrincipal Profile profile,
			@PathVariable long id,
			@Valid @RequestBody User user

	) {

		// restrict admin granting to admins

		//if ( !profile.isEditor(Users.ID+id) || user.isAdmin() && !profile.isAdmin() ) {
		//    throw new HttpException(FORBIDDEN);
		//}

		return noContent().location(events.trace(profile, update, users.update(id, user))).build();

	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	ResponseEntity<Void> delete(

			@AuthenticationPrincipal Profile profile,
			@PathVariable long id

	) {

		//if ( !profile.isEditor(Users.ID+id) ) {
		//    throw new HttpException(FORBIDDEN);
		//}

		return noContent().location(events.trace(profile, delete, users.delete(id))).build();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private boolean isSortingOrderValid(Optional<String> sortingOrder) {

		if (sortingOrder.isEmpty()) {

			return true;

		} else {

			return sortingOrder.get().trim().equalsIgnoreCase("asc")
					|| sortingOrder.get().trim().equalsIgnoreCase("desc");

		}

	}

	private boolean isSortingPropertyValid(Optional<String> sortingProperty) {

		if (sortingProperty.isEmpty()) {

			return true;

		} else {

			return sortingProperty.get().trim().equalsIgnoreCase("forenameLowerCase")
					|| sortingProperty.get().trim().equalsIgnoreCase("surnameLowerCase")
					|| sortingProperty.get().trim().equalsIgnoreCase("email");

		}

	}

}