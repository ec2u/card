package eu.ec2u.card.events;

import eu.ec2u.card.Tool.Resource;
import eu.ec2u.card.ToolSecurity.Profile;
import eu.ec2u.card.events.Events.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventsService {

    @Autowired private EventsRepository events;


    public boolean gone(final String id) {
        return false; // !!!
    }

    public <R extends Resource> R trace(final Profile actor, final Action action, final R resource) {
        return resource; // !!!
    }

}
