package utils;

import java.util.Optional;

public class Worker extends Thread {

  private final coarseBacklog backlog;
  private boolean interrupted = false;
  private int toSleep = 1;

  public Worker(coarseBacklog backlog) {
    this.backlog = backlog;
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
    //Needs to initiate a google search for pictures using searchterm seedWord and by the colour stored in  col
    //
    }

  private void SLEEP(){
    try {
      Thread.sleep(toSleep);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
