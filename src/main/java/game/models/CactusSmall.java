package game.models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Random;

public class CactusSmall extends Obstacle implements Collideable{
    private ImageView obstacleView = new ImageView();
    private static Image oneCactus = new Image(Obstacle.class.getResourceAsStream("/drawable/cactusSmallOne.png")
                ,17.0,35.0,true,false);
    private static Image twoCactus = new Image(Obstacle.class.getResourceAsStream("/drawable/cactusSmallTwo.png")
                ,34.0,35.0,true,false);
    private static Image threeCactus = new Image(Obstacle.class.getResourceAsStream("/drawable/cactusSmallThree.png")
                ,51.0,35.0,true,false);
    private static Image fourCactus = new Image(Obstacle.class.getResourceAsStream("/drawable/cactusSmallFour.png")
                ,68.0,35.0,true,false);
    private Random random = new Random();

    public CactusSmall(Node view, double width, double height, int xPos, int yPos, int minGap) {
        super(view, width, height, xPos, yPos, minGap);
    }

    private Rectangle[] collisionBoxes;

    public CactusSmall() {
        this.setMinGap(120);
       // this.setWidth(17.0);
       // this.setHeight(35.0);
        this.setView(obstacleView);
        this.getView().setTranslateX(735.0);
        this.getView().setTranslateY(311.0);
        obstacleView.setImage(selectSize());
        this.collisionBoxes = collisionBoxesForCactusImage(this.getView().getTranslateX(),
                this.getView().getTranslateY(), this.obstacleView.getImage());
    }
    private Image selectSize(){
        int i = random.nextInt(3);
        Image returnedImage;
        switch (i){
            case 0:
                returnedImage = oneCactus;
                this.setWidth(17.0);
                this.setHeight(35.0);
                break;
            case 1:
                returnedImage = twoCactus;
                this.setWidth(34.0);
                this.setHeight(35.0);
                break;
            case 2:
                returnedImage = threeCactus;
                this.setWidth(51.0);
                this.setHeight(35.0);
                break;
            default:
                returnedImage = fourCactus;
                this.setWidth(68.0);
                this.setHeight(35.0);
                break;
        }
        return returnedImage;
    }

    @Override
    public Rectangle[] collisionArea() { return collisionBoxes;}

    @Override
    public void update(){
        this.collisionBoxes = collisionBoxesForCactusImage(this.getView().getTranslateX(),
                this.getView().getTranslateY(), this.obstacleView.getImage());

           // rectangle.setTranslateX(this.getView().getTranslateX());
          //  rectangle.setTranslateY(this.getView().getTranslateY());

        super.update();
    }

    private Rectangle[] collisionBoxesForCactusImage(double posX, double posY, Image image){
        double smallerCactusSize = 5.0;

        return new Rectangle[]{
                new Rectangle(posX, posY + 7.0,
                        smallerCactusSize, 27.0),
                new Rectangle(posX + smallerCactusSize, posY,
                        image.getWidth() - 2*smallerCactusSize, 34.0),
                new Rectangle(posX + image.getWidth() - smallerCactusSize, posY + 4.0,
                        smallerCactusSize - 1, 14.0)
        };
    }
}