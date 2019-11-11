package game.controllers;

import game.models.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private Pane gamePane;
    private Dinosaur player;
    private Obstacle obstacle;
    private Random random = new Random();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        player = new Dinosaur();
        //player.getView().setTranslateX(10.0);
       // player.getView().setTranslateY(311.0);
        gamePane.getChildren().add(player.getView());

      //  ImageView obstacleView = new ImageView(
       //         new Image(GameController.class.getResourceAsStream("/drawable/cactusSmall0000.png")));
        // Rectangle2D viewportRect = new Rectangle2D(40, 35, 110, 110);
        // obstacleView.setViewport(viewportRect);

       // obstacle = new Obstacle(obstacleView);
      //  obstacle.getView().setTranslateX(590.0);
       // obstacle.getView().setTranslateY(311.0);
        obstacle = new  CactusSmall();
        gamePane.getChildren().add(obstacle.getView());


        gamePane.requestFocus();

        gamePane.setOnKeyPressed((e) -> {
            if ((e.getCode() == KeyCode.SPACE) && (player.isJumping() == false)) {
                player.jump();
            }
            else if(((e.getCode() == (KeyCode.DOWN)) || (e.getCode() == (KeyCode.S))) && (player.isJumping() == false)){
                player.duck();
            }
        });
gamePane.setOnKeyReleased((event -> {
    if(((event.getCode() == KeyCode.DOWN) || (event.getCode() == (KeyCode.S))) && (player.isJumping() == false)){
        System.out.println("key released");
        player.notDuck();
    }
}));
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();
    }

    /*
        private static class Player extends GameObject {
            boolean jumping = false;
            boolean up = false;
            Player() {
                super(new ImageView(new Image(GameController.class.getResourceAsStream("/drawable/dino0000.png"))));
            }
        }

     */
    /*
    private static class Obstacle extends GameObject {
        Obstacle(ImageView imageView) {
            super(imageView);
        }
    }
     */


    private void onUpdate(){
        obstacle.update();
        player.update();
        if(player.isColliding(obstacle))

    {
        System.out.println("EndGame");
    }
        if(!obstacle.isAlive())

    {
        gamePane.getChildren().remove(obstacle.getView());
        System.out.println("is dead");
        int obstacleNumber = random.nextInt(3);
        switch (obstacleNumber){
            case 0:
                obstacle = new CactusSmall();
                break;
            case 1:
                obstacle = new CactusBig();
                break;
            default:
                obstacle = new Pterodactyl();
                break;
        }
       // obstacle = new Obstacle(obstacleView);
        //obstacle.getView().setTranslateX(590.0);
        //obstacle.getView().setTranslateY(311.0);
        gamePane.getChildren().add(obstacle.getView());

    }
        if(player.isJumping())
    {
        player.jumping();
    }
    }
}