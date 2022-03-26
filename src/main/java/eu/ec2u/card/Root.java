/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card;


import eu.ec2u.card.cards.Cards.Card;
import eu.ec2u.card.users.Users.User;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.util.List;

import javax.validation.constraints.*;

import static eu.ec2u.card.RootServer.*;

@Getter
@Setter
public final class Root {

    private String id="/";

    private Profile profile;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    public static final class Profile {

        @NotNull
        private String esi;

        private User user;
        private Card card;

    }

    @Getter
    @Setter
    public abstract static class Container<T extends Resource> extends Resource {

        @NotNull
        @Size(max=ContainerSize)
        private List<T> contains;

    }

    @Getter
    @Setter
    public abstract static class Resource {

        @Size(max=URLSize)
        private URI id;

        @Size(max=LineSize)
        @Pattern(regexp=LinePattern)
        private String label;

        @Size(max=TextSize)
        @Pattern(regexp=TextPattern)
        private String brief;

        @Size(max=4096)
        private URI image;

    }

}
