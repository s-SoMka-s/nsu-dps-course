import java.util.ArrayList;

public class PICounter {

  private final ArrayList<Worker> workers = new ArrayList<>();

  public PICounter(int threadsCount) {
    var start = 0L;
    long p = 1000000000;

    for (var i = 0; i < threadsCount && start < p; i++) {
      var end = start + p / threadsCount;
      var worker = new Worker(start, end);
      start = end;
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
}
