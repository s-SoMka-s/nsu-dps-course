package task15;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class NIOServer {

  private final int from;
  private final int to;
  private final String host;

  private Selector selector;
  private int connections_n = 0;

  // NIO Selector
  // Основная концепция
  // один поток может использоваться для управления несколькими каналами,
  NIOServer(int from, String host, int to) {
    this.from = from;
    this.host = host;
    this.to = to;

    try {
      // Селектор предоставляет механизм для мониторинга одного или нескольких каналов NIO
      // и распознавания, когда один или несколько становятся доступными для передачи данных.
      selector = Selector.open();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() throws IOException {
    var channel = ServerSocketChannel.open();
    channel.configureBlocking(false);

    var socketAddress = new InetSocketAddress(from);
    channel.socket().bind(socketAddress);

    // SelectionKey.OP_ACCEPT - когда сервер принимает соединение от клиента
    channel.register(selector, SelectionKey.OP_ACCEPT);


    while (true) {
      // блокируется до тех пор, пока хотя бы один из каналов не будет готов
      selector.select();

      // каждый ключ представляет зарегистрированный канал
      var keys = selector.selectedKeys();

     for (var selectionKey : keys) {
        keys.remove(selectionKey);

        if (selectionKey.isAcceptable()) {
          accept(selectionKey);
        }
        else if (selectionKey.isReadable()) {
          read(selectionKey);
        }
        else if (selectionKey.isWritable()) {
          write(selectionKey);
        }
      }
    }
  }

  private void accept(SelectionKey key) throws IOException {
    System.out.println("Someone wants to be accepted");

    // принимаем запрос на подключение от клиента
    var channel = (ServerSocketChannel) key.channel();
    var socketChannel = channel.accept();
    socketChannel.configureBlocking(false);
    var clientKey = socketChannel.register(selector, SelectionKey.OP_READ);

    connections_n++;
    System.out.println("Accepted connection:" + channel.socket() + "Total connections: " + connections_n);

    // Открываем соединение с сервером N
    var address = new InetSocketAddress(host, to);
    var serverConnection = SocketChannel.open(address);
    serverConnection.configureBlocking(false);
    var serverKey = serverConnection.register(selector, SelectionKey.OP_READ);

    // Читаем и пишем в буферы, поэтому создаем буфер
    var BUFFER_SIZE = 1024;
    var buffer1 = ByteBuffer.allocate(BUFFER_SIZE);
    var buffer2 = ByteBuffer.allocate(BUFFER_SIZE);

    var serverConnectionService = new IOService((SocketChannel) serverKey.channel(), buffer1, buffer2, clientKey);
    var clientConnectionService = new IOService((SocketChannel) clientKey.channel(), buffer2, buffer1, serverKey);

    serverKey.attach(serverConnectionService);
    clientKey.attach(clientConnectionService);
  }

  private void read(SelectionKey key) throws IOException {
    var canRead = ((IOService) key.attachment()).read();

    if (!canRead) {
      key.channel().close();
      key.cancel();
      connections_n--;
      System.out.println("Connection closed. Total connections: " + connections_n);
    }
  }

  private void write(SelectionKey key) throws IOException {
    var canWrite = ((IOService) key.attachment()).write();

    if (canWrite) {
      key.interestOps(SelectionKey.OP_READ);
    }
  }
}