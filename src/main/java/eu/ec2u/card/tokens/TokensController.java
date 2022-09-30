package eu.ec2u.card.tokens;

import com.fasterxml.jackson.annotation.JsonView;
import eu.ec2u.card.Tool.Container;
import eu.ec2u.card.Tool.Resource;
import eu.ec2u.card.ToolApplication;
import static eu.ec2u.card.ToolConfiguration.ContainerSize;
import eu.ec2u.card.ToolSecurity.Profile;
import static eu.ec2u.card.events.Events.Action.*;
import eu.ec2u.card.events.EventsService;
import eu.ec2u.card.tokens.Tokens.Token;
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
public final class TokensController {

	@Autowired
	private TokensService tokens;

	@Autowired
	private EventsService events;

	@RequestMapping(value = "/tokens", method = RequestMethod.GET)
	@JsonView(Container.class)
	ResponseEntity<Tokens> get(

			@RequestParam Optional<String> username,
			@RequestParam Optional<String> tokenNumber,
			@Valid @RequestParam(required = false, defaultValue = "0") @Min(0) int page,
			@Valid @RequestParam(required = false, defaultValue = "25") @Min(1) @Max(ContainerSize) int size,
			@RequestParam Optional<String> sortingOrder,
			@RequestParam Optional<String> sortingProperty

	) {

		Map<String, Optional<?>> paramHashMap = Map.of(
				"username", username,
				"tokenNumber", tokenNumber
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
					"sortingProperty is not valid, must be username or tokenNumber!"
			);

		}

		// Avoiding querying and sorting in the same time
		if (!notNullParamHashMap.isEmpty() && sortingOrder.isPresent()) {

			throw new ToolApplication.WrongQueryArgumentsException(
					"Sorting and querying are not allowed in the same get request!"
			);

		}

		return ok().body(tokens.browse(
				notNullParamHashMap,
				PageRequest.of(page, size),
				sortingOrder,
				sortingProperty
		));

	}

	@RequestMapping(value = "/tokens/", method = RequestMethod.POST)
	ResponseEntity<Void> post(

			@AuthenticationPrincipal Profile profile,
			@Valid @RequestBody Token token

	) {

		return created(events.trace(profile, create, tokens.create(token))).build();

	}

	@RequestMapping(value = "/tokens/{tokenNumber}", method = RequestMethod.GET)
	@JsonView(Resource.class)
	ResponseEntity<Token> get(

			@AuthenticationPrincipal Profile profile,
			@PathVariable long tokenNumber

	) {

		return ok().body(tokens.relate(tokenNumber));

	}

	@RequestMapping(value = "/tokens/{tokenNumber}", method = RequestMethod.PUT)
	ResponseEntity<Void> put(

			@AuthenticationPrincipal Profile profile,
			@PathVariable long tokenNumber,
			@Valid @RequestBody Token token

	) {

		return noContent().location(events.trace(profile, update, tokens.update(tokenNumber, token))).build();

	}

	@RequestMapping(value = "/tokens/{tokenNumber}", method = RequestMethod.DELETE)
	ResponseEntity<Void> delete(

			@AuthenticationPrincipal Profile profile,
			@PathVariable long tokenNumber

	) {

		return noContent().location(events.trace(profile, delete, tokens.delete(tokenNumber))).build();

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

			return sortingProperty.get().trim().equalsIgnoreCase("username")
					|| sortingProperty.get().trim().equalsIgnoreCase("tokenNumber");

		}

	}

}
