package eu.ec2u.card.cards;

import eu.ec2u.card.cards.Cards.Card;
import eu.ec2u.card.cards.Cards.CardData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class CardsService {

	@Autowired
	private CardsRepository cardsRepository;

	Cards browse(final Pageable slice) {

		final Cards cards = new Cards();

		cards.setPath(Cards.Id);

		cards.setContains(this.cardsRepository.findAll(slice)
				.map(CardData::transfer)
				.getContent()
		);

		return cards;
	}

	@Transactional
	Card create(final Card card) {
		return Optional.of(new CardData())
				.map(data -> data.transfer(card))
				.map(data -> cardsRepository.save(data))
				.map(CardData::transfer)
				.orElseThrow(NoSuchElementException::new);
	}

	Card relate(final long id) {
		return cardsRepository.findById(id)
				.map(CardData::transfer)
				.orElseThrow(NoSuchElementException::new);
	}

	@Transactional
	Card update(final long id, final Card card) {
		return cardsRepository.findById(id)
				.map(data -> data.transfer(card))
				.map(data -> cardsRepository.save(data))
				.map(CardData::transfer)
				.orElseThrow(NoSuchElementException::new);
	}

	@Transactional
	Card delete(final long id) {
		return cardsRepository.findById(id).map(data -> {
					cardsRepository.delete(data);
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

		HashSet<CardData> cardDataSet = new HashSet<CardData>();
		List<Card> cardList = new ArrayList<>();

		forenamePrefix.ifPresent(forename -> cardDataSet.addAll(this.cardsRepository.findByHolderForename(
				forename.toLowerCase(),
				forename.toLowerCase() + "\ufffd",
				slice
		)));

		surnamePrefix.ifPresent(surname -> cardDataSet.addAll(this.cardsRepository.findByHolderSurname(
				surname.toLowerCase(),
				surname.toLowerCase() + "\ufffd",
				slice
		)));

		expiryDate.ifPresent(date -> cardDataSet.addAll(this.cardsRepository.findByExpiryDate(
				LocalDate.parse(date),
				slice
		)));

		cardNumber.ifPresent(number -> cardDataSet.addAll(this.cardsRepository.findByVirtualCardNumber(
				number,
				slice
		)));

		if(forenamePrefix.isEmpty() && surnamePrefix.isEmpty() &&
				expiryDate.isEmpty() && cardNumber.isEmpty()) {

			cardDataSet.addAll(this.cardsRepository.findAllCardData(slice));

		}

		cardDataSet.forEach(cardData -> cardList.add(cardData.transfer()));
		cardList.sort(Comparator.comparing(card -> card.getHolderSurname().toLowerCase()));

		cards.setContains(cardList);

		return cards;

	}

}
