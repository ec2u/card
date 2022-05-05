package eu.ec2u.card.tokens;

import com.fasterxml.jackson.annotation.JsonView;
import eu.ec2u.card.Tool.Container;
import eu.ec2u.card.Tool.Resource;
import eu.ec2u.card.ToolSecurity.Profile;
import eu.ec2u.card.cards.Cards;
import eu.ec2u.card.events.EventsService;
import eu.ec2u.card.tokens.Tokens.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import static eu.ec2u.card.ToolConfiguration.ContainerSize;
import static eu.ec2u.card.events.Events.Action.*;
import static org.springframework.http.ResponseEntity.*;
import java.util.Optional;

@RestController
@RequestMapping("")
public final class TokensController {

	@Autowired private TokensService tokens;

	@Autowired private EventsService events;

	@RequestMapping(value = "/tokens", method = RequestMethod.GET)
	@JsonView(Container.class)
	ResponseEntity<Tokens> get(

			@RequestParam Optional<String> username,
			@RequestParam Optional<Long> tokenNumber,
			@Valid @RequestParam(required=false, defaultValue="0") @Min(0) final int page,
			@Valid @RequestParam(required=false, defaultValue="25") @Min(1) @Max(ContainerSize) final int size,
			@RequestParam Optional<String> sortingOrder,
			@RequestParam Optional<String> sortingProperty

	) {

		return ok().body(tokens.browse(
				username,
				tokenNumber,
				PageRequest.of(page, size),
				sortingOrder,
				sortingProperty
		));

	}

	@RequestMapping(value = "/tokens/", method = RequestMethod.POST)
	ResponseEntity<Void> post(

			@AuthenticationPrincipal final Profile profile,
			@Valid @RequestBody final Token token

	) {

		return created(events.trace(profile, create, tokens.create(token))).build();

	}

	@RequestMapping(value = "/tokens/{tokenNumber}", method = RequestMethod.GET)
	@JsonView(Resource.class)
	ResponseEntity<Token> get(

			@AuthenticationPrincipal final Profile profile,
			@PathVariable final long tokenNumber

	) {

		return ok().body(tokens.relate(tokenNumber));

	}

	@RequestMapping(value = "/tokens/{tokenNumber}", method = RequestMethod.PUT)
	ResponseEntity<Void> put(

			@AuthenticationPrincipal final Profile profile,
			@PathVariable final long tokenNumber,
			@Valid @RequestBody final Token token

	) {

		return noContent().location(events.trace(profile, update, tokens.update(tokenNumber, token))).build();

	}

	@RequestMapping(value = "/tokens/{tokenNumber}", method = RequestMethod.DELETE)
	ResponseEntity<Void> delete(

			@AuthenticationPrincipal final Profile profile,
			@PathVariable final long tokenNumber

	) {

		return noContent().location(events.trace(profile, delete, tokens.delete(tokenNumber))).build();

	}

}
