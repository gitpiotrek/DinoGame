package game.models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class CactusBig extends Obstacle{
    private ImageView obstacleView = new ImageView();
    private static Image oneCactus = new Image(Obstacle.class.getResourceAsStream("/drawable/cactusBigOne.png")
                ,25.0,50.0,true,false);
    private Image twoCactus= new Image(Obstacle.class.getResourceAsStream("/drawable/cactusBigTwo.png")
            ,50.0,50.0,true,false);;
    private Image threeCactus= new Image(Obstacle.class.getResourceAsStream("/drawable/cactusBigThree.png")
            ,75.0,50.0,true,false);;

    private Random random = new Random();
    public CactusBig(Node view, double width, double height, int xPos, int yPos, int minGap) {
        super(view, width, height, xPos, yPos, minGap);
    }

    public CactusBig(){
        this.setMinGap(120);
        this.setWidth(25.0);
        this.setHeight(50.0);
        obstacleView.setImage(selectSize());
        obstacleView.setImage(selectSize());
        this.setView(obstacleView);
        this.getView().setTranslateX(750.0);
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
