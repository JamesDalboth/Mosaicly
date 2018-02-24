package socialnetwork.domain;

import java.util.Optional;
import socialnetwork.domain.Task.Command;

public class Worker extends Thread {

  private final Backlog backlog;
  private boolean interrupted = false;
  private int toSleep = 1;

  public Worker(Backlog backlog) {
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
      case DELETE:
       if (!nextTask.getBoard().deleteMessage(nextTask.message)) {
         this.backlog.add(new Task(Command.DELETE,nextTask.getMessage(),nextTask.getBoard()));
       };

        break;
      case POST:
        nextTask.getBoard().addMessage(nextTask.message);
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
