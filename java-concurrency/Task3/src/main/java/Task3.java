public class Task3 {
  public static void main(String[] args) {
    var strings = new String[]{"string 1", "string 2"};
    for (var i = 0; i < 4; i++) {
      var threadNum = i;
      new Thread(() -> printStrings(threadNum, strings.clone())).start();
    }
  }

  private static void printStrings(int threadNumber, String[] strings) {
    for (var str : strings) {
      System.out.println("Thread " + threadNumber + ": " + str);
    }
  }
}
