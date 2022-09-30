package eu.ec2u.card.cards;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
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
import java.util.*;
import java.util.regex.Pattern;

@Service
public class CardsService {

	@Autowired
	private CardsRepository cardsRepository;

	@Autowired
	private DatastoreTemplate datastoreTemplate;

	Cards browse(

			Map<String, Object> notNullParamHashMap,
			Pageable slice,
			Optional<String> sortingOrder,
			Optional<String> sortingProperty

	) {

		/* Queries are designed to work with only one parameter at time!
		 * Sorting on queries' results with a sorting property different from filtering property is not possible due
		 * to Datastore features. */

		Cards cards = new Cards();
		cards.setPath(Cards.Id);

		EntityQuery.Builder builder = Query.newEntityQueryBuilder().setKind("Card").setLimit(slice.getPageSize());

		if (notNullParamHashMap.isEmpty() && sortingOrder.isPresent() && sortingProperty.isPresent()) {

			builder = builder.setOrderBy(sortingHelper(sortingOrder.get(), sortingProperty.get()));

		} else if (!notNullParamHashMap.isEmpty() && sortingOrder.isEmpty()) {

			Map.Entry<String, Object> firstEntry = notNullParamHashMap.entrySet().stream().findFirst().get();

			builder = builder.setFilter(StructuredQuery.CompositeFilter.and(
					StructuredQuery.PropertyFilter.ge(firstEntry.getKey(),
							((String) firstEntry.getValue()).trim().toLowerCase()),
					StructuredQuery.PropertyFilter.lt(firstEntry.getKey(),
							((String) firstEntry.getValue()).trim().toLowerCase() + "\ufffd")));

		}

		Query<Entity> query = builder.build();
		
		List<Card> cardList = new ArrayList<>();

		datastoreTemplate.query(query, CardData.class)
				.toList()
				.forEach(cardData -> cardList.add(cardData.transfer()));

		cards.setContains(cardList);

		return cards;

	}

	@Transactional
	Card create(Card card) {

		String cardsFieldsValidatorResult = cardsFieldsValidator(card);

		if (!cardsFieldsValidatorResult.isEmpty()) {

			throw new ToolApplication.WrongPostArgumentsException(cardsFieldsValidatorResult);

		}

		return Optional.of(new CardData())
				.map(data -> data.transfer(card))
				.map(data -> cardsRepository.save(data))
				.map(CardData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	Card relate(long id) {

		return cardsRepository.findById(id)
				.map(CardData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	@Transactional
	Card update(long id, Card card) {

		String cardsFieldsValidatorResult = cardsFieldsValidator(card);

		if (!cardsFieldsValidatorResult.isEmpty()) {

			throw new ToolApplication.WrongPutArgumentsException(cardsFieldsValidatorResult);

		}

		return cardsRepository.findById(id)
				.map(data -> data.transfer(card))
				.map(data -> cardsRepository.save(data))
				.map(CardData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	@Transactional
	Card delete(long id) {

		return cardsRepository.findById(id).map(data -> {
					cardsRepository.delete(data);
					return data;
				})
				.map(CardData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private StructuredQuery.OrderBy sortingHelper(

			String sortingOrder,
			String sortingProperty

	) {

		if (sortingOrder.equals("asc")) {

			return StructuredQuery.OrderBy.asc(sortingProperty);

		} else {

			return StructuredQuery.OrderBy.desc(sortingProperty);

		}

	}

	private String cardsFieldsValidator(Card card) {

		Pattern holderForenamePattern = Pattern.compile("^[a-zA-Z ]+$");
		Pattern holderSurnamePattern = Pattern.compile("^[a-zA-Z ]+$");
		Pattern virtualCardNumberPattern = Pattern.compile("^[0-9]+$");

		String result = "";

		if (!holderForenamePattern.matcher(card.getHolderForename()).matches()) {
			result += "The inserted holder forename is not valid, must contain only letters and blanks. \n";
		}

		if (!holderSurnamePattern.matcher(card.getHolderSurname()).matches()) {
			result += "The inserted holder surname is not valid, must contain only letters and blanks. \n";
		}

		if (!virtualCardNumberPattern.matcher(Long.toString(card.getVirtualCardNumber())).matches()) {
			result += "The inserted virtual card number is not valid, must contain only numbers without blanks. \n";
		}

		if (card.getExpiringDate().isBefore(LocalDate.now())) {
			result += "The inserted date must follow today's. \n";
		}

		return result;

	}

}
