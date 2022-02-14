package eu.ec2u.card.events;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import eu.ec2u.card.events.Events.EventData;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends DatastoreRepository<EventData, Long> {

}