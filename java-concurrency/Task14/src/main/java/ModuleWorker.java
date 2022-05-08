import java.util.concurrent.Semaphore;

public class ModuleWorker implements Runnable {

  private final Semaphore curComponents;
  private final Semaphore aComponents;
  private final Semaphore bComponents;

  public ModuleWorker(Semaphore curComponents, Semaphore aComponents, Semaphore bComponents) {
    this.curComponents = curComponents;
    this.aComponents = aComponents;
    this.bComponents = bComponents;
  }

  @Override
  public void run() {
    try {
      aComponents.acquire();
      bComponents.acquire();
      Thread.sleep(500);
      System.out.println("module was created.");
      curComponents.release();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
