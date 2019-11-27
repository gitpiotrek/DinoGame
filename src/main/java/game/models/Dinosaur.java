package game.models;

import game.controllers.GameController;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static game.models.PhysicAbstraction.GRAVITY;

public class Dinosaur extends GameObject{

    private boolean jumping = false;
    private boolean duck = false;
    //flag for animate running dinosaur
    private boolean runningLeg = false;
   private ImageView animateMovement = new ImageView();
   private Image leftLeg = new Image(GameController.class.getResourceAsStream("/drawable/dinorunleft.png")
           ,44.0,47.0,true,false);
   private Image rightLeg = new Image(GameController.class.getResourceAsStream("/drawable/dinorunright.png")
           ,44.0,47.0,true,false);
   private Image jumpingDino = new Image(GameController.class.getResourceAsStream("/drawable/dino0000.png")
           ,44.0,47.0,true,false);
    private Image duckLeftLeg = new Image(GameController.class.getResourceAsStream("/drawable/dinoduckright.png")
            ,59.0,25.0,true,false);
    private Image duckRightLeg = new Image(GameController.class.getResourceAsStream("/drawable/dinoduckleft.png")
            ,59.0,25.0,true,false);

    private int floatingTime = 8;
    private int runningTime = 25;
    private int xPos;
    private int yPos;
private double jumpSpeed = 4.0;
    public Dinosaur(Node view) {
        super(view);
    }

    public Dinosaur() {
        animateMovement.setImage(leftLeg);
        this.setView(animateMovement);
        this.getView().setTranslateX(10.0);
        this.getView().setTranslateY(299.0);
    }
/**
 * old jumping method
    public void jumping(){
        if (this.jumping){
            if((this.getView().getTranslateY() >= 180.0) && this.up){
                if(this.getView().getTranslateY() <= 311.0){
                    animateMovement.setImage(jumpingDino);
                }
                this.getView().setTranslateY(this.getView().getTranslateY() -3);
            }
            else if((this.getView().getTranslateY() <= 311.0) && !this.up) {
                if(this.getView().getTranslateY() == 311.0){
                this.jumping = false;
                update();
            }
                this.getView().setTranslateY(this.getView().getTranslateY() + 3);
            }
            else {
                if(floatingTime == 0){
                this.up = false;
                floatingTime = 8; }
                else{
                    --floatingTime;
                }
            }
        }
    }
 */
public void jumping(){
    if (this.jumping) {
        /*
        if ((this.getView().getTranslateY() <= 311.0) && jumpSpeed == 4.0) {
            animateMovement.setImage(jumpingDino);
        }

         */
            this.getView().setTranslateY(this.getView().getTranslateY() - jumpSpeed);
            jumpSpeed -= GRAVITY;

        if ((this.getView().getTranslateY() >= 299.0) && jumpSpeed < 0.0) {
            this.jumping = false;
            jumpSpeed = 4.0;
            update();
        }
    }
}

// cos działa xD
    @Override
    public void update(){
        if(!jumping && !duck) {
            if(runningTime == 0) {
                if (runningLeg) {
                    // this.setView(leftLeg);
                   animateMovement.setImage(rightLeg);
                    runningLeg = false;
                } else {
                    //  this.setView(rightLeg);
                    animateMovement.setImage(leftLeg);
                    runningLeg = true;
                }
                runningTime = 20;
            }
            else --runningTime;
        }
        else if(duck && !jumping){
            if(runningTime == 0) {
                if (runningLeg) {
                    // this.setView(leftLeg);
                    animateMovement.setImage(duckRightLeg);
                    runningLeg = false;
                } else {
                    //  this.setView(rightLeg);
                    animateMovement.setImage(duckLeftLeg);
                    runningLeg = true;
                }
                runningTime = 20;
            }

            else --runningTime;
        }else {
            animateMovement.setImage(jumpingDino);
        }
    }

    public void jump() {
        this.jumping = true;
       // this.up = true;
       // this.setView(jumpingDino);
    }

    public boolean  isJumping() {
        return jumping;
    }
    public boolean isDucking(){return duck;}

    public void duck(){
        this.duck = true;
        animateMovement.setImage(duckLeftLeg);
        this.setView(animateMovement);
        this.getView().setTranslateY(321.0);
    }
    public void notDuck(){
        this.duck = false;
        animateMovement.setImage(leftLeg);
        this.setView(animateMovement);
        this.getView().setTranslateY(299.0);
    }
}
