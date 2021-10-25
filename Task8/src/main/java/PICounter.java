import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

class Worker extends Thread {

  private final long start;
  private final long end;
  private double result = 0;

  public Worker(long start, long end) {
    this.start = start;
    this.end = end;
  }

  public double getResult() {
    return result;
  }

  @Override
  public void run() {
    var k = 1;
    for (var i = start; i < end; i++) {
      result += ((1.0 / (2 * i + 1)) * k);
      k *= -1;
    }
  }
}

public class PICounter {
  private final CyclicBarrier barrier;
  private final long p = 1000000000;
  private final ArrayList<Worker> workers = new ArrayList<>();

  public PICounter(int threadsCount, CyclicBarrier barrier) {
    this.barrier = barrier;
    var counter = 0L;

    for (var i = 0; i < threadsCount && counter < p; i++) {
      var end = counter + p / threadsCount;
      var worker = new Worker(counter, end);
      counter = end;
      workers.add(worker);
    }
  }

  public double Calculate() {
    workers.forEach(w -> {
      w.start();
      try {
        w.join();
      } catch (InterruptedException e) {
        System.out.println("Worker is dead");
      }
    });

    var res = 0d;
    for (var worker : workers) {
      res += worker.getResult();
    }

    return res;
  }

  public void stop() {

  }
}