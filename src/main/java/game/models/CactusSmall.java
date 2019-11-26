package game.models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class CactusSmall extends Obstacle {
   private ImageView obstacleView = new ImageView();
   private Image oneCactus;
   private Image twoCactus;
   private Image threeCactus;
    private Image fourCactus;
    private Random random = new Random();
    public CactusSmall(Node view, double width, double height, int xPos, int yPos, int minGap) {
        super(view, width, height, xPos, yPos, minGap);
    }

    public CactusSmall(){
        this.setMinGap(120);
        this.setWidth(17.0);
        this.setHeight(35.0);
        initializeImages();
        this.setView(obstacleView);
        obstacleView.setImage(selectSize());
        this.getView().setTranslateX(735.0);
        this.getView().setTranslateY(311.0);

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
            case 2:
                returnedImage = threeCactus;
                break;
            default:
                returnedImage = fourCactus;
                break;
        }
        return returnedImage;
    }
    public void initializeImages(){
        ImageView obstacleView = new ImageView();
        oneCactus = new Image(this.getClass().getResourceAsStream("/drawable/cactusSmallOne.png")
                ,this.getWidth(),this.getHeight(),true,false);
        twoCactus = new Image(this.getClass().getResourceAsStream("/drawable/cactusSmallTwo.png")
                ,this.getWidth()*2,this.getHeight(),true,false);
        threeCactus = new Image(this.getClass().getResourceAsStream("/drawable/cactusSmallThree.png")
                ,this.getWidth()*3,this.getHeight(),true,false);
        fourCactus = new Image(this.getClass().getResourceAsStream("/drawable/cactusSmallFour.png")
                ,this.getWidth()*4,this.getHeight(),true,false);
    }
}
