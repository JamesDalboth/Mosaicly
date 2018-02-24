package socialnetwork.domain;

import java.util.Optional;
import Sets.coarsegrained.CoarseSet;
public class coarseBacklog implements Backlog {

  CoarseSet<Task> toProcess = new CoarseSet<>();
  @Override
  public boolean add(Task task) {
    return toProcess.add(task);
  }

  public Optional<Task> getNextTaskToProcess(){
    if(toProcess.size()>0){

      return toProcess.poll();
    }
    return Optional.empty();
  }

  @Override
  public int numberOfTasksInTheBacklog() {
    return toProcess.size();
  }
}
