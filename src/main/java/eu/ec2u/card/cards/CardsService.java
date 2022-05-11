package eu.ec2u.card.cards;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.spring.data.datastore.core.DatastoreTemplate;
import eu.ec2u.card.ToolApplication;
import eu.ec2u.card.cards.Cards.Card;
import eu.ec2u.card.cards.Cards.CardData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class CardsService {

	@Autowired private CardsRepository cardsRepository;

	@Autowired private DatastoreTemplate datastoreTemplate;

	@SuppressWarnings("ALL")
	Cards browse(

			final Optional<String> forename,
			final Optional<String> surname,
			final Optional<String> expiringDate,
			final Optional<String> virtualCardNumber,
			final Pageable slice,
			final Optional<String> sortingOrder,
			final Optional<String> sortingProperty

	) {

		// Queries are designed to work with only one parameter at time!

		final Cards cards = new Cards();
		cards.setPath(Cards.Id);

		Query<Entity> query = null;

		if (forename.isPresent() && surname.isEmpty() && expiringDate.isEmpty() && virtualCardNumber.isEmpty()) {

			query = Query.newEntityQueryBuilder()
					.setKind("Card")
					.setFilter(StructuredQuery.CompositeFilter.and(
							StructuredQuery.PropertyFilter.ge("holderForenameLowerCase", forename.get().toLowerCase()),
							StructuredQuery.PropertyFilter.lt("holderForenameLowerCase", forename.get().toLowerCase() + "\ufffd")
					))
					.setOrderBy(sortingFromOptional(sortingOrder, "holderForenameLowerCase"))
					.setLimit(slice.getPageSize())
					.build();

        } else if (forename.isEmpty() && surname.isPresent() && expiringDate.isEmpty() && virtualCardNumber.isEmpty()) {

			query = Query.newEntityQueryBuilder()
					.setKind("Card")
					.setFilter(StructuredQuery.CompositeFilter.and(
							StructuredQuery.PropertyFilter.ge("holderSurnameLowerCase", surname.get().toLowerCase()),
							StructuredQuery.PropertyFilter.lt("holderSurnameLowerCase", surname.get().toLowerCase() + "\ufffd")
					))
					.setOrderBy(sortingFromOptional(sortingOrder, "holderSurnameLowerCase"))
					.setLimit(slice.getPageSize())
					.build();

        } else if (forename.isEmpty() && surname.isEmpty() && expiringDate.isPresent() && virtualCardNumber.isEmpty()) {

			query = Query.newEntityQueryBuilder()
					.setKind("Card")
					.setFilter(StructuredQuery.PropertyFilter.le("expiringDate", expiringDate.get().trim()))
					.setOrderBy(sortingFromOptional(sortingOrder, "expiringDate"))
					.setLimit(slice.getPageSize())
					.build();

		} else if (forename.isEmpty() && surname.isEmpty() && expiringDate.isEmpty() && virtualCardNumber.isPresent()) {

			query = Query.newEntityQueryBuilder()
					.setKind("Card")
					.setFilter(StructuredQuery.PropertyFilter.eq("virtualCardNumber", virtualCardNumber.get()))
					.setOrderBy(sortingFromOptional(sortingOrder, "virtualCardNumber"))
					.setLimit(slice.getPageSize())
					.build();

		} else if (forename.isEmpty() && surname.isEmpty() && expiringDate.isEmpty() && virtualCardNumber.isEmpty()) {

			if(sortingProperty.isEmpty() && sortingOrder.isEmpty()) {

				query = Query.newEntityQueryBuilder()
						.setKind("Card")
						.setLimit(slice.getPageSize())
						.build();

			} else {

				if (!isSortingPropertyValid(sortingProperty)) {

					throw new ToolApplication.WrongQueryArgumentsException(
							"Sorting property parameter incorrect." +
									" Must be holderForenameLowerCase, holderSurnameLowerCase, expiringDate or virtualCardNumber!");

				}

				query = Query.newEntityQueryBuilder()
						.setKind("Card")
						.setOrderBy(sortingFromOptional(sortingOrder, sortingProperty.orElse("holderSurnameLowerCase").trim()))
						.setLimit(slice.getPageSize())
						.build();

			}

		} else {

			throw new ToolApplication.WrongQueryArgumentsException(
					"Queries are designed to work with only one parameter at time!");

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

		final String cardsFieldsValidatorResult = cardsFieldsValidator(card);

		if(!cardsFieldsValidatorResult.isEmpty()) {

			throw new ToolApplication.WrongPostArgumentsException(cardsFieldsValidatorResult);

		}

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

		final String cardsFieldsValidatorResult = cardsFieldsValidator(card);

		if(!cardsFieldsValidatorResult.isEmpty()) {

			throw new ToolApplication.WrongPutArgumentsException(cardsFieldsValidatorResult);

		}

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

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("ALL")
	private final StructuredQuery.OrderBy sortingFromOptional(final Optional<String> sortingOrder, final String sortingProperty) {

		if(sortingOrder.isPresent()) {

			if (sortingOrder.get().trim().equalsIgnoreCase("asc")) {

				return StructuredQuery.OrderBy.asc(sortingProperty);

			} else if (sortingOrder.get().trim().equalsIgnoreCase("desc")) {

				return StructuredQuery.OrderBy.desc(sortingProperty);

			} else throw new ToolApplication.WrongQueryArgumentsException(
					"Specified sorting order is invalid, must be asc or desc!");

		} else {

			return StructuredQuery.OrderBy.asc(sortingProperty);

		}

	}

	private boolean isSortingPropertyValid(final Optional<String> sortingProperty) {

		return sortingProperty.map(s -> s.trim().equalsIgnoreCase("holderForenameLowerCase") ||
				s.trim().equalsIgnoreCase("holderSurnameLowerCase") ||
				s.trim().equalsIgnoreCase("virtualCardNumber") ||
				s.trim().equalsIgnoreCase("expiringDate")).orElse(true);

	}

	private String cardsFieldsValidator(final Card card) {

		final Pattern holderForenamePattern = Pattern.compile("^[a-zA-Z ]+$");
		final Pattern holderSurnamePattern = Pattern.compile("^[a-zA-Z ]+$");
		final Pattern virtualCardNumberPattern = Pattern.compile("^[0-9]+$");

		String result = "";

		if (!holderForenamePattern.matcher(card.getHolderForename()).matches())
			result += "The inserted holder forename is not valid, must contain only letters and blanks. \n";

		if (!holderSurnamePattern.matcher(card.getHolderSurname()).matches())
			result += "The inserted holder surname is not valid, must contain only letters and blanks. \n";

		if (!virtualCardNumberPattern.matcher(Long.toString(card.getVirtualCardNumber())).matches())
			result += "The inserted virtual card number is not valid, must contain only numbers without blanks. \n";

		if (card.getExpiringDate().isBefore(LocalDate.now()))
			result += "The inserted date must follow today's. \n";

		return result;

	}

}
