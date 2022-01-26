package task16;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

  private static void waitUser() {
    System.out.println("\nEnter space to scroll down.");

    try {
      int key;
      do {
        key = System.in.read();
      } while (key != ' ' && key != -1);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void sendRequest(URI uri) {
    var client = HttpClient.newHttpClient();
    var request = HttpRequest.newBuilder().uri(uri).build();

    client.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
        .thenApply(HttpResponse::body)
        .thenAccept(stream -> {
          var linesCount = new AtomicInteger();
          stream.forEach(line -> {
            System.out.println(line);
            linesCount.getAndIncrement();

            if (linesCount.get() % 25 == 0) {
              waitUser();
            }
          });
        })
        .join();
  }

  public static void main(String[] args) {
    var requestURL = "https://vecher.moscow/";
    var uri = URI.create(requestURL);
    sendRequest(uri);
  }
}
