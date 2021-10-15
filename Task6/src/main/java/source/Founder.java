package source;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public final class Founder {

  private final List<Runnable> workers;
  private final CyclicBarrier barrier;

  public Founder(final Company company) {
    workers = new ArrayList<>(company.getDepartmentsCount());
    barrier = new CyclicBarrier(company.getDepartmentsCount() + 1);

    for (var i = 0; i < company.getDepartmentsCount(); i++) {
      workers.add(new Worker(company.getFreeDepartment(i), barrier));
    }
  }


  public void start() throws BrokenBarrierException, InterruptedException {
    for (final Runnable worker : workers) {
      new Thread(worker).start();
    }

    barrier.await();
  }
}