import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class Main {

  public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
    var threadsCount = 20;
    if (threadsCount < 1) {
      throw new IllegalArgumentException("Thread count couldn't be less then 1");
    }

    var barrier = new CyclicBarrier(threadsCount + 1);
    var piCounter = new PICounter(threadsCount, barrier);

    SignalHandler signalHandler = signal -> {
      System.out.println("It's time to stop");
      piCounter.stop();
    };
    Signal.handle(new Signal("INT"), signalHandler);

    barrier.await();
  }
}
