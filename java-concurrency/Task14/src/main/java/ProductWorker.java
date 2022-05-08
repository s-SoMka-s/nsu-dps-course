import java.util.concurrent.Semaphore;

public class ProductWorker implements Runnable {

  private final Semaphore curComponents;
  private final Semaphore cComponents;
  private final Semaphore modules;

  public ProductWorker(Semaphore curComponents, Semaphore cComponents, Semaphore modules) {
    this.curComponents = curComponents;
    this.cComponents = cComponents;
    this.modules = modules;
  }

  @Override
  public void run() {
    try {
      cComponents.acquire();
      modules.acquire();
      Thread.sleep(500);
      System.out.println("Widget was created.");
      curComponents.release();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}