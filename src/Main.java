import Classification.Core;
import Classification.GifDecoder;
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
      GifDecoder gd = new GifDecoder();
      gd.read(location);

      result = new Picture[gd.getFrameCount()];
      for (int i = 0, count = gd.getFrameCount(); i < count; i++)
      {
          BufferedImage image = gd.getFrame(i);
          result[i] = new Picture(image);
      }
      return result;
  }
}

