import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

  public static void main(String[] args) {
    var isChild = new AtomicBoolean(false);
    Runnable task = new Child(isChild);
    new Thread(task).start();

    for (var i = 0; i < 10; i++) {
      synchronized (isChild) {
        try {
          while (isChild.get())
          {
            isChild.wait();
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("parent prints " + (i + 1) + ": string");
        isChild.set(true);
        isChild.notify();
      }
    }
  }
}
