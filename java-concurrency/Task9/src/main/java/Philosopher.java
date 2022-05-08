import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {

  final Semaphore leftFork;
  final Semaphore rightFork;
  int spaghettiLeft;
  int number;

  public Philosopher(Semaphore leftFork, Semaphore rightFork, int number) {
    this.leftFork = leftFork;
    this.rightFork = rightFork;
    spaghettiLeft = 5;
    this.number = number;
  }

  private void think() {
    try {
      System.out.format("Philosopher %d is thinking\n", number);
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void eat() {
    try {
      System.out.format("Philosopher %d is eating\n", number);
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    spaghettiLeft -= 1;
  }

  @Override
  public void run() {
    while (spaghettiLeft > 0) {
      think();
      try {
        leftFork.acquire();
        rightFork.acquire();
        eat();
        leftFork.release();
        rightFork.release();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
