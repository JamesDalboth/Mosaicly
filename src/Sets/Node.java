package Sets;

public interface Node<T> {

  T item();

  int key();

  Node<T> next();

  void setItem(T item);

  void setKey(int key);

  void setNext(Node<T> next);
}