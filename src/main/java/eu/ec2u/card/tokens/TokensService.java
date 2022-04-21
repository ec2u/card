package eu.ec2u.card.tokens;

import eu.ec2u.card.tokens.Tokens.Token;
import eu.ec2u.card.tokens.Tokens.TokenData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@SuppressWarnings("ALL")
public class TokensService {

	@Autowired private TokensRepository tokens;


	Tokens browse(final Pageable slice) {

		final Tokens tokens = new Tokens();

		tokens.setPath(Tokens.Id);

		tokens.setContains(this.tokens.findAll(slice)
				.map(Tokens.TokenData::transfer)
				.getContent()
		);

		return tokens;

	}

	@Transactional
	Token create(final Token token) {
		return Optional.of(new Tokens.TokenData())
				.map(data -> data.transfer(token))
				.map(data -> tokens.save(data))
				.map(Tokens.TokenData::transfer)
				.orElseThrow(NoSuchElementException::new);
	}

	Token relate(final long tokenNumber) {
		return tokens.findById(tokenNumber)
				.map(TokenData::transfer)
				.orElseThrow(NoSuchElementException::new);
	}

	@Transactional
	Token update(final long tokenNumber, final Token token) {
		return tokens.findById(tokenNumber)
				.map(data -> data.transfer(token))
				.map(data -> tokens.save(data))
				.map(TokenData::transfer)
				.orElseThrow(NoSuchElementException::new);
	}

	@Transactional
	Token delete(final long tokenNumber) {
		return tokens.findById(tokenNumber)
				.map(data -> {

					tokens.delete(data);

					return data;

				})
				.map(TokenData::transfer)
				.orElseThrow(NoSuchElementException::new);
	}

	Tokens search(

			final Optional<String> usernamePrefix,
			final Optional<Long> tokenNumber,
			final Pageable slice

	) {

		final Tokens tokens = new Tokens();

		tokens.setPath(Tokens.Id);

		List<Token> tokenList = new ArrayList<>();

		tokens.setContains(this.tokens.findAll(slice)
				.map(TokenData::transfer)
				.filter(token -> token.getTokenNumber() == tokenNumber.orElse(token.getTokenNumber()))
				.filter(token -> token.getServiceOrUserName().startsWith(usernamePrefix.orElse(token.getServiceOrUserName())))
				.toList());

		return tokens;

	}

}
