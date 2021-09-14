public class Task4 {
  public static void main(String[] args) {
    var milliSecToWait = 2 * 1000;
    var child = new Thread(() -> {
      try {
        System.out.println("I was started");
        Thread.sleep(milliSecToWait);
      } catch (InterruptedException e) {
        System.out.println("I was interrupted");
      }
    });

    child.start();
    child.interrupt();
  }
}
