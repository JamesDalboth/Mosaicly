package AIStuff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.meta.ClassificationViaRegression;
import weka.classifiers.pmml.consumer.NeuralNetwork;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.BallTree;

public class ColourClassifier {

  public static void main(String[] args) throws Exception {

    //1.ATTRIBUTES

    // Declare two numeric attributes
    Attribute R = new Attribute("R");
    Attribute G = new Attribute("G");
    Attribute B = new Attribute("B");

    // Declare a nominal attribute along with its values
    ArrayList<String> colourVal = new ArrayList<>();
    String[] colours = {"YELLOW",
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

    colourVal.addAll(Arrays.asList(colours));

    Attribute COLOUR = new Attribute("COLOUR", colourVal);

    // Declare the feature vector
    ArrayList<Attribute> wekaAttributes = new ArrayList<>();
    wekaAttributes.add(R);
    wekaAttributes.add(G);
    wekaAttributes.add(B);
    wekaAttributes.add(COLOUR);

    // Create an empty training set
    Instances isTrainingSet = new Instances("Rel", wekaAttributes, 200000);

    // Set class index
    isTrainingSet.setClassIndex(3);

    List<String> readAllLines = Files.readAllLines(Paths.get("data.txt"));
    for (int i = 0; i < readAllLines.size(); i++) {
//      if (i % 10000 == 0){
//        System.out.println("i = " + i);
//      }
      String line = readAllLines.get(i);

      String[] splitted = line.split(" ");
      int red = Integer
          .parseInt(splitted[0].substring(1, splitted[0].length() - 1));
      int green = Integer
          .parseInt(splitted[1].substring(0, splitted[1].length() - 1));
      int blue = Integer
          .parseInt(splitted[2].substring(0, splitted[2].length() - 1));
      String colour = splitted[3].substring(0, splitted[3].length())
          .toUpperCase();
      if (splitted.length == 5) {
        colour = splitted[4].toUpperCase();
      }
      Instance next = new DenseInstance(4);
      next.setValue(R, red);
      next.setValue(G, green);
      next.setValue(B, blue);
      next.setValue(COLOUR, colour);
      isTrainingSet.add(next);
    }
//    Classifier cModel = new RandomForest();
//    cModel.buildClassifier(isTrainingSet);
//    System.out.println("Built the classifier!");
//    saveModel(cModel, "RandomForest");

    Classifier cModel = loadModel(new File("weka_models"), "/RandomForest");
    System.out.println("Loaded model.");
    // Test the model
    Evaluation eTest = new Evaluation(isTrainingSet);
    eTest.evaluateModel(cModel, isTrainingSet);

    // Print the result à la Weka explorer:
    String strSummary = eTest.toSummaryString();
    System.out.println(strSummary);

  }

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


  private static Classifier loadModel(File path, String name) throws Exception {

    Classifier classifier;

    FileInputStream fis = new FileInputStream(path + name + ".model");
    ObjectInputStream ois = new ObjectInputStream(fis);

    classifier = (Classifier) ois.readObject();
    ois.close();

    return classifier;
  }
}
