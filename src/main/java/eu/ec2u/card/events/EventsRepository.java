package eu.ec2u.card.events;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import eu.ec2u.card.Tool;
import eu.ec2u.card.ToolSecurity.Profile;
import eu.ec2u.card.events.Events.Action;
import eu.ec2u.card.events.Events.EventData;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends DatastoreRepository<EventData, String> {

    public default String trace(
            final Profile actor, final Action action, final String container, final Tool.Resource resource
    ) {
        return resource.getId();
    }

}