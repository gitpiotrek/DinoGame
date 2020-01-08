package game.models;
//TODO Zmniejszyc odpowiednio wysokosc lotu ptaka
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Random;

public class Pterodactyl extends Obstacle {
    private Random random = new Random();

    private ImageView obstacleView = new ImageView();
    private static Image pterodactylUp = new Image(Obstacle.class.getResourceAsStream("/drawable/pterodactylUp.png")
            ,46.0,40.0,true,false);
    private static Image pterodactylDown = new Image(Obstacle.class.getResourceAsStream("/drawable/pterodactylDown.png")
                ,46.0,40.0,true,false);

    private Rectangle[] collisionBoxes;

   public Pterodactyl(Node view, double width, double height, int xPos, int yPos, int minGap) {
       super(view, width, height, xPos, yPos, minGap);
   }

   private int runningTime = 20;
   private boolean runningLeg = false;
   public Pterodactyl(){
       this.setMinGap(150);
       this.setWidth(46.0);
       this.setHeight(40.0);
       this.setView(obstacleView);
       this.getView().setTranslateX(746.0 );
       this.getView().setTranslateY(343.0 - selectYPos());
       obstacleView.setImage(pterodactylUp);
       collisionBoxes = collisionBoxesForPterodacty(this.getView().getTranslateX(),this.getView().getTranslateY());
   }

   @Override
   public void update(){
       if(runningTime == 0) {
           if (runningLeg) {
               obstacleView.setImage(pterodactylUp);
               runningLeg = false;
           } else {
               obstacleView.setImage(pterodactylDown);

               runningLeg = true;
           }
           runningTime = 20;
       }else {
           --runningTime;
       }
       collisionBoxes = collisionBoxesForPterodacty(this.getView().getTranslateX(),this.getView().getTranslateY());
       super.update();
   }

    private double selectYPos(){
        int i = random.nextInt(3);
        int y;
        switch (i){
            case 0:
              y = 100;
                break;
            case 1:
                y = 70;
                break;
            default:
                y = 50;
                break;
        }
        return y;
    }

    @Override
    public Rectangle[] collisionArea() { return collisionBoxes; }

    private Rectangle[] collisionBoxesForPterodacty(double posX, double posY){
        return new Rectangle[]{
                new Rectangle( posX + 15, posY + 15, 16, 5),
                new Rectangle(posX + 18, posY + 21, 24, 6),
                new Rectangle(posX + 2, posY + 14, 4, 3),
                new Rectangle(posX + 6, posY + 10, 4, 7),
                new Rectangle(posX + 10,posY +  8, 6, 9)

               // new Rectangle(posX + 20, posY + 20,
                  //      26.0,20.0)
        };
    }
}