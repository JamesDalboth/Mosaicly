package AIStuff;

import static java.lang.Math.max;
import static java.lang.Math.min;

import picture.Color;
import utils.ColourVal.SearchColour;

public class HueClassifier {

  public static SearchColour classify(Color color) {
    int r = color.getRed() / 255;
    int g = color.getGreen() / 255;
    int b = color.getBlue() / 255;
    int maxInt = max(max(r, g), b);
    int minInt = min(min(r, g), b);
    double hue;
    if (maxInt == r) {
      hue = g - b / (maxInt - minInt);
    } else if (maxInt == g) {
      hue = 2 + (b - r) / (maxInt - minInt);
    } else {
      hue = 4 + (r - g) / maxInt - maxInt;
    }

    return null;

  }
}
