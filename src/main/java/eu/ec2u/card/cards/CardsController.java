/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card.cards;

import com.fasterxml.jackson.annotation.JsonView;
import eu.ec2u.card.Card.Container;
import eu.ec2u.card.Card.Resource;
import eu.ec2u.card.CardSecurity.Profile;
import eu.ec2u.card.cards.Cards.Card;
import eu.ec2u.card.events.EventsService;
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

import static eu.ec2u.card.CardConfiguration.ContainerSize;
import static eu.ec2u.card.events.Events.Action.*;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(Cards.Id)
public final class CardsController {

	@Autowired
	private CardsService cards;
	@Autowired
	private EventsService events;

	@GetMapping("")
	@JsonView(Container.class)
	ResponseEntity<Cards> get(

			@AuthenticationPrincipal final Saml2AuthenticatedPrincipal principal,
			@Valid @RequestParam(required = false, defaultValue = "0") @Min(0) final int page,
			@Valid @RequestParam(required = false, defaultValue = "25") @Min(1) @Max(ContainerSize) final int size

	) {
		return ok().body(cards.browse(PageRequest.of(page, size, Sort.by("holderSurname"))));
	}

	;

	@PostMapping("")
	ResponseEntity<Void> post(

			@AuthenticationPrincipal final Profile profile,
			@Valid @RequestBody final Card card

	) {
		return noContent().location(events.trace(profile, create, cards.create(card))).build();
	}

	@GetMapping("{id}")
	@JsonView(Resource.class)
	ResponseEntity<Card> get(

			@AuthenticationPrincipal final Profile profile,
			@PathVariable final long id

	) {
		return ok().body(cards.relate(id));
	}

	@PutMapping("{id}")
	ResponseEntity<Void> put(

			@AuthenticationPrincipal final Profile profile,
			@PathVariable final long id,
			@Valid @RequestBody final Card card

	) {
		return noContent().location(events.trace(profile, update, cards.update(id, card))).build();
	}

	@DeleteMapping("{id}")
	ResponseEntity<Void> delete(

			@AuthenticationPrincipal final Profile profile,
			@PathVariable final long id

	) {

		return noContent().location(events.trace(profile, delete, cards.delete(id))).build();
	}
}
