package Sets.coarsegrained;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Sets.sequential.SequentialSet;

public class CoarseSet<E> extends SequentialSet<E> {

  private Lock lock = new ReentrantLock();

  @Override
  public boolean contains(E item) {
    lock.lock();
    try {
      return super.contains(item);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean add(E item) {
    lock.lock();
    try {
      return super.add(item);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean remove(E item) {
    lock.lock();
    try {
      return super.remove(item);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public List<E> toList() {
    lock.lock();
    try {
      return super.toList();
    } finally {
      lock.unlock();
    }

  }

  @Override
  public Optional<E> poll() {
    lock.lock();
    try {
      return super.poll();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int size() {
    lock.lock();
    try {
      return super.size();
    } finally {
      lock.unlock();
    }
  }
}
