package eu.ec2u.card.events;

import eu.ec2u.card.Tool.Resource;
import eu.ec2u.card.ToolSecurity.Profile;
import eu.ec2u.card.events.Events.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class EventsService {

    @Autowired private EventsRepository events;


    public URI trace(final Profile actor, final Action action, final Resource resource) {
        return URI.create(resource.getPath()); // !!!
    }

}
