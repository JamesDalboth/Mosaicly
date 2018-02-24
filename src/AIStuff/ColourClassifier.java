package AIStuff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import picture.Color;
import utils.ColourVal.SearchColour;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class ColourClassifier {

  private String[] colours = {"YELLOW",
      "GREEN",
      "MUSTARD",
      "GOLD",
      "ORANGE",
      "OLIVE",
      "BROWN",
      "RED",
      "TEAL",
      "CYAN",
      "BLUE",
      "BLACK",
      "MAROON",
      "MAGENTA",
      "PINK",
      "PURPLE"};
  private Classifier classifier;

  public ColourClassifier() {
    try {
      this.classifier = loadModel(new File("weka_models"), "/RandomForest");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {

    ColourClassifier c = new ColourClassifier();
    System.out.println("Loaded model.");
    System.out.println(c.classify(new Color(171, 151, 0)));
  }

  public static Classifier loadModel(File path, String name) throws Exception {

    Classifier classifier;

    FileInputStream fis = new FileInputStream(path + name + ".model");
    ObjectInputStream ois = new ObjectInputStream(fis);

    classifier = (Classifier) ois.readObject();
    ois.close();

    return classifier;
  }
//
//    List<String> readAllLines = Files.readAllLines(Paths.get("data.txt"));
//    for (int i = 0; i < readAllLines.size(); i++) {
////      if (i % 10000 == 0){
////        System.out.println("i = " + i);
////      }
//      String line = readAllLines.get(i);
//
//      String[] splitted = line.split(" ");
//      int red = Integer
//          .parseInt(splitted[0].substring(1, splitted[0].length() - 1));
//      int green = Integer
//          .parseInt(splitted[1].substring(0, splitted[1].length() - 1));
//      int blue = Integer
//          .parseInt(splitted[2].substring(0, splitted[2].length() - 1));
//      String colour = splitted[3].substring(0, splitted[3].length())
//          .toUpperCase();
//      if (splitted.length == 5) {
//        colour = splitted[4].toUpperCase();
//      }
//      Instance next = new DenseInstance(4);
//      next.setValue(R, red);
//      next.setValue(G, green);
//      next.setValue(B, blue);
//      next.setValue(COLOUR, colour);
//      isTrainingSet.add(next);
//    }
////    Classifier cModel = new RandomForest();
////    cModel.buildClassifier(isTrainingSet);
////    System.out.println("Built the classifier!");
////    saveModel(cModel, "RandomForest");
//
//
//    // Test the model
////    Evaluation eTest = new Evaluation(isTrainingSet);
////    eTest.evaluateModel(cModel, isTrainingSet);
////
////    // Print the result à la Weka explorer:
////    String strSummary = eTest.toSummaryString();
////    System.out.println(strSummary);

  private static void saveModel(Classifier c, String name) throws Exception {
    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(
          new FileOutputStream("weka_models" + name + ".model"));

    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    oos.writeObject(c);
    oos.flush();
    oos.close();
  }

  public Instances getTestSet() {
    //1.ATTRIBUTES

    // Declare two numeric attributes
    Attribute R = new Attribute("R");
    Attribute G = new Attribute("G");
    Attribute B = new Attribute("B");

    // Declare a nominal attribute along with its values
    ArrayList<String> colourVal = new ArrayList<>();

    colourVal.addAll(Arrays.asList(colours));

    Attribute COLOUR = new Attribute("COLOUR", colourVal);

    // Declare the feature vector
    ArrayList<Attribute> wekaAttributes = new ArrayList<>();
    wekaAttributes.add(R);
    wekaAttributes.add(G);
    wekaAttributes.add(B);
    wekaAttributes.add(COLOUR);

    // Create an empty training set
    Instances isTrainingSet = new Instances("Rel", wekaAttributes, 1);

    // Set class index
    isTrainingSet.setClassIndex(3);
    return isTrainingSet;
  }

  public SearchColour classify(Color color) {
    SearchColour[] s = {
        SearchColour.YELLOW,
        SearchColour.GREEN,
        SearchColour.YELLOW,
        SearchColour.ORANGE,
        SearchColour.GREEN,
        SearchColour.BROWN,
        SearchColour.RED,
        SearchColour.TEAL,
        SearchColour.TEAL,
        SearchColour.BLUE,
        SearchColour.BLACK,
        SearchColour.BROWN,
        SearchColour.PURPLE,
        SearchColour.PINK,
        SearchColour.PURPLE
    };
    // Create an empty test set
    Instances testSet = getTestSet();
    // Set class index

    Instance instance = new DenseInstance(3);
    instance.setValue(0, color.getRed());
    instance.setValue(1, color.getBlue());
    instance.setValue(2, color.getGreen());
    testSet.add(instance);
    try {
      return s[(int) classifier.classifyInstance(testSet.instance(0))];
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
