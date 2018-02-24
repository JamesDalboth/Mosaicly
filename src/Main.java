import Classification.Core;
import picture.Picture;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {
    String inputFile = args[0];
    String seedword = args[1];

    Picture[] pics = decomposeGif(inputFile);

    Core core = new Core(pics, 5, seedword, 10);
    System.out.println("Done");
  }

  public static Picture[] decomposeGif(String location) throws IOException {
      Picture[] result = new Picture[0];
      if (!location.endsWith(".gif")) {
          result = new Picture[1];
          result[0] = picture.Utils.loadPicture(location);
      }
      ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
      ImageInputStream in = ImageIO.createImageInputStream(new File(location));
      reader.setInput(in);
      result = new Picture[reader.getNumImages(true)];
      for (int i = 0, count = reader.getNumImages(true); i < count; i++)
      {
          BufferedImage image = reader.read(i);
          result[i] = new Picture(image);
      }
      return result;
  }
}

