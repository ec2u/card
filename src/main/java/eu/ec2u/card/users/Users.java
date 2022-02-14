package eu.ec2u.card.users;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import eu.ec2u.card.Card.Container;
import eu.ec2u.card.Card.Resource;
import eu.ec2u.card.users.Users.User;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;

@Getter
@SuperBuilder
public class Users extends Container<User> {

    public static final String Path="/users/";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Jacksonized
    @SuperBuilder
    @Accessors(fluent=true)
    public static final class User extends Resource {

        private final String forename;
        private final String surname;

        private final String email;

    }

    @Entity(name="users")
    @Accessors(fluent=true, chain=true)
    public static final class Data {

        @Id
        private String id;

        private String forename;
        private String surname;

        private String email;

    }

}
