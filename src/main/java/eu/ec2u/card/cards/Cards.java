/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card.cards;

import eu.ec2u.card.Card.ResourceData;
import eu.ec2u.card.cards.Cards.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;

import javax.validation.constraints.*;

import static eu.ec2u.card.Card.LinePattern;
import static eu.ec2u.card.Card.LineSize;

import static java.lang.String.format;


@Getter
@Setter
public class Cards extends eu.ec2u.card.Card.Container<Card> {

	static final String Id="/cards/";

	@Getter
	@Setter
	static final class Card extends eu.ec2u.card.Card.Resource {

		@NotNull
		@Size(max=LineSize)
		@Pattern(regexp=LinePattern)
		private String holderForename;

		@NotNull
		@Size(max=LineSize)
		@Pattern(regexp=LinePattern)
		private String holderSurName;

		@NotNull
		// @DateTimeFormat
		private Date expiringDate;

		@NotNull
		private Long virtualCardNumber;

	}

	static final class CardData extends ResourceData {

		protected Optional<String> getPath() {
			return Optional.of(this)
					.filter(data -> data.id != null)
					.map(data -> Id+data.id);
		}

		protected Optional<String> getLabel() {
			return Optional.of(this)
					.filter(data -> data.holderForename != null)
					.filter(data -> data.holderSurname != null)
					.map(data -> format("%s %s", data.holderForename, data.holderSurname));
		}

		private String holderForename;
		private String holderSurname;
		private Date expiringDate;
		private Long virtualCardNumber;

		Card transfer() {

			final Card card = new Card();

			transfer(card, this);

			card.setHolderForename(holderForename);
			card.setHolderSurName(holderSurname);
			card.setExpiringDate(expiringDate);
			card.setVirtualCardNumber(virtualCardNumber);

			return card;
		}

		CardData transfer(final Card card) {

			transfer(this, card);

			holderForename = card.getHolderForename();
			holderSurname = card.getHolderSurName();
			expiringDate = card.getExpiringDate();
			virtualCardNumber = card.getVirtualCardNumber();

			return this;
		}

	}

}


