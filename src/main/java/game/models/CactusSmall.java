package game.models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class CactusSmall extends Obstacle {
   private ImageView obstacleView = new ImageView();
   private Image oneCactus = new Image(this.getClass().getResourceAsStream("/drawable/cactusSmall0000.png")
      ,this.getWidth(),this.getHeight(),true,false);
   private Image twoCactus = new Image(this.getClass().getResourceAsStream("/drawable/cactusSmallTwo.png"));
   private Image threeCactus = new Image(this.getClass().getResourceAsStream("/drawable/cactusSmallThree.png"));
    private Image fourCactus = new Image(this.getClass().getResourceAsStream("/drawable/cactusSmallMany0000.png")
            ,this.getWidth()*4,this.getHeight(),true,false);


    private Random random = new Random();
    public CactusSmall(Node view, double width, double height, int xPos, int yPos, int minGap) {
        super(view, width, height, xPos, yPos, minGap);
    }

    public CactusSmall(){
        this.setMinGap(120);
        this.setWidth(17.0);
        this.setHeight(35.0);
        this.setView(obstacleView);
        obstacleView.setImage(selectSize());
        obstacleView.setFitHeight(this.getHeight());
        obstacleView.setFitWidth(this.getWidth()*2);
        this.getView().setTranslateX(590.0);
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
}
