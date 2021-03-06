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

import Classification.Core;
import picture.PicLoc;
import picture.Picture;
import picture.Utils;
import utils.Location;
import utils.Size;

public class Stitcher {

  private final int chunkLength;
  //  private Map pictures = new HashMap();
  private BufferedImage[][] grid; //Organised by where the pictures are supposed to go.
  private Size ActualSize; //Corresponds to the resolution of the end picture.
  private final int scale;

  public Stitcher(Size size, int chunkLength, int scale) {
    this.grid = new BufferedImage[size.height()/chunkLength][size.width()/chunkLength];
    this.chunkLength = chunkLength;
    this.ActualSize = size;
    this.scale = scale;
  }

  public void add(PicLoc picloc) {
    Location loc = picloc.getLocation();
    grid[loc.getY()/chunkLength][loc.getX()/chunkLength] = picloc.getPicture().getImage();
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

  public BufferedImage run() {

//    BufferedImage[] grid = readImages(src);
    BufferedImage target = new BufferedImage(ActualSize.width()*scale,
        ActualSize.height()*scale, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = (Graphics2D) target.getGraphics();
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        g.drawImage(grid[i][j], chunkLength*scale * j, chunkLength*scale * i, null);
      }
    }
    return target;
  }

}
