package game.models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Random;

public class CactusBig extends Obstacle implements Collideable{
    private ImageView obstacleView = new ImageView();
    private final static Image oneCactus = new Image(Obstacle.class.getResourceAsStream("/drawable/cactusBigOne.png")
                ,25.0,50.0,true,false);
    private final static Image twoCactus = new Image(Obstacle.class.getResourceAsStream("/drawable/cactusBigTwo.png")
            ,50.0,50.0,true,false);
    private final static Image threeCactus = new Image(Obstacle.class.getResourceAsStream("/drawable/cactusBigThree.png")
            ,75.0,50.0,true,false);

    private  Rectangle[] collisionBoxes;

    private Random random = new Random();
    public CactusBig(Node view, double width, double height, int xPos, int yPos, int minGap) {
        super(view, width, height, xPos, yPos, minGap);
    }

    public CactusBig(){
        this.setMinGap(120);
       // this.setWidth(25.0);
       // this.setHeight(50.0);
        this.setView(obstacleView);
        this.getView().setTranslateX(750.0);
        this.getView().setTranslateY(296.0);
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
                this.setWidth(25.0);
                this.setHeight(50.0);
                break;
            case 1:
                returnedImage = twoCactus;
                this.setWidth(50.0);
                this.setHeight(50.0);
                break;
            default:
                returnedImage = threeCactus;
                this.setWidth(75.0);
                this.setHeight(50.0);
                break;
        }
        return returnedImage;
    }

    @Override
    public Rectangle[] collisionArea() {
       return collisionBoxes;
    }

    @Override
    public void update(){

            this.collisionBoxes = collisionBoxesForCactusImage(this.getView().getTranslateX(),
                    this.getView().getTranslateY(), this.obstacleView.getImage());
          //  rectangle.setLayoutX(this.getView().getTranslateX());
          //  rectangle.setTranslateX(this.getView().getTranslateX());
          //  rectangle.setTranslateY(this.getView().getTranslateY());

        super.update();
    }

    private  Rectangle[] collisionBoxesForCactusImage(double posX, double posY, Image image){
        double smallerCactusSize = 7.0;
        return new  Rectangle[]{
                new Rectangle(posX, posY + 12.0,
                        smallerCactusSize, 38.0),
                new Rectangle(posX + smallerCactusSize, posY,
                        image.getWidth() - 2*smallerCactusSize, 49.0),
                new Rectangle(posX + image.getWidth() - smallerCactusSize, posY + 10,
                        smallerCactusSize -1 , 38.0)
        };
    }
}
