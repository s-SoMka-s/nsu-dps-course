public class MyLinkedList {

  private final MyLinkedListNode head;
  private MyLinkedListNode curr;
  private int size;

  public MyLinkedList() {
    head = new MyLinkedListNode("This is head", null, null);
    curr = head;
    size = 0;
  }

  public void addFirst(String str) {
    synchronized (head) {
      var node = new MyLinkedListNode(str, head, head.getNext());
      head.setNext(node);
      head.getNext().setPrevious(node);
      size++;
    }
  }

  public MyLinkedListNode getByIndex(int index) {
    var curr = head.getNext();
    for (var i = 0; i < index; i++) {
      curr = curr.getNext();
    }

    return curr;
  }

  public void swap(MyLinkedListNode a, MyLinkedListNode b) {
    synchronized (head) {
      var aPrv = a.getPrevious();
      var aNext = a.getNext();
      var bPrv = b.getPrevious();
      var bNext = b.getNext();

      aPrv.setNext(b);
      bNext.setPrevious(a);

      aNext.setPrevious(b);
      bPrv.setNext(a);

      a.setNext(bNext);
      a.setPrevious(bPrv);
      b.setPrevious(aPrv);
      b.setNext(aNext);
    }
  }

  public MyLinkedListNode getHead() {
    return this.head;
  }

  public int getSize() {
    return this.size;
  }
}

