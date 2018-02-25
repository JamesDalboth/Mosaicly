package Classification;

import Sets.optimistic.OptimisticBacklog;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
  private final int tileSize = 10;
  private final int noWorkers;
  private final int scale = 2;
  private static Map<ColourVal.SearchColour, Picture> imageMap;
  private static List<BufferedImage> outputs;

  public Core(int noWorkers, int noScavengers) {
      this.noWorkers = noWorkers;
      this.noScavengers = noScavengers;
  }

  public void run(Picture[] images) {
    this.images = images;
    scavengers = new ArrayList<>();
    workers = new ArrayList<>();
    Backlog backlog = new OptimisticBacklog();
    outputs = new ArrayList<>();

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
      System.out.println("Pictures Processed - " + (outputs.size()*100 / images.length));
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

  public void scan(String seedWord) {
      imageMap = new HashMap<>();
    ExecutorService exec = Executors.newFixedThreadPool(3);
    try {
      for (ColourVal.SearchColour searchColour : ColourVal.SearchColour.values()) {
        exec.submit(new Runnable() {
          @Override
          public void run() {
            ImageSearch imageSearch = new BingImageSearch(
                "90767059c1304b17b4477f7428311666", searchColour.toString(),
                seedWord);

            Picture picture = imageSearch.getImageUrl();

            Image scaledImage = picture.getImage().getScaledInstance(
                tileSize * scale,
                tileSize * scale, Image.SCALE_DEFAULT);
            Picture scaled = new Picture(Utils.toBufferedImage(scaledImage));
            imageMap.put(searchColour, scaled);
            System.out.println(searchColour.toString() + " Image found");
            try {
              // bing only allows 3 transactions per second
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        });
      }
    } finally {
      exec.shutdown();
    }
  }

  public static Picture colorToPic(ColourVal.SearchColour sc) {
    return imageMap.get(sc);
  }
}
