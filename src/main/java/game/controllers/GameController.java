package game.controllers;

import ai.communication.DataEmitter;
import ai.communication.DataReceiver;
import ai.communication.NodeInput;
import ai.communication.State;
import ai.fileManager.NeuralNetworkFileManager;
import ai.fileManager.RunFileWriter;
import ai.neural.NeuralNetwork;
import game.MainGameStarter;
import game.models.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import static game.models.PhysicAbstraction.*;

public class GameController implements Initializable {
    @FXML
    private Pane gamePane;
    private Score score;
    private Dinosaur player;
    private Track track;

    private LinkedBlockingDeque<Obstacle> obstacles;
    private Label endGame;
    private AnimationTimer timer;

    private Random random = new Random();
    private double currentSpeed;

    private DataReceiver dataReceiver;
    private DataEmitter dataEmitter;
    private RunFileWriter runFileWriter;

    private List<Cloud> clouds = new ArrayList<>();
    private Font font = Font.loadFont(this.getClass().getResourceAsStream("/font/PressStart2P-Regular.ttf"), 28.0);
    private ImageView replayImageView;
    private Image replayImage = new Image(GameController.class.getResourceAsStream("/drawable/restart_button.png")
            , 36.0, 32.0, true, false);

    private Label menuLabel;
    private NeuralNetwork neuralNetwork = null;

    //  private Image backToMenuImage = new Image(GameController.class.getResourceAsStream("/drawable/restart_button.png")
    //      ,36.0,32.0,true,false);


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        gamePane.setOnKeyPressed((e) -> {});
        switch (MainGameStarter.gameState) {
            case PLAY:
                setOnKeyPressed();
                break;
            case TRAIN:
                setOnKeyPressed();
                dataReceiver = new DataReceiver();
                dataEmitter = new DataEmitter(dataReceiver);
                runFileWriter = new RunFileWriter(dataReceiver);
                runFileWriter.writeToFile();
                break;
            case SHOW_RESULT:
                dataReceiver = new DataReceiver();
                dataEmitter = new DataEmitter(dataReceiver);
                NeuralNetworkFileManager neuralNetworkFileManager = new NeuralNetworkFileManager(MenuController.selectedNeuralFile);
                try {
                    neuralNetwork = neuralNetworkFileManager.loadNeuralNetwork();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Load neural network file error");
                }
                break;
        }

        currentSpeed = INITIAL_SPEED;
        obstacles = new LinkedBlockingDeque<>();

        score = new Score();
        player = new Dinosaur();
        player.getView().toFront();
        track = new Track();
        endGame = new Label();
        endGame.setTranslateX(230);
        endGame.setTranslateY(150);
        endGame.setText("GAME OVER");
        endGame.setFont(font);

        gamePane.getChildren().add(player.getView());
        gamePane.getChildren().add(score.getView());
        gamePane.getChildren().add(endGame);
        gamePane.getChildren().add(track.getView());

        endGame.setVisible(false);

        //replay menu zrobic lepiej
        menuLabel = new Label("Press M to back to menu");
        gamePane.getChildren().add(menuLabel);
        menuLabel.setTranslateY(260);
        menuLabel.setTranslateX(332);
        menuLabel.setVisible(false);
        //---------

        replayImageView = new ImageView();
        gamePane.getChildren().add(replayImageView);

        replayImageView.setTranslateY(220);
        replayImageView.setTranslateX(332);
        replayImageView.setImage(replayImage);
        replayImageView.setVisible(false);

