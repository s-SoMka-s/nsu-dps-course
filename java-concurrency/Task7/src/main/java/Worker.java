public class Worker extends Thread {

  private final long start;
  private final long end;
  private double result = 0;

  public Worker(long start, long end) {
    this.start = start;
    this.end = end;
  }

  public double getResult() {
    return result;
  }

  @Override
  public void run() {
    var k = 1;
    for (var i = start; i < end; i++) {
      result += ((1.0 / (2 * i + 1)) * k);
      k *= -1;
    }
  }
}
