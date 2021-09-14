public class Task1 {
  public static void main(String[] args) {
    new Thread(Task1::printSomething).start();
    printSomething();
  }

  private static void printSomething() {
    for (var i = 0; i < 10; i++){
      System.out.println((i+1) + ": string");
    }
  }
}
