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
    switch(nextTask.command){
      case COLSEARCH:
        //Harry's request of colour goes here.
        break;
      case REQUEST:
        //James' request code goes here
        break;
    }
  }
  private void SLEEP(){
    try {
      Thread.sleep(toSleep);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
