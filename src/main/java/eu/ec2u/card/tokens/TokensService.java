package eu.ec2u.card.tokens;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.spring.data.datastore.core.DatastoreTemplate;
import eu.ec2u.card.ToolApplication;
import eu.ec2u.card.tokens.Tokens.Token;
import eu.ec2u.card.tokens.Tokens.TokenData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class TokensService {

	@Autowired
	private TokensRepository tokensRepository;

	@Autowired
	private DatastoreTemplate datastoreTemplate;

	Tokens browse(

			Map<String, Object> notNullParamHashMap,
			Pageable slice,
			Optional<String> sortingOrder,
			Optional<String> sortingProperty

	) {

		/* Queries are designed to work with only one parameter at time!
		 *  Sorting on queries' results with a sorting property different from filtering property is not possible due
		 * to Datastore features. */

		Tokens tokens = new Tokens();
		tokens.setPath(Tokens.Id);

		EntityQuery.Builder builder = Query.newEntityQueryBuilder().setKind("Token").setLimit(slice.getPageSize());

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

		List<Token> tokenList = new ArrayList<>();

		datastoreTemplate.query(query, TokenData.class)
				.toList()
				.forEach(tokenData -> tokenList.add(tokenData.transfer()));

		tokens.setContains(tokenList);

		return tokens;

	}

	@Transactional
	Token create(Token token) {

		String tokensFieldsValidatorResult = tokensFieldsValidator(token);

		if (!tokensFieldsValidatorResult.isEmpty()) {

			throw new ToolApplication.WrongPostArgumentsException(tokensFieldsValidatorResult);

		}

		return Optional.of(new Tokens.TokenData())
				.map(data -> data.transfer(token))
				.map(data -> tokensRepository.save(data))
				.map(Tokens.TokenData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	Token relate(long tokenNumber) {

		return tokensRepository.findById(tokenNumber)
				.map(TokenData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	@Transactional
	Token update(long tokenNumber, Token token) {

		String tokensFieldsValidatorResult = tokensFieldsValidator(token);

		if (!tokensFieldsValidatorResult.isEmpty()) {

			throw new ToolApplication.WrongPutArgumentsException(tokensFieldsValidatorResult);

		}

		return tokensRepository.findById(tokenNumber)
				.map(data -> data.transfer(token))
				.map(data -> tokensRepository.save(data))
				.map(TokenData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	@Transactional
	Token delete(long tokenNumber) {

		return tokensRepository.findById(tokenNumber)
				.map(data -> {
					tokensRepository.delete(data);
					return data;
				}).map(TokenData::transfer)
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

	private String tokensFieldsValidator(Token token) {

		Pattern tokenNumberPattern = Pattern.compile("^[0-9]+$");

		String result = "";

		if (!tokenNumberPattern.matcher(Long.toString(token.getTokenNumber())).matches()) {
			result += "The inserted token number is not valid, must contain only numbers without blanks. \n";
		}

		return result;

	}

}
