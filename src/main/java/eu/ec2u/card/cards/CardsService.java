package eu.ec2u.card.cards;

import eu.ec2u.card.cards.Cards.Card;
import eu.ec2u.card.cards.Cards.CardData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.swing.text.html.Option;
import java.util.*;

@Service
public class CardsService {

	@Autowired
	private CardsRepository cards;

	Cards browse(final Pageable slice) {

		final Cards cards = new Cards();

		cards.setPath(Cards.Id);

		cards.setContains(this.cards.findAll(slice)
				.map(CardData::transfer)
				.getContent()
		);

		return cards;
	}

	@Transactional
	Card create(final Card card) {
		return Optional.of(new CardData())
				.map(data -> data.transfer(card))
				.map(data -> cards.save(data))
				.map(CardData::transfer)
				.orElseThrow(NoSuchElementException::new);
	}

	Card relate(final long id) {
		return cards.findById(id)
				.map(CardData::transfer)
				.orElseThrow(NoSuchElementException::new);
	}

	@Transactional
	Card update(final long id, final Card card) {
		return cards.findById(id)
				.map(data -> data.transfer(card))
				.map(data -> cards.save(data))
				.map(CardData::transfer)
				.orElseThrow(NoSuchElementException::new);
	}

	@Transactional
	Card delete(final long id) {
		return cards.findById(id).map(data -> {
					cards.delete(data);
					return data;
				})
				.map(CardData::transfer)
				.orElseThrow(NoSuchElementException::new);
	}

	Cards search(

			final Optional<String> forenamePrefix,
			final Optional<String> surnamePrefix,
			final Optional<String> expiryDate,
			final Optional<Long> cardNumber,
			final Pageable slice

	) {

		final Cards cards = new Cards();

		cards.setPath(Cards.Id);

		cards.setContains(this.cards.findAll(slice)
				.map(CardData::transfer)
				.filter(card -> Objects.equals(card.getVirtualCardNumber(), cardNumber.orElse(card.getVirtualCardNumber())))
				.filter(card -> card.getHolderSurname().startsWith(surnamePrefix.orElse(card.getHolderSurname())))
				.filter(card -> card.getHolderForename().startsWith(forenamePrefix.orElse(card.getHolderForename())))
				.filter(card -> card.getExpiringDate().toString().equals(expiryDate.orElse(card.getExpiringDate().toString())))
				.toList());

		return cards;

	}

}
