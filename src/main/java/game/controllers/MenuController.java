package game.controllers;

import game.MainGameStarter;
import game.models.GameState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Pane menuPane;

    @FXML
    private Button playButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button teachButton;

    @FXML
    private Button chooseNeuralNetworkFileButton;

    @FXML
    private Button showResultButton;

    @FXML
    private Label neuralFileLabel;

    @FXML
    private TextField playerNameTextField;

   private FileChooser fileChooser = new FileChooser();

  public static File selectedNeuralFile = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(selectedNeuralFile == null){
    showResultButton.setDisable(true);}
        else {
            neuralFileLabel.setText(selectedNeuralFile.getName());
            showResultButton.setDisable(false);
        }
    }

    @FXML
    void playAction(ActionEvent event) throws IOException {
        MainGameStarter.gameState = GameState.PLAY;
        loadGame();
    }

    @FXML
    void chooseNeuralNetworkFileAction(ActionEvent event) {
       selectedNeuralFile = fileChooser.showOpenDialog(menuPane.getParent().getScene().getWindow());
       if(selectedNeuralFile != null) {
           //validate file
           neuralFileLabel.setText(selectedNeuralFile.getName());
           showResultButton.setDisable(false);
       }
    }

    @FXML
    void teachAction(ActionEvent event) throws IOException {
        try {
            MainGameStarter.playerName = validate(playerNameTextField.getText());
        }catch (IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Player name is required");
            alert.setContentText("Enter player name before start learning your dino !");

            alert.showAndWait();
            return;
        }
        MainGameStarter.gameState = GameState.TRAIN;
        loadGame();
    }

    @FXML
    void showResultAction(ActionEvent event) throws IOException {
    MainGameStarter.gameState = GameState.SHOW_RESULT;
    //NeuralNetworkFileManager neuralNetworkFileManager = new NeuralNetworkFileManager(selectedNeuralFile);
   // loadNeuralNetworkSimulation(new GameController(neuralNetworkFileManager.loadNeuralNetwork()));
        loadGame();
    }

    @FXML
    void exitAction(ActionEvent event) {
        // Platform.exit();
        System.exit(0);
    }

    private void loadGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/GameView.fxml"));
        Pane root = loader.load();
        MainGameStarter.stackPane.getChildren().clear();
        MainGameStarter.stackPane.getChildren().add(root);
        Scene scene = MainGameStarter.stackPane.getScene();
        scene.onKeyPressedProperty().bind(root.onKeyPressedProperty());
        scene.onKeyReleasedProperty().bind(root.onKeyReleasedProperty());
    }
/*
    private void loadNeuralNetworkSimulation(GameController gameController)throws IOException{
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/GameView.fxml"));
        loader.setController(gameController);
        Pane root = loader.load();
        MainGameStarter.stackPane.getChildren().clear();
        MainGameStarter.stackPane.getChildren().add(root);
        Scene scene = MainGameStarter.stackPane.getScene();
        scene.onKeyPressedProperty().bind(root.onKeyPressedProperty());
    }
*/
    private String validate(String text) throws IllegalArgumentException {
        if(text.equals("") || text.isEmpty()){
            throw new IllegalArgumentException();
        }
       text = text.trim();
       text = text.replaceAll("\\.","");
       text = text.replaceAll("\\s","");
        text = text.replaceAll("/","");
       text = text.replaceAll(System.getProperty("path.separator"),"");
       return text;
    }
}
