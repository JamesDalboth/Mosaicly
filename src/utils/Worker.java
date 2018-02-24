package utils;

import Classification.Core;
import Sets.optimistic.OptimisticBacklog;
import Stitcher.Stitcher;
import java.util.Optional;
import picture.PicLoc;
import picture.Picture;

public class Worker extends Thread {

  private final Backlog backlog;
  private final Stitcher stitcher;
  private boolean interrupted = false;
  private int toSleep = 1;

  public Worker(Backlog backlog, Stitcher stitcher) {
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
    Picture picture = Core.colorToPic(nextTask.colour.searchByColour);
    PicLoc pl = new PicLoc(picture,nextTask.location);
    stitcher.add(pl);
  }

  private void SLEEP(){
    try {
      Thread.sleep(toSleep);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