        obstacles.add(new CactusSmall());
        gamePane.getChildren().add(obstacles.getFirst().getView());
        clouds.add(new Cloud());
        gamePane.getChildren().add(clouds.get(0).getView());

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                onUpdate();
            }
        };
        timer.start();

    }

    private void onUpdate() {

        switch (MainGameStarter.gameState) {
            case PLAY:
                break;
            case TRAIN:
                if (!obstacles.isEmpty()) emitData();
                break;
            case SHOW_RESULT:
                State state;
                if (!obstacles.isEmpty()) emitData();
                if (!dataReceiver.isEmpty()) {
                    try {
                        state = neuralNetwork.getStateResponse(dataReceiver.getData());
                        if(state == State.SMALL_JUMP){
                        System.out.println(state);}
                        player.controlByNeuralNetwork(state);
                    } catch (Exception e) {
                        player.die();
                    }
                }
                break;
        }

        drawCloud();
        drawObstacles();

        for (Obstacle o : obstacles) {
            o.update();
        }
        for (Cloud cloud : clouds) {
            cloud.update();
        }

        track.update(currentSpeed);
        player.update();
        score.onUpdate(currentSpeed);
        speedUp();

        if (obstacles.getFirst().isAlive()) {
            if (player.isColliding(obstacles.getFirst())) {
                replayImageView.setVisible(true);
                timer.stop();
                endGame.setVisible(true);
                player.die();

                //poprawic
                menuLabel.setVisible(true);

                switch (MainGameStarter.gameState) {
                    case PLAY:
                        break;
                    case TRAIN:
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setInitialFileName(MainGameStarter.playerName +"_" + score.getNumericScore());
                        fileChooser.getExtensionFilters().addAll(
                                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                        File dest = fileChooser.showSaveDialog(gamePane.getParent().getScene().getWindow());
                        runFileWriter.finishWriting(dest);
                        break;
                    case SHOW_RESULT:
                        System.out.println("Train your Dino again");
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + MainGameStarter.gameState);
                }
                gamePane.setOnKeyPressed((e) -> {
                    if ((e.getCode() == KeyCode.SPACE)) {
                        gamePane.getChildren().clear();

                        this.initialize(null, null);
                    }
                    if ((e.getCode() == KeyCode.M)) {
                        try {
                            loadMenu();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }

        if (obstacles.getFirst().getView().getTranslateX() <= obstacles.getFirst().getWidth() * (-1)) {
            obstacles.getFirst().setAlive(false);
        }
        /*
        if(obstacles.getFirst().getView().getTranslateX()<= -100){
            obstacles.getFirst().setVisible(false);
        }

         */

        if (!obstacles.getFirst().isAlive()) {
            gamePane.getChildren().remove(obstacles.getFirst().getView());
            obstacles.poll();
        }
        if (!clouds.get(0).isVisible()) {
            gamePane.getChildren().remove(clouds.get(0).getView());
            clouds.remove(0);
        }
        if (player.isJumping()) {
            player.jumping();
        }
    }

    private void drawCloud() {
        if (clouds.isEmpty()) {
            clouds.add(new Cloud());
            gamePane.getChildren().add(clouds.get(clouds.size() - 1).getView());
        } else if (clouds.get(clouds.size() - 1).getView().getTranslateX() < 680) {
            if (random.nextInt(100) < 1) {
                clouds.add(new Cloud());
                gamePane.getChildren().add(clouds.get(clouds.size() - 1).getView());
            }
        }
    }

    private void speedUp() {
        if (currentSpeed <= PhysicAbstraction.MAX_SPEED) {
            currentSpeed += PhysicAbstraction.ACCELERATION;
            if (currentSpeed > PhysicAbstraction.MAX_SPEED) {
                currentSpeed = PhysicAbstraction.MAX_SPEED;
            }
            for (Obstacle obstacle : obstacles) {
                obstacle.setVelocity(currentSpeed);
            }
        }
    }

    private void emitData() {
        NodeInput nodeInput = new NodeInput();
        double x = obstacles.getFirst().getView().getTranslateX();
        double y = obstacles.getFirst().getView().getTranslateY();
        double distance = (x - (player.getView().getTranslateX() + (player.isDucking() ? 59.0 : 44.0))) / 750;

        nodeInput.setDistanceToNextObstacle((distance>0)?distance:0);
        nodeInput.setDistanceBetweenObstacles((obstacles.size() > 1) ? (obstacles.iterator().next().getView().getTranslateX()
                - obstacles.iterator().next().getView().getTranslateX()) / 600 : 0.0);
        nodeInput.setPterodactylHeight(obstacles.getFirst() instanceof Pterodactyl ? (y / 343.0) : 0.00);
        nodeInput.setHeightOfObstacle(obstacles.getFirst().getHeight() / 50);
        nodeInput.setWidthOfObstacle((obstacles.getFirst().getWidth()) / 75);
        nodeInput.setPlayerYPosition((y - 220) / 100);
        nodeInput.setVelocity((this.currentSpeed - 6) / RELATIVE_MAX_SPEED);

        nodeInput.setState(returnState());
        dataEmitter.emitData(nodeInput);

    }

    private State returnState() {
        if (player.isDucking()) {
            return State.DUCK;
        } else if (player.isJumping() && player.isSmallJumping()) {
            return State.SMALL_JUMP;
        } else if (player.isJumping()) {
            return State.JUMP;
        } else {
            return State.RUN;
        }
    }

    private void drawObstacles() {
        if (obstacles.isEmpty() || (!obstacles.getFirst().isAlive() && obstacles.size() == 1)) {

            addRandomObstacle();
            gamePane.getChildren().add(obstacles.getLast().getView());

        } else if (700 - obstacles.getLast().getView().getTranslateX() > obstacles.getLast().getMinGap()) {
            if (random.nextInt(100) < 1) {

                addRandomObstacle();
                gamePane.getChildren().add(obstacles.getLast().getView());
            }
        }
    }

    private Obstacle addRandomObstacle() {
        int obstacleNumber = random.nextInt(3);
        Obstacle obstacle;
        switch (obstacleNumber) {
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
        obstacles.add(obstacle);
        return obstacle;
    }

    private void setOnKeyPressed() {
        gamePane.setOnKeyPressed((e) -> {
            if ((e.getCode() == KeyCode.SPACE) && (player.isJumping() == false)) {
                player.jump();
            } else if (((e.getCode() == (KeyCode.DOWN)) || (e.getCode() == (KeyCode.S))) && (player.isJumping() == false)) {
                player.duck();
            } else if (((e.getCode() == (KeyCode.DOWN)) || (e.getCode() == (KeyCode.S))) && (player.isJumping() == true)) {
                player.setSmallJumping(true);
            }
        });
        gamePane.setOnKeyReleased((event) -> {
            if (((event.getCode() == KeyCode.DOWN) || (event.getCode() == (KeyCode.S))) && (player.isJumping() == false)) {
                player.notDuck();
            }
        });
    }

    private void loadMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/MenuView.fxml"));
        Pane root = loader.load();
        MainGameStarter.stackPane.getChildren().clear();
        MainGameStarter.stackPane.getChildren().add(root);
    }
}