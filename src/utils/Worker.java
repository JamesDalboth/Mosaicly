package utils;

import Stitcher.Stitcher;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Optional;
import picture.PicLoc;
import picture.Picture;
import picture.Utils;
import search.BingImageSearch;
import search.ImageSearch;

public class Worker extends Thread {

  private final coarseBacklog backlog;
  private final Stitcher stitcher;
  private boolean interrupted = false;
  private int toSleep = 1;

  public Worker(coarseBacklog backlog, Stitcher stitcher) {
    this.backlog = backlog;
    this.stitcher = stitcher;
  }

  @Override
  public void run() {
    while (!interrupted) {
      if (backlog.numberOfTasksInTheBacklog() > 0) {
        Optional<Task> toDo = backlog.getNextTaskToProcess();
        if (toDo.isPresent()) {
          process(toDo.get());

        } else {
          SLEEP();
        }
      } else{
        SLEEP();
      }
    }
  }

  public void interrupt() {
    this.interrupted = true;
  }

  public void process(Task nextTask) {
    String searchColour = nextTask.colour.getSearchByColour();
    String seedWord = nextTask.seedWord;

    ImageSearch imageSearch = new BingImageSearch("", searchColour,
        seedWord);

    String imageUrl = imageSearch.getImageUrl();
    Picture picture = Utils.loadPicture(imageUrl);
    Image scaledImage = picture.getImage().getScaledInstance(
        nextTask.getSize().width(),
        nextTask.getSize().height(), Image.SCALE_DEFAULT);

    stitcher.add(new PicLoc(new Picture((BufferedImage) scaledImage),
        nextTask.location));
  }

  private void SLEEP(){
    try {
      Thread.sleep(toSleep);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
