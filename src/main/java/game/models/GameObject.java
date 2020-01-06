package game.models;

import javafx.scene.Node;

public class GameObject {
    private Node view;
    private double velocity = 6;

    private boolean alive = true;

    public double getVelocity() {
        return velocity;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    private boolean visible = true;

    public GameObject() {
    }

    public GameObject(Node view) {
        this.view = view;
    }

    public void update() {
        view.setTranslateX(view.getTranslateX() - velocity);
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
        if(this.view.getTranslateX() < -100.0){
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

//zajebiste rozwiązanie problemu z za dużym tłem obietów
    /*
public boolean isColliding(GameObject other) {
    return getView().getBoundsInParent().intersects(other.getView().getTranslateX()+2,
            other.getView().getTranslateY()+2,
            other.getView().getBoundsInParent().getWidth()+4 ,
            other.getView().getBoundsInParent().getHeight()-2);
}

     */

}
