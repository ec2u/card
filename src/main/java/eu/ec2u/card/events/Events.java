package eu.ec2u.card.events;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import eu.ec2u.card.Card.Container;
import eu.ec2u.card.Card.Resource;
import eu.ec2u.card.events.Events.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Events extends Container<Event> {

    public enum Action {
        login, logout,
        create, update, delete
    }


    public static final String Id="/events/";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    static final class Event extends Resource {

    }

    @Entity(name="users")
    static final class EventData {

    }

}
