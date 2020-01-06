package game.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Track extends GameObject{
    private Image image = new Image(this.getClass().getResourceAsStream("/drawable/route.png"),
            600.0, 12.0, true, false);
    private ImageView routeOne = new ImageView(image);
    private ImageView routeTwo = new ImageView(image);
    private ImageView routeThree = new ImageView(image);
    private HBox track = new HBox(routeOne, routeTwo, routeThree);

    public Track(){
        this.setView(track);
        this.getView().setTranslateX(0.0);
        this.getView().setTranslateY(346.0);
        this.getView().toBack();
    }
    public void update(double currentSpeed){
       track.setTranslateX(track.getTranslateX() - currentSpeed);
       if(track.getTranslateX() < -600){
           reset();
       }
    }
    private void reset(){
        this.getView().setTranslateX(0.0);
    }
}