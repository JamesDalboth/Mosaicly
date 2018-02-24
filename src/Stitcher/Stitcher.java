package Stitcher;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import picture.PicLoc;
import picture.Picture;
import picture.Utils;
import utils.Location;
import utils.Size;

public class Stitcher {

  private final int chunkLength;
  //  private Map pictures = new HashMap();
  private BufferedImage[][] grid; //Organised by where the pictures are supposed to go.
  private Size ApparentSize; //Corresponds to the size of the end picture.
  private Size ActualSize; //Corresponds to the resolution of the end picture.

  public Stitcher(Size size, int chunkLength) {
    this.ApparentSize = size;
    this.chunkLength = chunkLength;
    this.ActualSize = new Size(chunkLength * ApparentSize.height(),
        chunkLength * ApparentSize.width());
  }

  public static void main(String[] args) {
    int height = 10;
    int width = 20;
    Picture jesus = Utils.loadPicture("test_images/image0.jpg");
    Picture coke = Utils.loadPicture("test_images/image1.jpg");
    Random random = new Random();
    Stitcher stitcher = new Stitcher(new Size(height, width), 512);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (random.nextBoolean()) {
          stitcher.add(new PicLoc(jesus, new Location(i, j)));
        } else {
          stitcher.add(new PicLoc(coke, new Location(i, j)));
        }
      }
    }

    stitcher.run("test_images", "output");
  }

  public void add(PicLoc picloc) {
    Location loc = picloc.getLocation();
    grid[loc.getX()][loc.getY()] = picloc.getPicture().getImage();
  }

//
//  private BufferedImage[] readImages(String src) {
////    int imageCount = pictures.size();
//    int imageCount = 2;
//    BufferedImage images[] = new BufferedImage[imageCount];
//    for (int i = 0; i < images.length; i++) {
//      Path srcPath = Paths.get(src, "image" + i + ".jpg");
//      try {
//        images[i] = ImageIO.read(new File(srcPath.toString()));
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//    }
//
//    return images;
//  }

  private void run(String src, String dest) {

//    BufferedImage[] grid = readImages(src);
    BufferedImage target = new BufferedImage(ActualSize.width(),
        ActualSize.height(), BufferedImage.TYPE_INT_RGB);
    Graphics2D g = (Graphics2D) target.getGraphics();
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        System.out.println("Drawing image " + i);
        g.drawImage(grid[i][j], chunkLength * i, chunkLength * j, null);
      }
    }

    Path destPath = Paths.get(dest, "target.jpg");
    File output = new File(destPath.toString());

    try {
      ImageIO.write(target, "jpg", output);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
