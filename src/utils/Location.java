package utils;

public class Location {
  // 0,0 is top left.

  private final int x;
  private final int y;

  public Location() {
    this(0, 0);
  }

  public Location(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public String toString() {
    return "(" + x + "," + y + ")";
  }
}
