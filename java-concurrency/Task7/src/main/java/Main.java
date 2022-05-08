public class Main {
  public static void main(String args[]) {
    var threadsCount = 20;
    if (threadsCount < 1) {
      throw new IllegalArgumentException("Thread count couldn't be less then 1");
    }

    var piCounter = new PICounter(threadsCount);
    var res = piCounter.Calculate();
    System.out.println(res);
  }
}
