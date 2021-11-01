import java.util.concurrent.locks.ReentrantLock;

public class Philosopher extends Thread {

  private final ReentrantLock  leftFork;
  private final ReentrantLock  rightFork;
  private final Object monitor;
  int spaghettiLeft;
  int number;

  public Philosopher(ReentrantLock leftFork, ReentrantLock  rightFork, Object monitor, int number) {
    this.leftFork = leftFork;
    this.rightFork = rightFork;
    spaghettiLeft = 5;
    this.monitor = monitor;
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
      synchronized (monitor) {
        try {
          if (leftFork.tryLock()) {
            if (rightFork.tryLock()) {
              eat();
              leftFork.unlock();
              rightFork.unlock();
              monitor.notifyAll();
            }
            else {
              leftFork.unlock();
              monitor.wait();
            }
          }
          else {
            monitor.wait();
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}