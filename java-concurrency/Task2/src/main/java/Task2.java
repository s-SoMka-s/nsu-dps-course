public class Task2 {
  public static void main(String[] args) throws InterruptedException {
    Runnable task = () -> printSomething("child");

    var child = new Thread(task);
    child.start();
    child.join();

    printSomething("parent");
  }

  private static void printSomething(String caller) {
    for (var i = 0; i < 10; i++) {
      System.out.println(caller + " prints " + (i + 1) + ": string");
    }
  }
}
