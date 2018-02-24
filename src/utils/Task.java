package utils;

import java.util.concurrent.atomic.AtomicInteger;

public class Task {

  private static AtomicInteger nextTaskId = new AtomicInteger(0);


  final Location location;
  final Size size;
  final ColourVal colour;
  final int id;

  public Task(Location location, Size size,ColourVal colour) {

    this.location = location;
    this.size = size;
    this.colour = colour;
    this.id = nextTaskId.getAndIncrement();
  }




  public Location getLocation() {
    return location;
  }
  public Size getSize(){return size;}
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
