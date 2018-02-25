package Classification;

import Sets.optimistic.OptimisticBacklog;
import picture.Picture;
import picture.Utils;
import search.BingImageSearch;
import search.ImageSearch;
import utils.*;
import Stitcher.Stitcher;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Core {

  private Picture[] images;
  private final int noScavengers;
  private List<Scavenger> scavengers;
  private List<Worker> workers;
  private final int tileSize = 5;
  private final int noWorkers;
  private String seedWord;
  private final int scale = 2;
  private static Map<ColourVal.SearchColour, Picture> imageMap;
  private static List<BufferedImage> outputs;

  public Core(Picture[] images, int noScavengers, String seedWord,
              int noWorkers) {
    imageMap = new HashMap<>();
    this.seedWord = seedWord;
    this.images = images;
    this.noScavengers = noScavengers;
    scavengers = new ArrayList<>();
    workers = new ArrayList<>();
    Backlog backlog = new OptimisticBacklog();
    this.noWorkers = noWorkers;
    outputs = new ArrayList<>();

    scan();
    System.out.println("Scan completed");
    for (Picture pic : images) {
      Stitcher stitcher = new Stitcher(
              new Size(pic.getHeight(), pic.getWidth()), tileSize, scale);
      for (int i = 0; i < noScavengers; i++) {
        Scavenger scavenger = new Scavenger(i, pic, backlog, tileSize,
                noScavengers);
        scavengers.add(scavenger);
        scavenger.start();
      }

      for (Scavenger scavenger : scavengers) {
        try {
          scavenger.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      for (int i = 0; i < noWorkers; i++) {
        Worker worker = new Worker(backlog, stitcher);
        workers.add(worker);
        worker.start();
      }

      while (backlog.numberOfTasksInTheBacklog() != 0) {
      }

      for (Worker worker : workers) {
        worker.interrupt();
      }
      outputs.add(stitcher.run());
    }
    try {
      if (outputs.size() > 1) {
        // grab the output image type from the first image in the sequence
        BufferedImage firstImage = outputs.get(0);

        // create a new BufferedOutputStream with the last argument
        ImageOutputStream output =
                new FileImageOutputStream(new File("output/target.gif"));

        // create a gif sequence with the type of the first image, 1 second
        // between frames, which loops continuously
        GifSequenceWriter writer =
                new GifSequenceWriter(output, firstImage.getType(), 1, true);

        // write out the first image to our sequence...
        writer.writeToSequence(firstImage);
        for (int i = 1; i < outputs.size(); i++) {
          BufferedImage nextImage = outputs.get(i);
          writer.writeToSequence(nextImage);
        }

        writer.close();
        output.close();
      } else {
        Path destPath = Paths.get("output", "target.jpg");
        File output = new File(destPath.toString());
        ImageIO.write(outputs.get(0), "jpg", output);
      }


    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void scan() {
    for (ColourVal.SearchColour searchColour : ColourVal.SearchColour
        .values()) {
      ImageSearch imageSearch = new BingImageSearch(
          "7a4819d6134a4c3c860bf5bfd15ec1ef", searchColour.toString(),
          seedWord);

      Picture picture = imageSearch.getImageUrl();

      Image scaledImage = picture.getImage().getScaledInstance(
          tileSize * scale,
          tileSize * scale, Image.SCALE_DEFAULT);
      Picture scaled = new Picture(Utils.toBufferedImage(scaledImage));
      imageMap.put(searchColour, scaled);
    }
  }

  public static Picture colorToPic(ColourVal.SearchColour sc) {
    return imageMap.get(sc);
  }
}
