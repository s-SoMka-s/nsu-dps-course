import java.util.concurrent.Semaphore;

public class BWorker implements Runnable {

  private final Semaphore components;

  public BWorker(Semaphore components) {
    this.components = components;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Component - B was created.");
    components.release();
  }
}