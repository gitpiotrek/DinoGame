package game.models;

import ai.communication.State;
import game.controllers.GameController;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Dinosaur extends GameObject {

    private boolean jumping = false;
    private boolean duck = false;
    private boolean runningLeg = false;
    private ImageView animateMovement = new ImageView();
    private Image leftLeg = new Image(GameController.class.getResourceAsStream("/drawable/dino_left.png")
            , 44.0, 47.0, true, false);
    private Image rightLeg = new Image(GameController.class.getResourceAsStream("/drawable/dino_right.png")
            , 44.0, 47.0, true, false);
    private Image jumpingDino = new Image(GameController.class.getResourceAsStream("/drawable/dino.png")
            , 44.0, 47.0, true, false);
    private Image duckLeftLeg = new Image(GameController.class.getResourceAsStream("/drawable/dino_duck_left.png")
            , 59.0, 25.0, true, false);
    private Image duckRightLeg = new Image(GameController.class.getResourceAsStream("/drawable/dino_duck_right.png")
            , 59.0, 25.0, true, false);
    private Image dead = new Image(GameController.class.getResourceAsStream("/drawable/dino_dead.png")
            , 44.0, 47.0, true, false);

    private int runningTime = 20;
    private double jumpSpeed = Math.abs(PhysicAbstraction.INITIAL_JUMP_VELOCITY);
    private boolean smallJumping;
    private State previousState = State.RUN;

    private Rectangle[] collisionBoxes;

    public Dinosaur(Node view) {
        super(view);
    }

    public Dinosaur() {
        animateMovement.setImage(leftLeg);
        this.setView(animateMovement);
        this.getView().setTranslateX(20.0);
        this.getView().setTranslateY(308.0);
        this.getView().toFront();
        collisionBoxes = dinoCollisionBoxes(this.getView().getTranslateX(), this.getView().getTranslateY());
    }

    public void jumping() {
        if (this.jumping) {

            this.getView().setTranslateY(this.getView().getTranslateY() - jumpSpeed);
            if (isSmallJumping()) {
                jumpSpeed = PhysicAbstraction.INITIAL_JUMP_VELOCITY;
            } else {
                jumpSpeed -= PhysicAbstraction.GRAVITY;
            }

            if ((this.getView().getTranslateY() >= 308.0) && jumpSpeed < 0.0) {
                this.jumping = false;
                setSmallJumping(false);
                jumpSpeed = Math.abs(PhysicAbstraction.INITIAL_JUMP_VELOCITY);
                this.getView().setTranslateY(308.0);
                update();
            }
        }
    }

    public void setSmallJumping(boolean smallJumping) {
        this.smallJumping = smallJumping;
    }

    public boolean isSmallJumping() {
        return this.smallJumping;
    }

    @Override
    public void update() {
        collisionBoxes = dinoCollisionBoxes(this.getView().getTranslateX(), this.getView().getTranslateY());
        if (!jumping && !duck) {
            if (runningTime == 0) {
                if (runningLeg) {
                    animateMovement.setImage(rightLeg);
                    runningLeg = false;
                } else {
                    animateMovement.setImage(leftLeg);
                    runningLeg = true;
                }
                runningTime = 20;
            } else --runningTime;
        } else if (duck && !jumping) {
            if (runningTime == 0) {
                if (runningLeg) {
                    animateMovement.setImage(duckRightLeg);
                    runningLeg = false;
                } else {
                    animateMovement.setImage(duckLeftLeg);
                    runningLeg = true;
                }
                runningTime = 20;
            } else --runningTime;
        } else {
            animateMovement.setImage(jumpingDino);
        }
    }

    public void jump() {
        this.jumping = true;
        this.duck = false;
    }

    public boolean isJumping() {
        return jumping;
    }

    public boolean isDucking() {
        return duck;
    }

    public void duck() {
        this.duck = true;
        animateMovement.setImage(duckLeftLeg);
        this.setView(animateMovement);
        this.getView().setTranslateY(330.0);
    }

    public void notDuck() {
        this.duck = false;
        animateMovement.setImage(leftLeg);
        this.setView(animateMovement);
        this.getView().setTranslateY(308.0);
    }

    public void die() {
        animateMovement.setImage(dead);
        this.setView(animateMovement);
    }

    public void run(){
        this.duck = false;
    }

    //skrypt w lniach ok 1700 - 1850 są collisionbox-y
    public boolean isColliding(Collideable obstacle) {

        if (!this.isDucking()) {
            for (Rectangle collisionBox : collisionBoxes) {
                for (Rectangle obstacleShape : obstacle.collisionArea()) {
                    if (collisionBox.getBoundsInParent().intersects(obstacleShape.getBoundsInParent())) {
                        return true;
                    }
                }
            }
        } else {
            for (Rectangle obstacleShape : obstacle.collisionArea()) {
                if (getView().getBoundsInParent().intersects(obstacleShape.getBoundsInParent())) {
                    return true;
                }
            }
        }
        return false;
    }
    private Rectangle[] dinoCollisionBoxes(double posX, double posY){
        return new Rectangle[]{
                new Rectangle(posX + 22,posY, 17, 16),
                new Rectangle(posX + 1, posY + 18, 30, 9),
                new Rectangle(posX + 10,posY +  35, 14, 8),
                new Rectangle(posX + 1, posY + 24, 29, 5),
                new Rectangle(posX + 5, posY + 30, 21, 4),
                new Rectangle(posX + 9, posY + 34, 15, 4),
        };
    }

    public void controlByNeuralNetwork(State state) {
        switch (state) {
            case JUMP:
                this.jump();
                previousState = State.JUMP;
                break;
            case SMALL_JUMP:
                if (previousState == State.JUMP) {
                    this.setSmallJumping(true);
                    previousState = State.SMALL_JUMP;
                }
                break;
            case DUCK:
                if (!this.jumping) {
                    this.duck();
                    previousState = State.DUCK;
                }
                break;
            default:
                if (previousState == State.DUCK) {
                    this.notDuck();
                }
                this.run();
                previousState = State.RUN;
                break;
        }
    }
}

