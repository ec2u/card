/*
 * Copyright © 2020-2022 EC2U Consortium. All rights reserved.
 */

package eu.ec2u.card;

import com.metreeca.gcp.GCPServer;

import static com.metreeca.json.Values.uuid;
import static com.metreeca.rest.Handler.asset;
import static com.metreeca.rest.MessageException.status;
import static com.metreeca.rest.Response.OK;
import static com.metreeca.rest.Toolbox.resource;
import static com.metreeca.rest.handlers.Publisher.publisher;
import static com.metreeca.rest.handlers.Router.router;
import static com.metreeca.rest.services.Logger.Level.debug;
import static com.metreeca.rest.wrappers.Server.server;

public final class Card {

	public static final String root="root"; // root role


	static {
		debug.log("com.metreeca");
	}

	public static void main(final String... args) {
		new Card().start();
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void start() {
		new GCPServer().delegate(toolbox -> toolbox

				.get(() -> server()

						.with(bearer(uuid() /* !!! service(vault()).get("eu-ec2u-data").orElse(uuid())*/, root))

						.wrap(router()

								.path("/*", asset(

										publisher(resource(Card.class, "/static"))

												.fallback("/index.html"),

										router()

												.path("/", status(OK))

								))

						)
				)

		).start();
	}

}
