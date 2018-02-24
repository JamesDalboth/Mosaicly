package Sets.sequential;


import Sets.baseNode;

public class SequentialNode<E> implements baseNode<E> {
  
  private E item;
  private int key;
  private baseNode<E> next;

  public SequentialNode(E item) {
    this(item, null);
  }

  public SequentialNode(E item, baseNode<E> next) {
    this(item, item.hashCode(), next);
  }

  protected SequentialNode(E item, int key, baseNode<E> next) {
    this.item = item;
    this.key = key;
    this.next = next;
  }

  @Override
  public E item() {
    return item;
  }

  @Override
  public int key() {
    return key;
  }

  @Override
  public baseNode<E> next() {
    return next;
  }

  @Override
  public void setItem(E item) {
    this.item = item;
  }

  @Override
  public void setKey(int key) {
    this.key = key;
  }

  @Override
  public void setNext(baseNode<E> next) {
    this.next = next;
  }
}
