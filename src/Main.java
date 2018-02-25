import Classification.Core;
import Classification.GifDecoder;
import org.w3c.dom.Node;
import picture.Picture;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Main extends JFrame{

    Picture[] pics;
  public static void main(String[] args) throws IOException {
      new Main(args);




    System.out.println("Done");
  }

  public Main(String[] args) throws IOException {
      Core core = new Core(5,10);

      String inputFile = args[0];
      String seedword = args[1];
      core.scan(seedword);
      pics = decomposeGif(inputFile);

      this.setSize(800,1000);

      JPanel panel = new JPanel();
      panel.setLayout(null);


      JButton runButton = new JButton();
      runButton.setSize(100,50);
      runButton.setBounds(350,900,100,50);
      runButton.setText("Run!");

      panel.add(runButton);
      JLabel imageLabel = new JLabel();
      ImageIcon pic=new ImageIcon(readImgFromFile(inputFile,imageLabel));
      imageLabel = new JLabel(pic);
      imageLabel.setBounds(0,0,400,400);

      JLabel resultLabel = new JLabel();
      resultLabel.setBounds(400,0,400,400);
      panel.add(resultLabel);


      panel.add(imageLabel);

      runButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              core.run(pics);

              ImageIcon pic2=new ImageIcon(readImgFromFile("C:\\Users\\JamesDalboth\\IdeaProjects\\Mosaicly\\output\\target.gif",resultLabel));
              resultLabel.setIcon(pic2);
          }

      });

      this.add(panel);
      this.setVisible(true);
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  public static Picture[] decomposeGif(String location) throws IOException {
      Picture[] result = new Picture[0];
      if (!location.endsWith(".gif")) {
          result = new Picture[1];
          result[0] = picture.Utils.loadPicture(location);
      } else {
          GifDecoder gd = new GifDecoder();
          gd.read(location);

          result = new Picture[gd.getFrameCount()];
          for (int i = 0, count = gd.getFrameCount(); i < count; i++)
          {
              BufferedImage image = gd.getFrame(i);
              result[i] = new Picture(image);
          }
      }
      return result;
  }

    public static Image readImgFromFile(String filename, Component parent) {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }

        // Fix for bug when delay is 0
        try {
            // Load anything but GIF the normal way
            if (!filename.substring(filename.length() - 4).equalsIgnoreCase(".gif")) {
                return ImageIO.read(file);
            }

            // Get GIF reader
            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            // Give it the stream to decode from
            reader.setInput(ImageIO.createImageInputStream(file));

            int numImages = reader.getNumImages(true);

            // Get 'metaFormatName'. Need first frame for that.
            IIOMetadata imageMetaData = reader.getImageMetadata(0);
            String metaFormatName = imageMetaData.getNativeMetadataFormatName();

            // Find out if GIF is bugged
            boolean foundBug = false;
            for (int i = 0; i < numImages && !foundBug; i++) {
                // Get metadata
                IIOMetadataNode root = (IIOMetadataNode)reader.getImageMetadata(i).getAsTree(metaFormatName);

                // Find GraphicControlExtension node
                int nNodes = root.getLength();
                for (int j = 0; j < nNodes; j++) {
                    Node node = root.item(j);
                    if (node.getNodeName().equalsIgnoreCase("GraphicControlExtension")) {
                        // Get delay value
                        String delay = ((IIOMetadataNode)node).getAttribute("delayTime");

                        // Check if delay is bugged
                        if (Integer.parseInt(delay) == 0) {
                            foundBug = true;
                        }

                        break;
                    }
                }
            }

            // Load non-bugged GIF the normal way
            Image image;
            if (!foundBug) {
                image = Toolkit.getDefaultToolkit().createImage(filename);
            } else {
                // Prepare streams for image encoding
                ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
                try (ImageOutputStream ios = ImageIO.createImageOutputStream(baoStream)) {
                    // Get GIF writer that's compatible with reader
                    ImageWriter writer = ImageIO.getImageWriter(reader);
                    // Give it the stream to encode to
                    writer.setOutput(ios);

                    writer.prepareWriteSequence(null);

                    for (int i = 0; i < numImages; i++) {
                        // Get input image
                        BufferedImage frameIn = reader.read(i);

                        // Get input metadata
                        IIOMetadataNode root = (IIOMetadataNode)reader.getImageMetadata(i).getAsTree(metaFormatName);

                        // Find GraphicControlExtension node
                        int nNodes = root.getLength();
                        for (int j = 0; j < nNodes; j++) {
                            Node node = root.item(j);
                            if (node.getNodeName().equalsIgnoreCase("GraphicControlExtension")) {
                                // Get delay value
                                String delay = ((IIOMetadataNode)node).getAttribute("delayTime");

                                // Check if delay is bugged
                                if (Integer.parseInt(delay) == 0) {
                                    // Overwrite with a valid delay value
                                    ((IIOMetadataNode)node).setAttribute("delayTime", "10");
                                }

                                break;
                            }
                        }

                        // Create output metadata
                        IIOMetadata metadata = writer.getDefaultImageMetadata(new ImageTypeSpecifier(frameIn), null);
                        // Copy metadata to output metadata
                        metadata.setFromTree(metadata.getNativeMetadataFormatName(), root);

                        // Create output image
                        IIOImage frameOut = new IIOImage(frameIn, null, metadata);

                        // Encode output image
                        writer.writeToSequence(frameOut, writer.getDefaultWriteParam());
                    }

                    writer.endWriteSequence();
                }

                // Create image using encoded data
                image = Toolkit.getDefaultToolkit().createImage(baoStream.toByteArray());
            }

            // Trigger lazy loading of image
            MediaTracker mt = new MediaTracker(parent);
            mt.addImage(image, 0);
            try {
                mt.waitForAll();
            }
            catch (InterruptedException e) {
                image = null;
            }
            image = image.getScaledInstance(
                    400,
                    400, Image.SCALE_DEFAULT);
            return image;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

