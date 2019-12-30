package game.models;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Obstacle extends GameObject implements Collideable{
    private double  width;
    private double height;
    private int xPos;
    private int yPos;
    private int minGap;

    public Obstacle() {
    }

    public Obstacle(Node view, double width, double height, int xPos, int yPos, int minGap) {
        super(view);
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
        this.minGap = minGap;
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

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getMinGap() {
        return minGap;
    }

    public void setMinGap(int minGap) {
        this.minGap = minGap;
    }

    @Override
    public Rectangle[] collisionArea() {
        return new Rectangle[0];
    }
}
