package Classification;

import picture.PicLoc;
import picture.Picture;
import picture.Utils;
import search.BingImageSearch;
import search.ImageSearch;
import utils.*;
import Stitcher.Stitcher;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class Core {

    private Picture initImage;
    private final int noScavengers;
    private List<Scavenger> scavengers;
    private List<Worker> workers;
    private final int tileSize = 25;
    private final int noWorkers;
    private String seedWord;
    private static Map<ColourVal.SearchColour, Picture> imageMap;

    public Core(Picture initImage, int noScavengers, String seedWord, int noWorkers) {
        imageMap = new HashMap<>();
        this.seedWord = seedWord;
        this.initImage = initImage;
        this.noScavengers = noScavengers;
        scavengers = new ArrayList<>();
        workers = new ArrayList<>();
        Backlog backlog = new coarseBacklog();
        this.noWorkers = noWorkers;
        Stitcher stitcher = new Stitcher(new Size(initImage.getWidth(), initImage.getHeight()), tileSize);
        for (int i = 0; i < noScavengers; i++) {
            Scavenger scavenger = new Scavenger(i, initImage, backlog, tileSize, noScavengers);
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
            Worker worker = new Worker((coarseBacklog) backlog, stitcher);
            workers.add(worker);
            worker.start();
        }

        while (backlog.numberOfTasksInTheBacklog() != 0) {
        }

        for (Worker worker : workers) {
            worker.interrupt();
        }

        stitcher.run("test.png");


    }

    public void scan() {
        for (ColourVal.SearchColour searchColour : ColourVal.SearchColour.values()) {
            ImageSearch imageSearch = new BingImageSearch("7a4819d6134a4c3c860bf5bfd15ec1ef", searchColour.toString(),
                    seedWord);

            String imageUrl = imageSearch.getImageUrl();
            Picture picture = Utils.loadPicture(imageUrl);
            Image scaledImage = picture.getImage().getScaledInstance(
                    tileSize,
                    tileSize, Image.SCALE_DEFAULT);
            Picture scaled = new Picture(Utils.toBufferedImage(scaledImage));
            imageMap.put(searchColour,scaled);
        }
    }

    public static Picture colorToPic(ColourVal.SearchColour sc) {
        return imageMap.get(sc);
    }
}
