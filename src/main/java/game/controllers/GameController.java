package game.controllers;

import ai.communication.DataEmiter;
import ai.communication.DataReceiver;
import ai.communication.NodeInput;
import ai.communication.State;
import game.models.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GameController implements Initializable, Runnable{

    @FXML
    private Pane gamePane;
    private Score score;
    private Dinosaur player;
    private Obstacle obstacle;
    private Label endGame;
    private  AnimationTimer timer;
    private Random random = new Random();
    private double currentSpeed = 6;
    private DataReceiver dataReceiver = new DataReceiver();
    private DataEmiter dataEmiter = new DataEmiter(dataReceiver);
    private ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(2);
    private Thread ioThread = new Thread(() ->{
        while (true) {
    if(!dataReceiver.isEmpty()) {
   NodeInput nodeInput = dataReceiver.getData();
    System.out.println(nodeInput.getDistanceToNextObstacle() +" "+ nodeInput.getHeightOfObstacle() + " "+
          nodeInput.getWidthOfObstacle() + " " + nodeInput.getPlayerYPosition() + " " +
            nodeInput.getVelocity() + " " + nodeInput.getState() );
}
        }
    });

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        score = new Score();
        player = new Dinosaur();
        endGame = new Label();
        endGame.setTranslateX(310);
        endGame.setTranslateY(190);
        endGame.setText("GAME OVER");
        //player.getView().setTranslateX(10.0);
       // player.getView().setTranslateY(311.0);
        gamePane.getChildren().add(player.getView());

        gamePane.getChildren().add(score.getView());

        gamePane.getChildren().add(endGame);
        endGame.setVisible(false);

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
        setSpaceOnKeyPressed();

gamePane.setOnKeyReleased((event -> {
    if(((event.getCode() == KeyCode.DOWN) || (event.getCode() == (KeyCode.S))) && (player.isJumping() == false)){
        System.out.println("key released");
        player.notDuck();
    }
}));

//Threads
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                onUpdate();
            }
        };
        timer.start();



        executorService.scheduleAtFixedRate(this,0,200, TimeUnit.MILLISECONDS);
        ioThread.setName("Thread for io calculation");
        ioThread.start();
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
        score.onUpdate(currentSpeed);
        speedUp();
        if(player.isColliding(obstacle))

    {

        timer.stop();
        score.resetScore();
        endGame.setVisible(true);


        gamePane.setOnKeyPressed((e) -> {
            if ((e.getCode() == KeyCode.SPACE)) {
                timer.start();
                obstacle.setAlive(false);
                drawObsticle();
                player.getView().setTranslateY(311.0);
                endGame.setVisible(false);
                setSpaceOnKeyPressed();
            }
            else if(((e.getCode() == (KeyCode.DOWN)) || (e.getCode() == (KeyCode.S))) && (player.isJumping() == false)){
            }
        });
        //System.out.println("EndGame");
    }
        drawObsticle();
        if(player.isJumping())
    {
        player.jumping();
    }
    }
    private void speedUp(){
        currentSpeed += PhysicAbstraction.ACCELERATION;
    }

    @Override
    public void run() {
        NodeInput nodeInput = new NodeInput();
        nodeInput.setDistanceToNextObstacle(((obstacle.getView().getTranslateX() - (player.getView().getTranslateX() + (player.isDucking()?59.0:44.0)))+50)/590);

       // nodeInput.setDistanceBetweenObstacles(obstacle.getView().getTranslateX());
        nodeInput.setPterodactylHeight((obstacle instanceof Pterodactyl?obstacle.getView().getTranslateY():0.0));
        nodeInput.setHeightOfObstacle(obstacle.getHeight()/50);
        nodeInput.setWidthOfObstacle(obstacle.getWidth()/46);
        nodeInput.setPlayerYPosition((player.getView().getTranslateY()-380)/100);
        nodeInput.setVelocity((this.currentSpeed-6)/14);

        nodeInput.setState(returnState());
        dataEmiter.emitData(nodeInput);
    }

    private State returnState(){
        if(player.isDucking()){
        return State.DUCK;
        }else if (player.isJumping()){
            return State.JUMP;
        }else if(player.isDucking() && player.isJumping()){
            return State.SMALL_JUMP;
        }else {return State.RUN;}
    }
    public void drawObsticle(){
        if(!obstacle.isAlive()) {
            gamePane.getChildren().remove(obstacle.getView());
            //  System.out.println("Current speed is: " + currentSpeed);
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
            obstacle.setVelocity(currentSpeed);
            gamePane.getChildren().add(obstacle.getView());

        }}
    public void setSpaceOnKeyPressed(){
        gamePane.setOnKeyPressed((e) -> {
            if ((e.getCode() == KeyCode.SPACE) && (player.isJumping() == false)) {
                player.jump();
            }
            else if(((e.getCode() == (KeyCode.DOWN)) || (e.getCode() == (KeyCode.S))) && (player.isJumping() == false)){
                player.duck();
            }
        });
    }
}