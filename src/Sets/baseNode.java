package Sets;

public interface baseNode<T> {

  T item();

  int key();

  baseNode<T> next();

  void setItem(T item);

  void setKey(int key);

  void setNext(baseNode<T> next);
}