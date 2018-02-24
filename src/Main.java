import Classification.Core;
import picture.Picture;

public class Main {

    public static void main(String[] args) {
        String inputFile = args[0];
        String seedword = args[1];

        Picture initPic = picture.Utils.loadPicture(inputFile);

        Core core = new Core(initPic,5,seedword);
    }
}

