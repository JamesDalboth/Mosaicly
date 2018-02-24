package Classification;

import picture.Picture;
import utils.Backlog;
import utils.Worker;
import utils.coarseBacklog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Core {

    private Picture initImage;
    private final int noScavengers;
    private List<Scavenger> scavengers;
    private List<Worker> workers;
    private final int tileSize = 10;
    private final int noWorkers;
    private String seedWord;

    public Core(Picture initImage, int noScavengers, String seedWord, int noWorkers) {
        this.seedWord = seedWord;
        this.initImage = initImage;
        this.noScavengers = noScavengers;
        scavengers = new ArrayList<>();
        workers = new ArrayList<>();
        Backlog backlog = new coarseBacklog();
        this.noWorkers = noWorkers;
        for (int i = 0; i < noScavengers; i++) {
            Scavenger scavenger = new Scavenger(i,initImage,backlog,tileSize,noScavengers,seedWord);
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
            Worker worker = new Worker((coarseBacklog) backlog);
            workers.add(worker);
            worker.start();
        }

        while (backlog.numberOfTasksInTheBacklog() != 0) {
        }

        for (Worker worker : workers) {
            worker.interrupt();
        }


    }




}
