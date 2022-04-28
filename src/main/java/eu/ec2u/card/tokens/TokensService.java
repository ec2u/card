package eu.ec2u.card.tokens;

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


	Tokens browse(final Pageable slice) {

		final Tokens tokens = new Tokens();

		tokens.setPath(Tokens.Id);

		tokens.setContains(this.tokensRepository.findAll(slice)
				.map(Tokens.TokenData::transfer)
				.getContent()
		);

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

	Tokens search(

			final Optional<String> usernamePrefix,
			final Optional<Long> tokenNumber,
			final Pageable slice

	) {

		final Tokens tokens = new Tokens();

		tokens.setPath(Tokens.Id);

		HashSet<TokenData> tokenDataSet = new HashSet<TokenData>();
		List<Token> tokenList = new ArrayList<>();

		usernamePrefix.ifPresent(username -> tokenDataSet.addAll(this.tokensRepository.findByServiceOrUserName(
				username.toLowerCase(),
				username.toLowerCase() + "\ufffd",
				slice
		)));

		tokenNumber.ifPresent(number -> tokenDataSet.addAll(this.tokensRepository.findByTokenNumber(
				number,
				slice
		)));

		if(usernamePrefix.isEmpty() && tokenNumber.isEmpty()) {

			tokenDataSet.addAll(this.tokensRepository.findAllTokenData(slice));

		}

		tokenDataSet.forEach(tokenData -> tokenList.add(tokenData.transfer()));
		tokenList.sort(Comparator.comparing(token -> token.getServiceOrUserName().toLowerCase()));

		tokens.setContains(tokenList);

		return tokens;

	}

}
