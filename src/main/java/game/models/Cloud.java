package game.models;

import game.controllers.GameController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class Cloud extends GameObject{
    private ImageView cloudView = new ImageView();
    private Image cloudImage = new Image(GameController.class.getResourceAsStream("/drawable/cloud.png"), 45.0,15.0,true,false);
    public Cloud(){
        this.setMinGap(80);
        this.setVelocity(2.0);
        this.setView(cloudView);
        this.cloudView.setImage(cloudImage);
        this.getView().setTranslateX(750.0);
        this.getView().toBack();
        Random random = new Random();
        //losowanie wysokosci;
        int y=random.nextInt(120);
        this.getView().setTranslateY(311.0-y);

    }
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double getMinGap() {
        return minGap;
    }

    public void setMinGap(double minGap) {
        this.minGap = minGap;
    }

    private double width;
    private double height;
    private double xPos;
    private double yPos;
    private double minGap;

}
