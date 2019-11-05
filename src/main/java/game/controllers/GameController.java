package game.controllers;

import game.models.GameObject;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private Pane gamePane;
    private Scene scene;
    private Player player;
    private   Obstacle obstacle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        player = new Player();
       player.getView().setTranslateX(10.0);
       player.getView().setTranslateY(311.0);
        gamePane.getChildren().add(player.getView());

        ImageView obstacleView = new ImageView(
                new Image(GameController.class.getResourceAsStream("/drawable/cactusSmall0000.png")));
       // Rectangle2D viewportRect = new Rectangle2D(40, 35, 110, 110);
       // obstacleView.setViewport(viewportRect);

        obstacle = new Obstacle(obstacleView);
        obstacle.getView().setTranslateX(590.0);
        obstacle.getView().setTranslateY(311.0);
        gamePane.getChildren().add(obstacle.getView());


        gamePane.requestFocus();

        gamePane.setOnKeyPressed((e) -> {
            if(e.getCode() == KeyCode.SPACE){
                System.out.println("Space has been clicked !");
                player.jumping = true;
                player.up = true;
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
             onUpdate();
            }
        };
        timer.start();
    }

    private static class Player extends GameObject {
        boolean jumping = false;
        boolean up = false;
        Player() {
            super(new ImageView(new Image(GameController.class.getResourceAsStream("/drawable/dino0000.png"))));
        }
    }
    private static class Obstacle extends GameObject{
        Obstacle(ImageView imageView){
            super(imageView);
        }
    }


    private void onUpdate(){
        obstacle.update();
        if (player.isColliding(obstacle)){
            System.out.println("EndGame");
        }
        if(! obstacle.isAlive()){
    gamePane.getChildren().remove(obstacle.getView());
            System.out.println("is dead");
            ImageView obstacleView = new ImageView(
                    new Image(GameController.class.getResourceAsStream("/drawable/cactusSmall0000.png")));
            obstacle = new Obstacle(obstacleView);
            obstacle.getView().setTranslateX(590.0);
            obstacle.getView().setTranslateY(311.0);
            gamePane.getChildren().add(obstacle.getView());

}
        if (player.jumping){
            if((player.getView().getTranslateY() >= 150.0) && player.up){
                player.getView().setTranslateY(player.getView().getTranslateY() -2);
            }
            else if((player.getView().getTranslateY() <= 311.0) && !player.up) {
                player.getView().setTranslateY(player.getView().getTranslateY() + 2);
            }
            else {
                player.up = false;
            }
        }
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}