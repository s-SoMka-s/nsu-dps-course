import java.util.concurrent.Semaphore;

public class Child implements Runnable {

  private final Semaphore parentTurn;
  private final Semaphore childTurn;

  public Child(Semaphore parentTurn, Semaphore childTurn) {
    this.parentTurn = parentTurn;
    this.childTurn = childTurn;
  }


  @Override
  public void run() {
    for (var i = 0; i < 10; i++) {
      try {
        childTurn.acquire();
        System.out.println("child prints " + (i + 1) + ": string");
        parentTurn.release();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}