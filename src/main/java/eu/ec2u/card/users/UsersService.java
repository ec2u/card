package eu.ec2u.card.users;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.spring.data.datastore.core.DatastoreTemplate;
import eu.ec2u.card.ToolApplication;
import eu.ec2u.card.users.Users.User;
import eu.ec2u.card.users.Users.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class UsersService {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private DatastoreTemplate datastoreTemplate;

	Users browse(

			Map<String, Object> notNullParamHashMap,
			Pageable slice,
			Optional<String> sortingOrder,
			Optional<String> sortingProperty

	) {

		/* Queries are designed to work with only one parameter at time!
		 * Sorting on queries' results with a sorting property different from filtering property is not possible due
		 * to Datastore features. */

		Users users = new Users();
		users.setPath(Users.Id);

		EntityQuery.Builder builder = Query.newEntityQueryBuilder().setKind("User").setLimit(slice.getPageSize());

		if (notNullParamHashMap.isEmpty() && sortingOrder.isPresent() && sortingProperty.isPresent()) {

			builder = builder.setOrderBy(sortingHelper(sortingOrder.get(), sortingProperty.get()));

		} else if (!notNullParamHashMap.isEmpty() && sortingOrder.isEmpty()) {

			Map.Entry<String, Object> firstEntry = notNullParamHashMap.entrySet().stream().findFirst().get();

			if (firstEntry.getKey().equals("isAdmin")) {

				builder = builder.setFilter(StructuredQuery.PropertyFilter.eq("admin",
						(Boolean) firstEntry.getValue()));

			} else {

				builder = builder.setFilter(StructuredQuery.CompositeFilter.and(
						StructuredQuery.PropertyFilter.ge(firstEntry.getKey(),
								((String) firstEntry.getValue()).trim().toLowerCase()),
						StructuredQuery.PropertyFilter.lt(firstEntry.getKey(),
								((String) firstEntry.getValue()).trim().toLowerCase() + "\ufffd")));

			}

		}

		Query<Entity> query = builder.build();

		List<User> userList = new ArrayList<>();

		datastoreTemplate.query(query, UserData.class)
				.toList()
				.forEach(userData -> userList.add(userData.transfer()));

		users.setContains(userList);

		return users;

	}

	@Transactional
	User create(User user) {

		String usersFieldsValidatorResult = usersFieldsValidator(user);

		if (!usersFieldsValidatorResult.isEmpty()) {

			throw new ToolApplication.WrongPostArgumentsException(usersFieldsValidatorResult);

		}

		return Optional.of(new UserData())
				.map(data -> data.transfer(user))
				.map(data -> usersRepository.save(data))
				.map(UserData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	User relate(long id) {

		return usersRepository.findById(id)
				.map(UserData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	@Transactional
	User update(long id, User user) {

		String usersFieldsValidatorResult = usersFieldsValidator(user);

		if (!usersFieldsValidatorResult.isEmpty()) {

			throw new ToolApplication.WrongPutArgumentsException(usersFieldsValidatorResult);

		}

		return usersRepository.findById(id)
				.map(data -> data.transfer(user))
				.map(data -> usersRepository.save(data))
				.map(UserData::transfer)
				.orElseThrow(NoSuchElementException::new);

	}

	@Transactional
	User delete(long id) {

		return usersRepository.findById(id)
				.map(data -> {
					usersRepository.delete(data);
					return data;
				}).map(UserData::transfer)
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

	private String usersFieldsValidator(User user) {

		Pattern forenamePattern = Pattern.compile("^[a-zA-Z ]+$");
		Pattern surnamePattern = Pattern.compile("^[a-zA-Z ]+$");
		Pattern emailPattern = Pattern.compile("^[a-z0-9.]+@[a-z0-9.]+$");

		String result = "";

		if (!forenamePattern.matcher(user.getForename()).matches()) {
			result += "The inserted forename is not valid, must contain only letters and blanks. \n";
		}

		if (!surnamePattern.matcher(user.getSurname()).matches()) {
			result += "The inserted surname is not valid, must contain only letters and blanks. \n";
		}

		if (!emailPattern.matcher(user.getEmail()).matches()) {
			result += "The inserted email is not valid, must be lower case, \n must contain @ and no blanks. \n";
		}

		return result;

	}

}
