package game.models;

import javafx.geometry.Point2D;
import javafx.scene.Node;

public class GameObject {
    private Node view;
    //private Point2D velocity = new Point2D(-6, 0);
    private double velocity = 6;

    private boolean alive = true;

    public GameObject() {
    }

    public GameObject(Node view) {
        this.view = view;
    }

    public void update() {
        view.setTranslateX(view.getTranslateX() - velocity);
       // view.setTranslateY(view.getTranslateY() + velocity.getY());
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }


    public Node getView() {
        return view;
    }

    public void setView(Node view) {
        this.view = view;
    }

    public boolean isAlive() {
        if(this.view.getTranslateX() < 10){
            alive = false;
        }
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double getRotate() {
        return view.getRotate();
    }
/*
    public void rotateRight() {
        view.setRotate(view.getRotate() + 5);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
    }

    public void rotateLeft() {
        view.setRotate(view.getRotate() - 5);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
    }
*/
    public boolean isColliding(GameObject other) {
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }
}
