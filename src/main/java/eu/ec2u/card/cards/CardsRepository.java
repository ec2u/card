package eu.ec2u.card.cards;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import com.google.cloud.spring.data.datastore.repository.query.Query;
import eu.ec2u.card.cards.Cards.CardData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CardsRepository extends DatastoreRepository<CardData, Long> {

	@Query("SELECT * FROM Card WHERE " +
			"holderForenameLowerCase >= @holderForenamePrefix AND holderForenameLowerCase < @holderForenamePrefixMax")
	List<CardData> findByHolderForename(
			@Param("holderForenamePrefix") String holderForenamePrefix,
			@Param("holderForenamePrefixMax") String holderForenamePrefixMax,
			Pageable pageable
	);

	@Query("SELECT * FROM Card WHERE " +
			"holderSurnameLowerCase >= @holderSurnamePrefix AND holderSurnameLowerCase < @holderSurnamePrefixMax")
	List<CardData> findByHolderSurname(
			@Param("holderSurnamePrefix") String holderSurnamePrefix,
			@Param("holderSurnamePrefixMax") String holderSurnamePrefixMax,
			Pageable pageable
	);


	@Query("SELECT * FROM Card WHERE virtualCardNumber = @virtualCardNumber")
	List<CardData> findByVirtualCardNumber(
		@Param("virtualCardNumber") Long virtualCardNumber,
		Pageable pageable
	);

	@Query("SELECT * FROM Card WHERE expiringDate = @expiringDate")
	List<CardData> findByExpiryDate(
			@Param("expiringDate")LocalDate expiringDate,
			Pageable pageable
	);

	@Query("SELECT * FROM Card")
	List<CardData> findAllCardData(Pageable pageable);

}
