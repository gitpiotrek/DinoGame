package game.models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class CactusBig extends Obstacle{
    private ImageView obstacleView = new ImageView();
    private Image oneCactus = new Image(this.getClass().getResourceAsStream("/drawable/cactusBig0000.png")
            ,this.getWidth(),this.getHeight(),true,false);
    private Image twoCactus = new Image(this.getClass().getResourceAsStream("/drawable/cactusSmallMany0000.png")
            ,this.getWidth()*2,this.getHeight(),true,false);
    private Image threeCactus = new Image(this.getClass().getResourceAsStream("/drawable/cactusSmallMany0000.png")
            ,this.getWidth()*3,this.getHeight(),true,false);

    private Random random = new Random();
    public CactusBig(Node view, double width, double height, int xPos, int yPos, int minGap) {
        super(view, width, height, xPos, yPos, minGap);
    }

    public CactusBig(){
        this.setMinGap(120);
        this.setWidth(25.0);
        this.setHeight(50.0);
        this.setView(obstacleView);
        obstacleView.setImage(selectSize());
        this.getView().setTranslateX(590.0);
        this.getView().setTranslateY(296.0);

    }

    private Image selectSize(){
        int i = random.nextInt(3);
        Image returnedImage;
        switch (i){
            case 0:
                returnedImage = oneCactus;
                break;
            case 1:
                returnedImage = twoCactus;
                break;
            default:
                returnedImage = threeCactus;
                break;

        }
        return returnedImage;
    }
}
