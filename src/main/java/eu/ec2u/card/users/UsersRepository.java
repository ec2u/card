package eu.ec2u.card.users;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import com.google.cloud.spring.data.datastore.repository.query.Query;
import eu.ec2u.card.users.Users.UserData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends DatastoreRepository<UserData, Long> {

	@Query("SELECT * FROM User WHERE " +
			"forenameLowerCase >= @forenamePrefix AND forenameLowerCase < @forenamePrefixMax")
	List<UserData> findByForename(
			@Param("forenamePrefix") String forenamePrefix,
			@Param("forenamePrefixMax") String forenamePrefixMax,
			Pageable pageable
	);

	@Query("SELECT * FROM User WHERE " +
			"surnameLowerCase >= @surnamePrefix AND surnameLowerCase < @surnamePrefixMax")
	List<UserData> findBySurname(
			@Param("surnamePrefix") String surnamePrefix,
			@Param("surnamePrefixMax") String surnamePrefixMax,
			Pageable pageable
	);

	@Query("SELECT * FROM User WHERE " +
			"email >= @emailPrefix AND email < @emailPrefixMax")
	List<UserData> findByEmail(
			@Param("emailPrefix") String emailPrefix,
			@Param("emailPrefixMax") String emailPrefixMax,
			Pageable pageable
	);

	@Query("SELECT * FROM User")
	List<UserData> findAllUserData(Pageable pageable);

}