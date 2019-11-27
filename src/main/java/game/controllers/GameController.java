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

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GameController implements Initializable, Runnable{
//TODO Ukrywanie przeszkod po lewej stronie, zwiekszenie predkosci, mozliwosc pojawienia sie wielu przeszkod ( max 3 ) w jednym widoku,
    @FXML
    private Pane gamePane;
    private Score score;
    private Dinosaur player;
    private List<Obstacle> obstacle = new ArrayList<>();
    private Label endGame;
    private  AnimationTimer timer;
    private Random random = new Random();
    private double currentSpeed = 6;
    private DataReceiver dataReceiver = new DataReceiver();
    private DataEmiter dataEmiter = new DataEmiter(dataReceiver);
    private ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(2);
    private List<List> neuronTrainingData = new ArrayList<>();
    private List<Double> distanceToNextObstacle = new ArrayList<>();
    private List<Double> heightOfObstacle = new ArrayList<>();
    private List<Double> widthOfObstacle = new ArrayList<>();
    private List<Double> playerYPosition = new ArrayList<>();
    private List<Double> pterodactylHeight = new ArrayList<>();
    private List<Double> velocity = new ArrayList<>();
    private List<State> state = new ArrayList<>();
    private int iterator=0;
    private Thread ioThread = new Thread(() ->{
        while (true) {
    if(!dataReceiver.isEmpty()) {
        NodeInput nodeInput = dataReceiver.getData();
            System.out.println(nodeInput.getDistanceToNextObstacle() +" "+ nodeInput.getHeightOfObstacle() + " "+
            nodeInput.getWidthOfObstacle() + " " + nodeInput.getPlayerYPosition() + " " +
            nodeInput.getVelocity() + " " +nodeInput.getPterodactylHeight() + nodeInput.getState() );

             distanceToNextObstacle.add(nodeInput.getDistanceToNextObstacle());
             heightOfObstacle.add(nodeInput.getHeightOfObstacle());
             widthOfObstacle.add(nodeInput.getWidthOfObstacle());
             playerYPosition.add(nodeInput.getPlayerYPosition());
             pterodactylHeight.add(nodeInput.getPterodactylHeight());
             velocity.add(nodeInput.getVelocity());
             state.add(nodeInput.getState());
             if(iterator%30 == 29){
                 FileWriter csvWriter = null;
                 try {
                     csvWriter = new FileWriter("new.csv");
                     for(int i=0;i<neuronTrainingData.get(0).size();i++) {
                         for (List a : neuronTrainingData) {
                             csvWriter.append(a.get(i) + ",");
                         }
                         csvWriter.append("\n");
                     }
                 } catch (IOException e) {
                     e.printStackTrace();
                 }

                 try {
                     csvWriter.flush();
                     csvWriter.close();

                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
             iterator++;
}
        }
    });

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        neuronTrainingData.add(distanceToNextObstacle);
        neuronTrainingData.add(heightOfObstacle);
        neuronTrainingData.add(widthOfObstacle);
        neuronTrainingData.add(playerYPosition);
        neuronTrainingData.add(velocity);
        neuronTrainingData.add(pterodactylHeight);
        neuronTrainingData.add(state);


        score = new Score();
        player = new Dinosaur();
        endGame = new Label();
        endGame.setTranslateX(310);
        endGame.setTranslateY(190);
        endGame.setText("GAME OVER");
        gamePane.getChildren().add(player.getView());
        gamePane.getChildren().add(score.getView());
        gamePane.getChildren().add(endGame);
        endGame.setVisible(false);

        obstacle.add(new  CactusSmall());
        gamePane.getChildren().add(obstacle.get(0).getView());
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

    private void onUpdate(){

        for(Obstacle o: obstacle){
            o.update();
        }
        player.update();
        score.onUpdate(currentSpeed);
        speedUp();



        if(obstacle.get(0).isAlive()){
                if(player.isColliding(obstacle.get(0))) {
                    timer.stop();
                    executorService.shutdownNow();
                    ioThread.suspend();
                    score.resetScore();
                    endGame.setVisible(true);
                    player.die();
                    gamePane.setOnKeyPressed((e) -> {
                        if ((e.getCode() == KeyCode.SPACE)) {

                            for(Obstacle o: obstacle){
                                gamePane.getChildren().remove(o.getView());
                            }
                            currentSpeed = 6;
                            obstacle.clear();
                            obstacle.add(new CactusSmall());
                            gamePane.getChildren().add(obstacle.get(0).getView());
                            player.getView().setTranslateY(299.0);
                            endGame.setVisible(false);
                            timer.start();
                            executorService = new ScheduledThreadPoolExecutor(2);
                            executorService.scheduleAtFixedRate(this, 0, 200, TimeUnit.MILLISECONDS);
                            ioThread.resume();
                            setSpaceOnKeyPressed();
                }});}
        }else{
            if(player.isColliding(obstacle.get(1))) {
                timer.stop();
                executorService.shutdownNow();
                ioThread.suspend();
                score.resetScore();
                endGame.setVisible(true);
                player.die();
                gamePane.setOnKeyPressed((e) -> {
                    if ((e.getCode() == KeyCode.SPACE)) {
                        for(Obstacle o: obstacle){
                            gamePane.getChildren().remove(o.getView());
                        }
                        currentSpeed = 6;
                        obstacle.clear();
                        obstacle.add(new CactusSmall());
                        gamePane.getChildren().add(obstacle.get(0).getView());
                        player.getView().setTranslateY(299.0);
                        endGame.setVisible(false);
                        timer.start();
                        executorService = new ScheduledThreadPoolExecutor(2);
                        executorService.scheduleAtFixedRate(this, 0, 200, TimeUnit.MILLISECONDS);
                        ioThread.resume();
                        setSpaceOnKeyPressed();
                    }});}

        }

        if(obstacle.get(0).getView().getTranslateX()<10){
            obstacle.get(0).setAlive(false);
        }
        if(obstacle.get(0).getView().getTranslateX()<= -100){
            obstacle.get(0).setVisible(false);
        }

        if(!obstacle.get(0).isVisible()){
            gamePane.getChildren().remove(obstacle.get(0).getView());
            obstacle.remove(0);
        }

        drawObsticle();
        if(player.isJumping()) {
            player.jumping();
        }
    }

    private void speedUp(){
        currentSpeed += PhysicAbstraction.ACCELERATION;
        for(Obstacle obstacle: obstacle){
            obstacle.setVelocity(currentSpeed);
        }
    }

    @Override
    public void run() {
        NodeInput nodeInput = new NodeInput();
        double x;
        double y;

        if(obstacle.get(0).getView().getTranslateX() > 10){
            x = obstacle.get(0).getView().getTranslateX();
            y = obstacle.get(0).getView().getTranslateY();
        }else {
            x = obstacle.get(1).getView().getTranslateX();
            y = obstacle.get(1).getView().getTranslateY();
        }

        nodeInput.setDistanceToNextObstacle(((x - (player.getView().getTranslateX() + (player.isDucking()?59.0:44.0)))+50)/750);
       // nodeInput.setDistanceBetweenObstacles(obstacle.getView().getTranslateX());
        nodeInput.setPterodactylHeight(obstacle instanceof Pterodactyl?(1.0/3.0 + 1.0/3.0*(261-y)/25.0):0.00);
        nodeInput.setHeightOfObstacle(obstacle.get(0).getHeight()/50);
        nodeInput.setWidthOfObstacle(obstacle.get(0).getWidth()/46);
        nodeInput.setPlayerYPosition((player.getView().getTranslateY()-220)/100);
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
            if(obstacle.isEmpty() || (!obstacle.get(0).isAlive() && obstacle.size() == 1)){
                    int obstacleNumber = random.nextInt(3);
                    switch (obstacleNumber){
                        case 0:
                            obstacle.add(new CactusSmall());
                            break;
                        case 1:
                            obstacle.add(new CactusBig());
                            break;
                        default:
                            obstacle.add(new Pterodactyl());
                            break;
                    }
            gamePane.getChildren().add(obstacle.get(obstacle.size()-1).getView());
        }else if (700-obstacle.get(obstacle.size()-1).getView().getTranslateX() > obstacle.get(obstacle.size()-1).getMinGap()){
                if(random.nextInt(100)<1){

                    int obstacleNumber = random.nextInt(3);
                    switch (obstacleNumber){
                        case 0:
                            obstacle.add(new CactusSmall());
                            break;
                        case 1:
                            obstacle.add(new CactusBig());
                            break;
                        default:
                            obstacle.add(new Pterodactyl());
                            break;
                    }
                    gamePane.getChildren().add(obstacle.get(obstacle.size()-1).getView());
                }


            }
       }

    public void setSpaceOnKeyPressed(){
        gamePane.setOnKeyPressed((e) -> {
            if ((e.getCode() == KeyCode.SPACE) && (player.isJumping() == false)) {
                player.jump();
            }
            else if(((e.getCode() == (KeyCode.DOWN)) || (e.getCode() == (KeyCode.S))) && (player.isJumping() == false)){
                player.duck();
            }else if(((e.getCode() == (KeyCode.DOWN)) || (e.getCode() == (KeyCode.S))) && (player.isJumping() == true)){
                player.setSmallJumping(true);
            }
        });
    }
}