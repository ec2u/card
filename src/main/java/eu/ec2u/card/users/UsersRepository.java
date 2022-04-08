package eu.ec2u.card.users;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import eu.ec2u.card.users.Users.UserData;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends DatastoreRepository<UserData, Long> {

	List<UserData> findBySurnameStartingWith(String surnamePrefix, Pageable pageable);

}