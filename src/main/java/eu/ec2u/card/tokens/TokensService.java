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
			final Optional<Long> tokenNumber,
			final Pageable slice

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
					.build();

        } else if (tokenNumber.isEmpty() && username.isPresent()) {

			query = Query.newEntityQueryBuilder()
					.setKind("Token")
					.setFilter(StructuredQuery.CompositeFilter.and(
									StructuredQuery.PropertyFilter.ge("usernameLowerCase", username.get().toLowerCase()),
									StructuredQuery.PropertyFilter.lt("usernameLowerCase", username.get().toLowerCase() + "\ufffd")))
					.setLimit(slice.getPageSize())
					.build();

		} else if (tokenNumber.isEmpty() && username.isEmpty()) {

			query = Query.newEntityQueryBuilder()
					.setKind("Token")
					.setLimit(slice.getPageSize())
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

				})
				.map(TokenData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

}
