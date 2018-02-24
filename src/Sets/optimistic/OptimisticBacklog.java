package Sets.optimistic;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import utils.Backlog;
import utils.Task;
import Sets.Node;

public class OptimisticBacklog implements Backlog {

  private AtomicInteger numberOfTasksInTheBacklog = new AtomicInteger(0);
  private ReadWriteNode<Task> head =
      new ReadWriteNode(null, Integer.MIN_VALUE, null);
  private ReadWriteNode<Task> tail =
      new ReadWriteNode(null, Integer.MAX_VALUE, null);

  public OptimisticBacklog() {
    head.setNext(tail);
  }

  private boolean valid(Node<Task> pred, Node<Task> curr) {
    Node<Task> node = head;
    while (node.key() <= pred.key()) {
      if (node == pred) {
        return pred.next() == curr;
      }
      node = node.next();
    }
    return false;
  }

  private Position<Task> find(ReadWriteNode<Task> start, int key) {
    ReadWriteNode<Task> pred;
    ReadWriteNode<Task> curr = start;
    do {
      pred = curr;
      curr = curr.next();
    } while (curr.key() < key);

    return new Position(pred, curr);
  }

  @Override
  public boolean add(Task task) {
    ReadWriteNode<Task> node = new ReadWriteNode<>(task);
    do {
      Position<Task> position = find(head, node.key());
      ReadWriteNode<Task> pred = position.pred;
      ReadWriteNode<Task> curr = position.curr;
      pred.lock();
      curr.lock();
      try {
        if (valid(pred, curr)) {
          if (position.curr.key() == node.key()) {
            return false;
          } else {
            node.setNext(position.curr);
            position.pred.setNext(node);
            numberOfTasksInTheBacklog.incrementAndGet();
            return true;
          }
        }
      } finally {
        pred.unlock();
        curr.unlock();
      }
    } while (true);
  }

  @Override
  public Optional<Task> getNextTaskToProcess() {
    do {
      ReadWriteNode<Task> nodeToRemove = head.next();
      head.lock();
      nodeToRemove.lock();
      try {
        if (numberOfTasksInTheBacklog.get() == 0) {
          return Optional.empty();
        } else {
          if (valid(head, nodeToRemove)) {
            head.setNext(nodeToRemove.next());
            numberOfTasksInTheBacklog.decrementAndGet();
            return Optional.of(nodeToRemove.item());
          }
        }
      } finally {
        head.unlock();
        nodeToRemove.unlock();
      }
    } while (true);
  }

  @Override
  public int numberOfTasksInTheBacklog() {
    return numberOfTasksInTheBacklog.get();
  }

  protected static class Position<T> {

    public final ReadWriteNode<T> pred, curr;

    public Position(ReadWriteNode<T> pred, ReadWriteNode<T> curr) {
      this.pred = pred;
      this.curr = curr;
    }
  }

}
