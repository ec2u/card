package eu.ec2u.card.events;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import eu.ec2u.card.Tool.Container;
import eu.ec2u.card.Tool.Resource;
import eu.ec2u.card.events.Events.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Events extends Container<Event> {

    public enum Action {
        LoggedIn, LoggedOut,
        Created, Updated, Deleted
    }


    public static final String Path="/events/";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    static final class Event extends Resource {

    }

    @Entity(name="users")
    static final class EventData {

    }

}
