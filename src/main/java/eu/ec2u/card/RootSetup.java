/*
 * Copyright © 2022 EC2U Alliance. All rights reserved.
 */

package eu.ec2u.card;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import static com.metreeca.core.Lambdas.checked;
import static com.metreeca.rest.Toolbox.service;
import static com.metreeca.rest.Toolbox.text;

import static work.BeanCodec.codec;

@Getter
public final class RootSetup {

    public static Supplier<RootSetup> setup() {
        return checked(() -> service(codec()).decode(text(RootSetup.class, ".json"), RootSetup.class));
    }


    private String version;
    private LocalDateTime instant;

    private String gateway;

}
