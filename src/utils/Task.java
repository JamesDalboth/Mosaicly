package utils;

import java.util.concurrent.atomic.AtomicInteger;

public class Task {

  private static AtomicInteger nextTaskId = new AtomicInteger(0);


  final Location location;
  final Size size;
  final ColourVal colour;
  final String seedWord;
  final int id;

  public Task(Location location, Size size, String seedWord) {

    this.location = location;
    this.size = size;
    this.seedWord = seedWord;
    this.id = nextTaskId.getAndIncrement();
  }




  public Location getLocation() {
    return location;
  }
  public Size getSize(){return size;}
  public String getSeedWord(){return seedWord;}
  public ColourVal getColour(){return colour;}




  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Task task = (Task) o;

    return id == task.id;
  }


  public int hashCode() {
    return id;
  }
}
