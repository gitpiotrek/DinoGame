package game.models;
//TODO Zmniejszyc odpowiednio wysokosc lotu ptaka
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class Pterodactyl extends Obstacle {
    private Random random = new Random();

    private ImageView obstacleView = new ImageView();
    private Image pterodactylUp;
    private Image pterodactylDown;
   public Pterodactyl(Node view, double width, double height, int xPos, int yPos, int minGap) {
       super(view, width, height, xPos, yPos, minGap);
   }
   private int runningTime = 20;
   private boolean runningLeg = false;
   public Pterodactyl(){
       this.setMinGap(150);
       this.setWidth(46.0);
       this.setHeight(40.0);
       initializeImages();
       this.setView(obstacleView);
       obstacleView.setImage(pterodactylUp);
       this.getView().setTranslateX(746.0 );
       this.getView().setTranslateY(343.0 - (double) selectYPos());

   }

   @Override
   public void update(){
       if(runningTime == 0) {
           if (runningLeg) {
               // this.setView(leftLeg);
               obstacleView.setImage(pterodactylUp);
               runningLeg = false;
           } else {
               //  this.setView(rightLeg);
               obstacleView.setImage(pterodactylDown);
               runningLeg = true;
           }
           runningTime = 20;
       }else {
           --runningTime;
       }
       super.update();
   }

    private int selectYPos(){
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
    public void initializeImages(){
        obstacleView = new ImageView();
        pterodactylUp = new Image(this.getClass().getResourceAsStream("/drawable/berdUp.png")
                ,this.getWidth(),this.getHeight(),true,false);
        pterodactylDown = new Image(this.getClass().getResourceAsStream("/drawable/berdDown.png")
                ,this.getWidth(),this.getHeight(),true,false);
    }
}
