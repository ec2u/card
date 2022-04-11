package eu.ec2u.card.escr;

import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@NoArgsConstructor
public class EscrHttpManager {

	public String httpGetter(final String uri) {

		String output = "";

		final HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.header("Key", System.getenv("API_SANDBOX_KEY"))
				.header("Accept", "application/json")
				.uri(URI.create(uri))
				.build();

		try {
			output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
		} catch (final IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return output;

	}

	public void httpPoster(final String uri, final String jsonString) {

		final HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.header("Key", System.getenv("API_SANDBOX_KEY"))
				.header("Accept", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonString))
				.build();

		try {
			HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
		} catch (final IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void httpPutter(final String uri, final String jsonString) {

		final HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.header("Key", System.getenv("API_SANDBOX_KEY"))
				.header("Accept", "application/json")
				.PUT(HttpRequest.BodyPublishers.ofString(jsonString))
				.build();

		try {
			HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
		} catch (final IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void httpDeleter(final String uri) {

		final HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.header("Key", System.getenv("API_SANDBOX_KEY"))
				.header("Accept", "application/json")
				.DELETE()
				.build();

		try {
			HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
		} catch (final IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
