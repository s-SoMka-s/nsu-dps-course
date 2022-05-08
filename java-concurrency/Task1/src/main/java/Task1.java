public class Task1 {
  public static void main(String[] args) {
    Runnable task = () -> printSomething("child");
    new Thread(task).start();
    printSomething("parent");
  }

  private static void printSomething(String caller) {
    for (var i = 0; i < 10; i++){
      System.out.println(caller + " prints " +  (i+1) + ": string");
    }
  }
}
