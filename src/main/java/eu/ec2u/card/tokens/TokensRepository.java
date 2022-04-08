package eu.ec2u.card.tokens;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import eu.ec2u.card.tokens.Tokens.TokenData;
import org.springframework.stereotype.Repository;

@Repository
public interface TokensRepository extends DatastoreRepository<TokenData, Long> {

}
