package eu.ec2u.card.users;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import eu.ec2u.card.Tool.Container;
import eu.ec2u.card.Tool.Resource;
import eu.ec2u.card.Tool.ResourceData;
import static eu.ec2u.card.ToolConfiguration.LinePattern;
import static eu.ec2u.card.ToolConfiguration.LineSize;
import eu.ec2u.card.users.Users.User;
import static java.lang.String.format;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

@Getter
@Setter
public class Users extends Container<User> {

	static final String Id = "/users/";


	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Getter
	@Setter
	static final class User extends Resource {

		@NotNull
		private Boolean admin;

		@NotNull
		@Size(max = LineSize)
		@Pattern(regexp = LinePattern)
		private String forename;

		@NotNull
		@Size(max = LineSize)
		@Pattern(regexp = LinePattern)
		private String surname;

		@NotNull
		@Size(max = LineSize)
		@Email
		private String email;

	}

	@SuppressWarnings("ALL")
	@Entity(name = "User")
	static final class UserData extends ResourceData {

		protected Optional<String> getPath() {
			return Optional.of(this)
					.filter(data -> data.id != null)
					.map(data -> Id + data.id);
		}

		protected Optional<String> getLabel() {
			return Optional.of(this)
					.filter(data -> data.forename != null)
					.filter(data -> data.surname != null)
					.map(data -> format("%s %s", data.forename, data.surname));
		}

		private boolean admin;

		private String forename;
		private String surname;

		private String forenameLowerCase;
		private String surnameLowerCase;

		private String email;


		User transfer() {

			final User user = new User();

			transfer(user, this);

			user.setAdmin(admin);

			user.setForename(forename);
			user.setSurname(surname);

			user.setEmail(email);

			return user;

		}

		UserData transfer(final User user) {

			transfer(this, user);

			admin = user.getAdmin();

			forename = user.getForename();
			surname = user.getSurname();

			forenameLowerCase = user.getForename().toLowerCase();
			surnameLowerCase = user.getSurname().toLowerCase();

			email = user.getEmail();

			return this;

		}

	}

}
