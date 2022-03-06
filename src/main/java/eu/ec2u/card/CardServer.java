package eu.ec2u.card;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class CardServer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override public void customize(final ConfigurableWebServerFactory factory) {
        factory.setPort(Optional.ofNullable(System.getenv("PORT")).map(Integer::parseInt).orElse(8080));
    }

}
