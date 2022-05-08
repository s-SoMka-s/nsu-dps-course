package task15;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    var url = "www.google.com";
    var from = 16080;
    var to = 80;

    var server = new NIOServer(from, url, to);
    server.run();
  }
}
