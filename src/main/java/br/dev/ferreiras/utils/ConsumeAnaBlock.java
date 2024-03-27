package br.dev.ferreiras.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ConsumeAnaBlock {

  public static String getVersionBlockedSites() {

    var client = HttpClient.newHttpClient();

    var request = HttpRequest.newBuilder(
            URI.create("https://api.anablock.net.br/api/version"))
            .header("accept", "application/json")
            .build();

    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .join();
  }

  public static List<String> getLatestListOfBlockedSites() {

    List<String> list = new LinkedList<>();

    var client = HttpClient.newHttpClient();

    var request = HttpRequest.newBuilder(
            URI.create("https://api.anablock.net.br/api/domain/all"))
            .header("accept", "application/json")
            .build();

    var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .join();

    return Collections.singletonList(response);
  }
}
