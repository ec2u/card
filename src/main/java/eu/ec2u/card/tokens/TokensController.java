package eu.ec2u.card.tokens;

import com.fasterxml.jackson.annotation.JsonView;
import eu.ec2u.card.Tool.Container;
import eu.ec2u.card.Tool.Resource;
import eu.ec2u.card.ToolSecurity.Profile;
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
import java.util.regex.Pattern;

@RestController
@RequestMapping(Tokens.Id)
public final class TokensController {

	@Autowired private TokensService tokens;
	@Autowired private EventsService events;


	@GetMapping("")
	@JsonView(Container.class)
	ResponseEntity<Tokens> get(

//			@AuthenticationPrincipal final Saml2AuthenticatedPrincipal principal,
			@RequestParam(value="usernamePrefix") Optional<String> usernamePrefix,
			@RequestParam(value="tokenNumber") Optional<Long> tokenNumber,
			@Valid @RequestParam(required=false, defaultValue="0") @Min(0) final int page,
			@Valid @RequestParam(required=false, defaultValue="25") @Min(1) @Max(ContainerSize) final int size

	) {

		return ok().body(tokens.search(usernamePrefix, tokenNumber, PageRequest.of(page, size, Sort.by("serviceOrUserNameLowerCase"))));

	}

	@GetMapping("/filters")
	@JsonView(Container.class)
	ResponseEntity<Tokens> search(

			@RequestParam(value= "usernamePrefix") Optional<String> usernamePrefix,
			@RequestParam(value="tokenNumber") Optional<Long> tokenNumber,
			@Valid @RequestParam(required=false, defaultValue="0") @Min(0) final int page,
			@Valid @RequestParam(required=false, defaultValue="25") @Min(1) @Max(ContainerSize) final int size

	) {

		return ok().body(tokens.search(usernamePrefix, tokenNumber, PageRequest.of(page, size, Sort.by("serviceOrUserNameLowerCase"))));

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

	private boolean isUsernamePrefixValid(String usernamePrefix) {

		return Pattern.compile("^[a-zA-Z]+$").matcher(usernamePrefix).matches();

	}

	private boolean isTokenNumberValid(String tokenNumber) {

		return Pattern.compile("^[0-9]+$").matcher(tokenNumber).matches();

	}

}
