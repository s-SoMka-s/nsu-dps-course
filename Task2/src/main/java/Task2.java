public class Task2 {
  public static void main(String[] args) throws InterruptedException {
    var child = new Thread(Task2::printSomething);
    child.start();
    child.join();
    printSomething();
  }

  private static void printSomething() {
    for (var i = 0; i < 10; i++){
      System.out.println((i+1) + ": string");
    }
  }
}
