package utils;

import picture.Color;

import java.util.HashMap;
import java.util.Map;

public class ColourVal {
    final Color avgCol;
    final SearchColour searchByColour;

    public Map<SearchColour,Color> enumConversion= new HashMap<>();

    public ColourVal(Color avgCol){
        this.avgCol = avgCol;
        this.searchByColour = searchByRepresentation();
    }
    private SearchColour searchByRepresentation(){
        //Will take avgCol and determine which colour bucket it fits into for search purposes


        //Initialisation ofa hashMap containing the RGB value for each SearchColour
        Map<SearchColour,Color> enumConversion= new HashMap<>();
        enumConversion.put(SearchColour.RED,new Color(255,0,0));
        enumConversion.put(SearchColour.ORANGE,new Color(255,153,51));
        enumConversion.put(SearchColour.YELLOW,new Color(255,255,100));
        enumConversion.put(SearchColour.GREEN,new Color(0,255,0));
        enumConversion.put(SearchColour.CYAN,new Color(51,255,255));
        enumConversion.put(SearchColour.BLUE,new Color(0,0,255));
        enumConversion.put(SearchColour.PURPLE,new Color(153,51,255));
        enumConversion.put(SearchColour.PINK,new Color(255,51,255));
        enumConversion.put(SearchColour.WHITE,new Color(255,255,255));
        enumConversion.put(SearchColour.GREY,new Color(178,178,178));
        enumConversion.put(SearchColour.BLACK,new Color(0,0,0));
        enumConversion.put(SearchColour.BROWN,new Color(102,51,0));



        double absRed = 0;
        double absOrange = 0;
        double absYellow = 0;
        double absGreen = 0;
        double absCyan = 0;
        double absBlue = 0;
        double absPurple = 0;
        double absPink = 0;
        double absWhite = 0;
        double absGrey = 0;
        double absBlack = 0;
        double absBrown = 0;


        return SearchColour.BLUE;
        //Placeholder return before implementation of colour is seen
    }
    public enum SearchColour{
        RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PURPLE, PINK, WHITE, GREY, BLACK, BROWN;
    }
}
