package eu.ec2u.card.cards;

import com.fasterxml.jackson.annotation.JsonView;
import eu.ec2u.card.Tool.Container;
import eu.ec2u.card.Tool.Resource;
import eu.ec2u.card.ToolSecurity;
import eu.ec2u.card.ToolSecurity.Profile;
import eu.ec2u.card.cards.Cards.Card;
import eu.ec2u.card.events.EventsService;
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
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("")
public final class CardsController {

	@Autowired private CardsService cards;

	@Autowired private EventsService events;

	@RequestMapping(value = "/cards", method = RequestMethod.GET)
	@JsonView(Container.class)
	ResponseEntity<Cards> get(

			@RequestParam Optional<String> forename,
			@RequestParam Optional<String> surname,
			@RequestParam Optional<String> expiringDate,
			@RequestParam Optional<Long> virtualCardNumber,
			@Valid @RequestParam(required=false, defaultValue="0") @Min(0) final int page,
			@Valid @RequestParam(required=false, defaultValue="25") @Min(1) @Max(ContainerSize) final int size,
			@RequestParam Optional<String> sortingOrder,
			@RequestParam Optional<String> sortingProperty

	) {

		return ok().body(cards.browse(
				forename,
				surname,
				expiringDate,
				virtualCardNumber,
				PageRequest.of(page, size, Sort.by("holderSurname")),
				sortingOrder,
				sortingProperty
		));

	}

	@RequestMapping(value = "/cards/", method = RequestMethod.POST)
	ResponseEntity<Void> post(

			@AuthenticationPrincipal final ToolSecurity.Profile profile,
			@Valid @RequestBody final Card card

	) {
		return noContent().location(events.trace(profile, create, cards.create(card))).build();
	}

	@RequestMapping(value = "/cards/{id}", method = RequestMethod.GET)
	@JsonView(Resource.class)
	ResponseEntity<Card> get(

			@AuthenticationPrincipal final ToolSecurity.Profile profile,
			@PathVariable final long id

	) {
		return ok().body(cards.relate(id));
	}

	@RequestMapping(value = "/cards/{id}", method = RequestMethod.PUT)
	ResponseEntity<Void> put(

			@AuthenticationPrincipal final ToolSecurity.Profile profile,
			@PathVariable final long id,
			@Valid @RequestBody final Card card

	) {
		return noContent().location(events.trace(profile, update, cards.update(id, card))).build();
	}

	@RequestMapping(value = "/cards/{id}", method = RequestMethod.DELETE)
	ResponseEntity<Void> delete(

			@AuthenticationPrincipal final Profile profile,
			@PathVariable final long id

	) {

		return noContent().location(events.trace(profile, delete, cards.delete(id))).build();
	}

}
