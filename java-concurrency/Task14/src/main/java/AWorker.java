import java.util.concurrent.Semaphore;

public class AWorker implements Runnable {

  private final Semaphore components;

  public AWorker(Semaphore components) {
    this.components = components;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Component - A was created.");
    components.release();
  }
}
