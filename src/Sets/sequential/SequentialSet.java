package Sets.sequential;

import Sets.baseSet;
import Sets.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SequentialSet<E> implements baseSet<E> {

  int size = 0;
  private Node<E> head, tail;

  public SequentialSet() {
    head = new SequentialNode<>(null, Integer.MIN_VALUE, null);
    tail = new SequentialNode<>(null, Integer.MAX_VALUE, null);
    head.setNext(tail);
  }

  private Position<E> find(Node<E> start, int key) {
    Node<E> pred, curr;
    curr = start;
    do {
      pred = curr;
      curr = curr.next();
    } while (curr.key() < key);  // until curr.key >= key
    return new Position<E>(pred, curr);
  }

  @Override
  public boolean contains(E item) {
    Node<E> node = new SequentialNode<>(item);
    Position<E> expectedPosition = find(head, node.key());

    return expectedPosition.curr.key() == node.key();
  }

  @Override
  public boolean add(E item) {
    Node<E> node = new SequentialNode<>(item);
    Position<E> where = find(head, node.key());
    if (where.curr.key() == node.key()) {
      return false;
    } else {
      node.setNext(where.curr);
      where.pred.setNext(node);
      size += 1;
      return true;
    }
  }

  @Override
  public boolean remove(E item) {
    Node<E> node = new SequentialNode<>(item);
    Position<E> where = find(head, node.key());
    if (where.curr.key() > node.key()) {
      return false;
    } else {
      where.pred.setNext(where.curr.next());
      size -= 1;
      return true;
    }
  }
  public Optional<E> poll(){
    Node<E> first = head.next();
    if(first == tail){
      return Optional.empty();
    }
    head.setNext(first.next());
    size -=1;
    return Optional.of(first.item());
  }


  public List<E> toList(){
    LinkedList<E> toReturn = new LinkedList<>();
    Node<E> curr;
    curr = head.next();
    while(curr!=tail){
      toReturn.addFirst(curr.item());
      curr = curr.next();
    }
    return toReturn;
  }

  @Override
  public int size() {
    return size;
  }


  private static class Position<T> {

    public final Node<T> pred, curr;

    public Position(Node<T> pred, Node<T> curr) {
      this.pred = pred;
      this.curr = curr;
    }
  }
}
