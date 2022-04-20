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
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static eu.ec2u.card.ToolConfiguration.ContainerSize;
import static eu.ec2u.card.events.Events.Action.*;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(Tokens.Id)
public final class TokensController {

	@Autowired private TokensService tokens;
	@Autowired private EventsService events;


	@GetMapping("")
	@JsonView(Container.class)
	ResponseEntity<Tokens> get(

			@AuthenticationPrincipal final Saml2AuthenticatedPrincipal principal,
			@Valid @RequestParam(required=false, defaultValue="0") @Min(0) final int page,
			@Valid @RequestParam(required=false, defaultValue="25") @Min(1) @Max(ContainerSize) final int size

	) {

		return ok().body(tokens.browse(PageRequest.of(page, size, Sort.by("serviceOrUserName"))));

	}

	@PostMapping("")
	ResponseEntity<Void> post(

			@AuthenticationPrincipal final Profile profile,
			@Valid @RequestBody final Token token

	) {

		return created(events.trace(profile, create, tokens.create(token))).build();

	}

	@GetMapping("{tokenNumber}")
	@JsonView(Resource.class)
	ResponseEntity<Token> get(

			@AuthenticationPrincipal final Profile profile,
			@PathVariable final long tokenNumber

	) {

		return ok().body(tokens.relate(tokenNumber));

	}

	@GetMapping("/filters")
	ResponseEntity<Tokens> searchBySurnamePrefix(

			@RequestParam(value="userNamePrefix") String userNamePrefix
	) {

		return ok().body(tokens.searchByUserNamePrefixAlternative(userNamePrefix));

	}

	@PutMapping("{tokenNumber}")
	ResponseEntity<Void> put(

			@AuthenticationPrincipal final Profile profile,
			@PathVariable final long tokenNumber,
			@Valid @RequestBody final Token token

	) {

		return noContent().location(events.trace(profile, update, tokens.update(tokenNumber, token))).build();

	}

	@DeleteMapping("{tokenNumber}")
	ResponseEntity<Void> delete(

			@AuthenticationPrincipal final Profile profile,
			@PathVariable final long tokenNumber

	) {

		return noContent().location(events.trace(profile, delete, tokens.delete(tokenNumber))).build();

	}

}
