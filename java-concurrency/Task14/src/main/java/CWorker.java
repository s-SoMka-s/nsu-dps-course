import java.util.concurrent.Semaphore;

public class CWorker implements Runnable {

  private final Semaphore components;

  public CWorker(Semaphore components) {
    this.components = components;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(300);
      System.out.println("Component - C was created.");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    components.release();
  }
}