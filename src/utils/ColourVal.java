package utils;

public class ColourVal {
    final Colour avgCol;
    final SearchColour searchByColour;

    public ColourVal(Colour avgCol, SearchColour searchByColour){
        this.avgCol = avgCol;
        this.searchByColour = searchByColour;
    }
    public enum SearchColour{
        RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PURPLE, PINK, WHITE, GREY, BLACK, BROWN;
    }
}
