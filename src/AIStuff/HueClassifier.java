package AIStuff;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import picture.Color;
import utils.ColourVal.SearchColour;

public class HueClassifier {

  public static SearchColour classify(Color color) {
    double r = (double) color.getRed() / 255.0;
    double g = (double) color.getGreen() / 255.0;
    double b = (double) color.getBlue() / 255.0;

//    if(color.getBlue()<6 && color.getGreen() <6 && color.getRed()<6){return SearchColour.WHITE;}
//    if(color.getBlue()>248 && color.getGreen() >248 && color.getRed()>248){return SearchColour.BLACK;}
//    int avg = (color.getBlue() + color.getRed() + color.getGreen())/3;
//    if(within5(avg,color.getBlue())&&within5(avg,color.getRed())&&within5(avg,color.getGreen())){return SearchColour.GRAY;}

    int maxInt = max(max(color.getRed(), color.getGreen()), color.getBlue());
    int minInt = min(min(color.getRed(), color.getGreen()), color.getBlue());

    double maxDouble = max(max(r, g), b);
    double minDouble = min(min(r, g), b);
    double hue;

    if (maxInt == minInt) {
      if (maxInt < 127) {
        if (maxInt == 0) {
          return SearchColour.BLACK;
        }
        return SearchColour.GRAY;
      } else {
        if (maxInt == 255) {
          return SearchColour.WHITE;
        }
        return SearchColour.GRAY;
      }
    } else {
      if (maxDouble == r) {
        hue = g - b / (maxDouble - minDouble);
      } else if (maxDouble == g) {
        hue = 2 + (b - r) / (maxDouble - minDouble);
      } else {
        hue = 4 + (r - g) / maxDouble - minDouble;
      }
      hue *= 60;
      if (hue < 0) {
        hue += 360;
      }


      if (hue <= 25) {
        return SearchColour.RED;
      }
      if (hue <= 41) {
        return SearchColour.ORANGE;
      }
      if (hue <= 69) {
        return SearchColour.YELLOW;
      }
      if (hue <= 166) {
        return SearchColour.GREEN;
      }
      if (hue <= 190) {
        return SearchColour.TEAL;
      }
      if (hue <= 251) {
        return SearchColour.BLUE;
      }
      if (hue <= 295) {
        return SearchColour.PURPLE;
      }
      if (hue <= 336) {
        return SearchColour.PINK;
      }
      if (hue <= 360) {
        return SearchColour.RED;
      }
      return null;
    }


  }



}
