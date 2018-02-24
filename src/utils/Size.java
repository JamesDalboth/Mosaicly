package utils;

public class Size {

  private final int height;
  private final int width;

  public Size() {
    this(1, 1);
  }

  public Size(int height, int width) {
    assert height >= 1 : "Invalid height.";
    assert width >= 1 : "Invalid width.";
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
