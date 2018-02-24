package picture;

import java.awt.image.BufferedImage;
import utils.Location;

public class PicLoc {

  /**
   * Construct a new Picture object from the specified image.
   *
   * @param image the internal representation of the image.
   */

  private final Location location;
  private final Picture picture;

  public PicLoc(Picture picture, Location location) {
    this.picture = picture;
    this.location = location;
  }

  public Picture getPicture() {
    return picture;
  }

  public Location getLocation() {
    return location;
  }
}
