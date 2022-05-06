package eu.ec2u.card.tokens;

import com.google.cloud.datastore.Entity;
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

@Service
public class TokensService {

	@Autowired private TokensRepository tokensRepository;

	@Autowired private DatastoreTemplate datastoreTemplate;

	@SuppressWarnings("ALL")
	Tokens browse(

			final Optional<String> username,
			final Optional<String> tokenNumber,
			final Pageable slice,
			final Optional<String> sortingOrder,
			final Optional<String> sortingProperty

	) {

		// Queries are designed to work with only one parameter at time!

		final Tokens tokens = new Tokens();
		tokens.setPath(Tokens.Id);

		Query<Entity> query = null;

		if (tokenNumber.isPresent() && username.isEmpty()) {

			query = Query.newEntityQueryBuilder()
					.setKind("Token")
					.setFilter(StructuredQuery.PropertyFilter.eq("tokenNumber", tokenNumber.get()))
					.setLimit(slice.getPageSize())
					.setOrderBy(sortingFromOptional(sortingOrder, "tokenNumber"))
					.build();

        } else if (tokenNumber.isEmpty() && username.isPresent()) {

			query = Query.newEntityQueryBuilder()
					.setKind("Token")
					.setFilter(StructuredQuery.CompositeFilter.and(
									StructuredQuery.PropertyFilter.ge("usernameLowerCase", username.get().toLowerCase()),
									StructuredQuery.PropertyFilter.lt("usernameLowerCase", username.get().toLowerCase() + "\ufffd")))
					.setLimit(slice.getPageSize())
					.setOrderBy(sortingFromOptional(sortingOrder, "usernameLowerCase"))
					.build();

		} else if (tokenNumber.isEmpty() && username.isEmpty()) {

			if (!isSortingPropertyValid(sortingProperty)) {

				throw new ToolApplication.WrongQueryArgumentsException(
						"Sorting property parameter incorrect. Must be username or tokenNumber!");

			}

			query = Query.newEntityQueryBuilder()
					.setKind("Token")
					.setLimit(slice.getPageSize())
					.setOrderBy(sortingFromOptional(sortingOrder, sortingProperty.orElse("username").trim()))
					.build();

		} else {

			throw new ToolApplication.WrongQueryArgumentsException(
					"Queries are designed to work with only one parameter at time!");

		}

		List<Token> tokenList = new ArrayList<>();

		this.datastoreTemplate.query(query, TokenData.class)
				.toList()
				.forEach(tokenData -> tokenList.add(tokenData.transfer()));

		tokens.setContains(tokenList);

		return tokens;

	}

	@Transactional
	Token create(final Token token) {

		return Optional.of(new Tokens.TokenData())
				.map(data -> data.transfer(token))
				.map(data -> tokensRepository.save(data))
				.map(Tokens.TokenData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	Token relate(final long tokenNumber) {

		return tokensRepository.findById(tokenNumber)
				.map(TokenData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	@Transactional
	Token update(final long tokenNumber, final Token token) {

		return tokensRepository.findById(tokenNumber)
				.map(data -> data.transfer(token))
				.map(data -> tokensRepository.save(data))
				.map(TokenData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	@Transactional
	Token delete(final long tokenNumber) {

		return tokensRepository.findById(tokenNumber)
				.map(data -> {
					tokensRepository.delete(data);
					return data;
				}).map(TokenData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("ALL")
	private StructuredQuery.OrderBy sortingFromOptional(Optional<String> sortingOrder, String sortingProperty) {

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

	private boolean isSortingPropertyValid(Optional<String> sortingProperty) {

		return sortingProperty.map(s -> s.trim().equalsIgnoreCase("username") ||
				s.trim().equalsIgnoreCase("tokenNumber")).orElse(true);

	}

}
