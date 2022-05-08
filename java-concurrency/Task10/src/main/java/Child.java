import java.util.concurrent.atomic.AtomicBoolean;

public class Child implements Runnable {

  private final AtomicBoolean isChild;

  public Child(AtomicBoolean isChild) {
    this.isChild = isChild;
  }


  @Override
  public void run() {
    for (var i = 0; i < 10; i++) {
      synchronized (isChild) {
        try {
          while (!isChild.get())
          {
            isChild.wait();
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("child prints " + (i + 1) + ": string");
        isChild.set(false);
        isChild.notify();
      }
    }
  }
}
