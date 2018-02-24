package Classification;

import picture.Color;
import picture.Picture;
import picture.Utils;
import utils.*;

public class Scavenger extends Thread{
    private int id;
    private Picture picture;
    private Backlog backlog;
    private int tileSize;
    private int scavengerNo;
    private String seedWord;
    public Scavenger(int id, Picture picture, Backlog backlog, int tileSize, int scavengerNo, String seedWord) {
        this.backlog = backlog;
        this.picture = picture;
        this.id = id;
        this.tileSize = tileSize;
        this.scavengerNo = scavengerNo;
        this.seedWord = seedWord;
    }

    @Override
    public void run() {
        int count = id;
        while (count < (picture.getWidth() * picture.getHeight()) / (tileSize * tileSize)) {
            Picture picture = Utils.createPicture(tileSize, tileSize);
            int xOffset = count / picture.getWidth();
            int yOffset = count % picture.getWidth();
            Location location = new Location();
            location.setX(xOffset);
            location.setY(yOffset);
            for (int i = 0;  i < tileSize; i++) {
                for (int j = 0; j < tileSize; j++) {
                    picture.setPixel(i,j,this.picture.getPixel(i + xOffset,j + yOffset));
                }
            }
            ColourVal colourVal = new ColourVal(averageColor(picture));
            System.out.println();
            backlog.add(new Task(location,new Size(tileSize,tileSize),seedWord,colourVal));
            count += scavengerNo;
        }
    }

    public Color averageColor(Picture picture) {
        int rsum = 0;
        int gsum = 0;
        int bsum = 0;

        for (int i = 0; i < picture.getWidth(); i++) {
            for (int j = 0; j < picture.getHeight(); j++) {
                rsum += picture.getPixel(i,j).getRed();
                gsum += picture.getPixel(i,j).getRed();
                bsum += picture.getPixel(i,j).getRed();
            }
        }

        rsum = Math.round(rsum / picture.getWidth() * picture.getHeight());
        gsum = Math.round(gsum / picture.getWidth() * picture.getHeight());
        bsum = Math.round(bsum / picture.getWidth() * picture.getHeight());
        return new Color(rsum,gsum,bsum);
    }
}
