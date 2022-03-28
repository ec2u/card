package eu.ec2u.card.cards.escr;

import lombok.NoArgsConstructor;

import javax.json.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@NoArgsConstructor
public class EscrHttpManager {

	public String httpGetter(final String uri) throws IOException, InterruptedException {

		final HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.header("Key", System.getenv("API_SANDBOX_KEY"))
				.header("Accept", "application/json")
				.uri(URI.create(uri))
				.build();

		return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();

	}

	public String httpPoster(final String uri, final JsonObject jsonObject) throws IOException, InterruptedException {

		final HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.header("Key", System.getenv("API_SANDBOX_KEY"))
				.header("Accept", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
				.build();

		return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();

	}

	public String httpPutter(final String uri, final JsonObject jsonObject) throws IOException, InterruptedException {

		final HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.header("Key", System.getenv("API_SANDBOX_KEY"))
				.header("Accept", "application/json")
				.PUT(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
				.build();

		return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();

	}

	public String httpDeleter(final String uri) throws IOException, InterruptedException {

		final HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.header("Key", System.getenv("API_SANDBOX_KEY"))
				.header("Accept", "application/json")
				.DELETE()
				.build();

		return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();

	}

}
