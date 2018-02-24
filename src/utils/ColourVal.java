package utils;

import picture.Color;

public class ColourVal {
    final Color avgCol;
    final SearchColour searchByColour;

    public ColourVal(Color avgCol){
        this.avgCol = avgCol;
        this.searchByColour = searchByRepresentation();
    }
    private SearchColour searchByRepresentation(){
        //Will take avgCol and determine which colour bucket it fits into for search purposes
        //Is dependent on implementation of colour
        static double
        static double absBlue = ;


        return SearchColour.BLUE;
        //Placeholder return before implementation of colour is seen
    }
    public enum SearchColour{
        RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PURPLE, PINK, WHITE, GREY, BLACK, BROWN;
    }
}
