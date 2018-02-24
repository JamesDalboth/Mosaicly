package utils;

public class Size {

  private final int height;
  private final int width;

  public Size(int height, int width) {
    this.height = height;
    this.width = width;
  }

  public int height() {
    return height;
  }

  public int width() {
    return width;
  }

  @Override
  public String toString() {
    return "H: " + height + " | " + "W: " + width;
  }
}
