package eu.ec2u.card.cards;

import eu.ec2u.card.cards.Cards.Card;
import eu.ec2u.card.cards.Cards.CardData;
import eu.ec2u.card.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

	Cards searchBySurnamePrefixAlternative(final String surnamePrefix) {

		final Cards cards = new Cards();

		final List<Card> cardList = new ArrayList<>();

		cards.setPath(Cards.Id);

		this.cards.findAll().forEach(cardData -> {

			Card card = cardData.transfer();

			if (card.getHolderSurname().startsWith(surnamePrefix)) {

				cardList.add(card);

            }

		});

		cards.setContains(cardList);

		return cards;

	}

}
