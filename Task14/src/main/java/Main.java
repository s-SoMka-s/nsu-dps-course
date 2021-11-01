import java.util.concurrent.Semaphore;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    var aComponents = new Semaphore(0);
    var bComponents = new Semaphore(0);
    var modules = new Semaphore(0);
    var cComponents = new Semaphore(0);
    var products = new Semaphore(0);

    while(true) {
      var workers = new Thread[5];
      workers[0] = new Thread(new AWorker(aComponents));
      workers[1] = new Thread(new BWorker(bComponents));
      workers[2] = new Thread(new CWorker(cComponents));
      workers[3] = new Thread(new ModuleWorker(modules, aComponents, bComponents));
      workers[4] = new Thread(new ProductWorker(products, cComponents, modules));

      for (var worker : workers) {
        worker.start();
      }

      for (var worker : workers) {
        worker.join();
      }
    }
  }
}
