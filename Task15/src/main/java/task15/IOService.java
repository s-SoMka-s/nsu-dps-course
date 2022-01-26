package task15;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class IOService {

  private final SocketChannel channel;
  private final ByteBuffer readBuffer, writeBuffer;

  private final SelectionKey receiverKey;

  IOService (SocketChannel channel, ByteBuffer readBuffer, ByteBuffer writeBuffer, SelectionKey receiverKey) {
    this.channel = channel;
    this.readBuffer = readBuffer;
    this.writeBuffer = writeBuffer;
    this.receiverKey = receiverKey;
  }

  public boolean write() throws IOException {
    // FROM -> | 3 2 1 | -> TO  (flip)  | 1 2 3 | -> TO
    writeBuffer.flip();
    channel.write(writeBuffer);
    return writeBuffer.remaining() == 0;
  }

  public boolean read() throws IOException {
    readBuffer.clear();
    var readedBytes = channel.read(readBuffer);

    if (readedBytes == -1) {
      receiverKey.channel().close();
      receiverKey.cancel();
      return false;
    }

    var service = (IOService) receiverKey.attachment();
    if (!service.write()) {
      service.receiverKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }
    return true;
  }
}
