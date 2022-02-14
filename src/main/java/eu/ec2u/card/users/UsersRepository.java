package eu.ec2u.card.users;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import eu.ec2u.card.users.Users.UserData;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends DatastoreRepository<UserData, String> {

}