import java.util.concurrent.TimeUnit;

public class Task4 {
  public static void main(String[] args) throws InterruptedException {
    var child = new Thread(() -> {
      while(!Thread.currentThread().isInterrupted()) {
        System.out.println("I am working...");
      }

      System.out.println("I was interrupted(");
    });

    child.start();
    TimeUnit.SECONDS.sleep(2);
    child.interrupt();
  }
}
