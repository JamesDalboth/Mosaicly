package utils;

import picture.Color;

import java.util.HashMap;
import java.util.Map;

public class ColourVal {
    final Color avgCol;
    final SearchColour searchByColour;

    public Map<SearchColour, Color> enumConversion;

    public ColourVal(Color avgCol) {
        this.avgCol = avgCol;
        //this.avgCol = new Color(0,0,0);
        this.searchByColour = searchByRepresentation();
    }

    private SearchColour searchByRepresentation() {
        //Will take avgCol and determine which colour bucket it fits into for search purposes


        //Initialisation ofa hashMap containing the RGB value for each SearchColour
        enumConversion = new HashMap<>();
        enumConversion.put(SearchColour.RED,new Color(200,0,0));
        enumConversion.put(SearchColour.ORANGE, new Color(255, 190, avgCol.getBlue()));
        enumConversion.put(SearchColour.YELLOW, new Color(255, 255, avgCol.getBlue()));
        enumConversion.put(SearchColour.GREEN,new Color(0,200,0));
        enumConversion.put(SearchColour.TEAL, new Color(avgCol.getRed(), 255, 255));
        enumConversion.put(SearchColour.BLUE,new Color(0,0,200));
        enumConversion.put(SearchColour.PURPLE, new Color(100, avgCol.getGreen(), 200));
        enumConversion.put(SearchColour.PINK, new Color(255, avgCol.getGreen(), 255));
        enumConversion.put(SearchColour.WHITE,new Color(255,255,255));
        enumConversion.put(SearchColour.GRAY, new Color(avgCol.getGreen(), avgCol.getBlue(), avgCol.getRed()));
        enumConversion.put(SearchColour.BROWN, new Color(204, 204, 0));
        enumConversion.put(SearchColour.BLACK,new Color(0,0,0));


        double currentMin = 1000;
        SearchColour toReturn = SearchColour.WHITE;
        for(SearchColour absColour : SearchColour.values()){
            if(findAbs(absColour)<currentMin){
                currentMin = findAbs(absColour);
                toReturn = absColour;
            }
        }
        return toReturn;
    }


    private double findAbs(SearchColour colour){
        double willBeReturned = 0;
        willBeReturned += Math.pow(enumConversion.get(colour).getRed() - avgCol.getRed(),2);
        willBeReturned += Math.pow(enumConversion.get(colour).getGreen() - avgCol.getGreen(),2);
        willBeReturned += Math.pow(enumConversion.get(colour).getBlue() - avgCol.getBlue(),2);
        willBeReturned = Math.sqrt(willBeReturned);

        return willBeReturned;
}


    public String getSearchByColour() {
        return searchByColour.toString();
    }

    public enum SearchColour {
        BROWN, RED, ORANGE, YELLOW, GREEN, TEAL, BLUE, PURPLE, PINK, WHITE, GRAY, BLACK;
    }
}
