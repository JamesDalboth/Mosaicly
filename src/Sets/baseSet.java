package Sets;

public interface baseSet<E> {

  boolean add(E item);

  boolean remove(E item);

  boolean contains(E item);

  int size();
}
