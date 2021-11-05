import java.util.Collections;

public class Sorter extends Thread {

  final MyLinkedList list;

  public Sorter(MyLinkedList list) {
    this.list = list;
  }

  public void sort() {
    if (list.getSize() < 2) {
      return;
    }

    for (int i = 0; i < list.getSize(); i++) {
      for (int j = i; j < list.getSize(); j++) {
        var c1 = list.getByIndex(i);
        var c2 = list.getByIndex(j);
        if (c1.compareTo(c2) > 0) {
          list.swap(c1, c2);
        }
      }
    }
  }

  @Override
  public void run() {
    while (!Thread.interrupted()) {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        break;
      }
      synchronized (list.getHead()) {
        this.sort();
      }
    }
  }
}