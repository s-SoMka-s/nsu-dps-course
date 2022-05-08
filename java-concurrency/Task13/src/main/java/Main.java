import java.util.concurrent.locks.ReentrantLock;

public class Main {

  public static void main(String[] args) {
    var philosophers = new Philosopher[5];
    var monitor = new Object();
    var forks = new ReentrantLock[5];

    for (int i = 0; i < forks.length; i++) {
      forks[i] = new ReentrantLock();
    }

    for (int i = 0; i < philosophers.length; i++) {
      var leftFork = forks[i % 5];
      var rightFork = forks[(i + 1) % 5];
      philosophers[i] = new Philosopher(leftFork, rightFork, monitor, i);
    }
    for (var philosopher : philosophers) {
      philosopher.start();
    }

    for (var philosopher : philosophers) {
      try {
        philosopher.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}