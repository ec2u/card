package eu.ec2u.card.tokens;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import com.google.cloud.spring.data.datastore.repository.query.Query;
import eu.ec2u.card.tokens.Tokens.TokenData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TokensRepository extends DatastoreRepository<TokenData, Long> {

	@Query("SELECT * FROM Token WHERE " +
			"serviceOrUserNameLowerCase >= @serviceOrUserNamePrefix AND serviceOrUserNameLowerCase < @serviceOrUserNamePrefixMax")
	List<TokenData> findByServiceOrUserName(
			@Param("serviceOrUserNamePrefix") String serviceOrUserNamePrefix,
			@Param("serviceOrUserNamePrefixMax") String serviceOrUserNamePrefixMax,
			Pageable pageable
	);

	@Query("SELECT * FROM Token WHERE tokenNumber = @tokenNumber")
	List<TokenData> findByTokenNumber(
			@Param("tokenNumber") Long tokenNumber,
			Pageable pageable
	);

	@Query("SELECT * FROM Token")
	List<TokenData> findAllTokenData(Pageable pageable);

}
