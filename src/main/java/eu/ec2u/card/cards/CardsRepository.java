package eu.ec2u.card.cards;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import eu.ec2u.card.cards.Cards.CardData;
import org.springframework.stereotype.Repository;

@Repository
public interface CardsRepository extends DatastoreRepository<CardData, Long> {
}
