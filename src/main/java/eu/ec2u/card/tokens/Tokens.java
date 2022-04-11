package eu.ec2u.card.tokens;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import eu.ec2u.card.Tool.*;
import eu.ec2u.card.tokens.Tokens.Token;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

import javax.validation.constraints.*;

import static eu.ec2u.card.ToolConfiguration.LinePattern;
import static eu.ec2u.card.ToolConfiguration.LineSize;

import static java.lang.String.format;

@Getter
@Setter
public class Tokens extends Container<Token> {

	static final String Id="/tokens/";

	@Getter
	@Setter
	static final class Token extends Resource {

		@NotNull
		private long tokenNumber;

		@NotNull
		@Size(max=LineSize)
		@Pattern(regexp=LinePattern)
		private String serviceOrUserName;

		@NotNull
		@Size(max=LineSize)
		@Pattern(regexp=LinePattern)
		private String serviceOrUserPassword;

	}

	@Entity(name="Token")
	static final class TokenData extends ResourceData {

		@Override
		protected Optional<String> getPath() {
			return Optional.of(this)
					.filter(data -> data.id != null)
					.map(data -> Id+data.id);
		}

		@Override
		protected Optional<String> getLabel() {
			return Optional.of(this)
					.filter(data -> data.serviceOrUserName != null)
					.filter(data -> data.serviceOrUserPassword != null)
					.map(data -> format("%s %s", data.serviceOrUserName, data.serviceOrUserPassword));
		}

		private long tokenNumber;

		private String serviceOrUserName;

		private String serviceOrUserPassword;



		Token transfer() {

			final Token token = new Token();

			transfer(token, this);

			token.setTokenNumber(tokenNumber);

			token.setServiceOrUserName(serviceOrUserName);

			token.setServiceOrUserPassword(serviceOrUserPassword);

			return token;

		}

		TokenData transfer(final Token token) {

			transfer(this, token);

			tokenNumber = token.getTokenNumber();

			serviceOrUserName = token.getServiceOrUserName();

			serviceOrUserPassword = token.getServiceOrUserPassword();

			return this;

		}

	}

}
