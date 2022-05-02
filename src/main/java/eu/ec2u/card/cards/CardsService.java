package eu.ec2u.card.cards;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.spring.data.datastore.core.DatastoreTemplate;
import eu.ec2u.card.cards.Cards.Card;
import eu.ec2u.card.cards.Cards.CardData;
import eu.ec2u.card.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class CardsService {

	@Autowired private CardsRepository cardsRepository;
	@Autowired private DatastoreTemplate datastoreTemplate;

	@SuppressWarnings("ALL")
	Cards browse(

			final Optional<String> label,
			final Optional<String> expiringDate,
			final Optional<Long> virtualCardNumber,
			final Pageable slice

	) {

		final Cards cards = new Cards();
		cards.setPath(Cards.Id);

		Query<Entity> query;

		if (virtualCardNumber.isPresent()) {

			query = Query.newEntityQueryBuilder()
					.setKind("Card")
					.setFilter(StructuredQuery.PropertyFilter.eq("virtualCardNumber", virtualCardNumber.get()))
					.setLimit(slice.getPageSize())
					.build();

        } else if (label.isPresent() && expiringDate.isPresent()) {

			query = Query.newEntityQueryBuilder()
					.setKind("Card")
					.setFilter(StructuredQuery.CompositeFilter.and(
							StructuredQuery.PropertyFilter.ge("label", label.get().toLowerCase()),
							StructuredQuery.PropertyFilter.lt("label", label.get().toLowerCase() + "\ufffd"),
							StructuredQuery.PropertyFilter.eq("expiringDate", expiringDate.get())
					))
					.setLimit(slice.getPageSize())
					.build();

		} else if (label.isEmpty() && expiringDate.isPresent()) {

			query = Query.newEntityQueryBuilder()
					.setKind("Card")
					.setFilter(StructuredQuery.PropertyFilter.eq("expiringDate", expiringDate.get()))
					.setLimit(slice.getPageSize())
					.build();

		} else if (label.isPresent()) {

			query = Query.newEntityQueryBuilder()
					.setKind("Card")
					.setFilter(StructuredQuery.CompositeFilter.and(
							StructuredQuery.PropertyFilter.ge("label", label.get().toLowerCase()),
							StructuredQuery.PropertyFilter.lt("label", label.get().toLowerCase() + "\ufffd")
					))
					.setLimit(slice.getPageSize())
					.build();

		} else {

			query = Query.newEntityQueryBuilder()
					.setKind("Card")
					.setLimit(slice.getPageSize())
					.build();

		}

		List<Card> cardList = new ArrayList<>();

		this.datastoreTemplate.query(query, CardData.class)
				.toList()
				.forEach(cardData -> cardList.add(cardData.transfer()));

		cards.setContains(cardList);

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

}
