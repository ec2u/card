package eu.ec2u.card.cards;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import eu.ec2u.card.Tool.Container;
import eu.ec2u.card.Tool.Resource;
import eu.ec2u.card.Tool.ResourceData;
import eu.ec2u.card.cards.Cards.Card;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Optional;

import static eu.ec2u.card.ToolConfiguration.LinePattern;
import static eu.ec2u.card.ToolConfiguration.LineSize;
import static java.lang.String.format;

@Getter
@Setter
public class Cards extends Container<Card> {

	static final String Id = "/cards/";

	@Getter
	@Setter
	static final class Card extends Resource {

		@NotNull
		@Size(max = LineSize)
		@Pattern(regexp = LinePattern)
		private String holderForename;

		@NotNull
		@Size(max = LineSize)
		@Pattern(regexp = LinePattern)
		private String holderSurname;

		@NotNull
		private LocalDate expiringDate;

		@NotNull
		private Long virtualCardNumber;

	}

	@SuppressWarnings("ALL")
	@Entity(name = "Card")
	static final class CardData extends ResourceData {

		@Override
		protected Optional<String> getPath() {
			return Optional.of(this)
					.filter(data -> data.id != null)
					.map(data -> Id + data.id);
		}

		@Override
		protected Optional<String> getLabel() {
			return Optional.of(this)
					.filter(data -> data.holderForename != null)
					.filter(data -> data.holderSurname != null)
					.map(data -> format("%s %s", data.holderForename, data.holderSurname));
		}

		private String holderForename;
		private String holderSurname;

		private String holderForenameLowerCase;
		private String holderSurnameLowerCase;

		private LocalDate expiringDate;
		private Long virtualCardNumber;

		Card transfer() {

			final Card card = new Card();

			transfer(card, this);

			card.setHolderForename(holderForename);
			card.setHolderSurname(holderSurname);
			card.setExpiringDate(expiringDate);
			card.setVirtualCardNumber(virtualCardNumber);

			return card;
		}

		CardData transfer(final Card card) {

			transfer(this, card);

			holderForename = card.getHolderForename();
			holderSurname = card.getHolderSurname();

			holderForenameLowerCase = card.getHolderForename().toLowerCase();
			holderSurnameLowerCase = card.getHolderSurname().toLowerCase();

			expiringDate = card.getExpiringDate();
			virtualCardNumber = card.getVirtualCardNumber();

			return this;
		}

	}
	
}


