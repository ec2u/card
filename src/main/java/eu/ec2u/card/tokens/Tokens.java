package eu.ec2u.card.tokens;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import eu.ec2u.card.Tool.Container;
import eu.ec2u.card.Tool.Resource;
import eu.ec2u.card.Tool.ResourceData;
import static eu.ec2u.card.ToolConfiguration.LinePattern;
import static eu.ec2u.card.ToolConfiguration.LineSize;
import eu.ec2u.card.tokens.Tokens.Token;
import static java.lang.String.format;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

@Getter
@Setter
public class Tokens extends Container<Token> {
	
	static final String Id = "/tokens/";

	@Getter
	@Setter
	static final class Token extends Resource {

		@NotNull
		private long tokenNumber;

		@NotNull
		@Size(max = LineSize)
		@Pattern(regexp = LinePattern)
		private String username;

		@NotNull
		@Size(max = LineSize)
		@Pattern(regexp = LinePattern)
		private String password;

	}

	@SuppressWarnings("ALL")
	@Entity(name = "Token")
	static final class TokenData extends ResourceData {

		@Override
		protected Optional<String> getPath() {
			return Optional.of(this)
					.filter(data -> data.id != null)
					.map(data -> Id + data.id);
		}

		@Override
		protected Optional<String> getLabel() {
			return Optional.of(this)
					.filter(data -> data.username != null)
					.filter(data -> data.password != null)
					.map(data -> format("%s %s", data.username, data.password));
		}

		private String tokenNumber;

		private String username;
		private String usernameLowerCase;

		private String password;


		Token transfer() {

			final Token token = new Token();

			transfer(token, this);

			token.setTokenNumber(Long.parseLong(tokenNumber));

			token.setUsername(username);

			token.setPassword(password);

			return token;

		}

		TokenData transfer(final Token token) {

			transfer(this, token);

			tokenNumber = String.valueOf(token.getTokenNumber());

			username = token.getUsername();
			usernameLowerCase = token.getUsername().toLowerCase();

			password = token.getPassword();

			return this;

		}

	}

}
