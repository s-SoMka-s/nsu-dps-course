public class MyLinkedListNode implements Comparable<MyLinkedListNode> {

  private String str;
  private MyLinkedListNode prev;
  private MyLinkedListNode next;

  public MyLinkedListNode(String str, MyLinkedListNode prev, MyLinkedListNode next) {
    this.str = str;
    this.next = next;
    this.prev = prev;
  }

  public void setNext(MyLinkedListNode n) {
    this.next = n;
  }

  public void setPrevious(MyLinkedListNode p) {
    this.prev = p;
  }

  public MyLinkedListNode getNext() {
    return this.next;
  }

  public MyLinkedListNode getPrevious() {
    return this.prev;
  }

  public String getMsg() {
    return this.str;
  }

  @Override
  public int compareTo(MyLinkedListNode o) {
    if (o == null) {
      return 1;
    }
    return this.getMsg().compareTo(o.getMsg());
  }
}
