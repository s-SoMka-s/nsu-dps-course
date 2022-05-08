package source;

import java.util.concurrent.BrokenBarrierException;

public class Main {
  public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
    var company = new Company(7);
    var founder = new Founder(company);
    founder.start();
    company.showCollaborativeResult();
  }
}
