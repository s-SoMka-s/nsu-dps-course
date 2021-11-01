import java.util.concurrent.Semaphore;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    var parentTurn = new Semaphore(1);
    var childTurn = new Semaphore(0);
    Runnable task = new Child(parentTurn, childTurn);
    new Thread(task).start();

    for (var i = 0; i < 10; i++) {
      parentTurn.acquire();
      System.out.println("parent prints " + (i + 1) + ": string");
      childTurn.release();
    }
  }
}