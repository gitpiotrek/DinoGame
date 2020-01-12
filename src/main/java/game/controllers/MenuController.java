package game.controllers;

import ai.fileManager.FileValidator;
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
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import sun.plugin.dom.exception.WrongDocumentException;

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
    private Label dinoGameLabel;

    @FXML
    private Button showResultButton;

    @FXML
    private Label neuralFileLabel;

    @FXML
    private TextField playerNameTextField;

    private Font font = Font.loadFont(this.getClass().getResourceAsStream("/font/PressStart2P-Regular.ttf"), 16.0);
    private Font smallFont = Font.loadFont(this.getClass().getResourceAsStream("/font/PressStart2P-Regular.ttf"), 10.0);
    private Border showResultActiveBorder = new Border(new BorderStroke(Color.SPRINGGREEN,
            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
    private FileChooser fileChooser = new FileChooser();

    public static File selectedNeuralFile = null;

    BackgroundImage backgroundImage = new BackgroundImage(new Image(MenuController.class.getResourceAsStream("/drawable/dino_background.jpg")
            , 700.0, 400.0, false, true),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dinoGameLabel.setFont(font);
        playButton.setFont(font);
        teachButton.setFont(smallFont);
        chooseNeuralNetworkFileButton.setFont(smallFont);
        showResultButton.setFont(smallFont);
        exitButton.setFont(smallFont);
        menuPane.setBackground(new Background(backgroundImage));

        if (selectedNeuralFile == null) {
            showResultButton.setDisable(true);
        } else {
            neuralFileLabel.setText(selectedNeuralFile.getName());
            showResultButton.setBorder(showResultActiveBorder);
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
        if (selectedNeuralFile != null) {
            try {
                FileValidator.validate(selectedNeuralFile);
            } catch (WrongDocumentException wde) {
                wrongFileAlert(wde.getMessage());
            } catch (Exception e) {
                wrongFileAlert("Wrong file selected");
            }
        }
        if (selectedNeuralFile != null) {
            neuralFileLabel.setText(selectedNeuralFile.getName());
            showResultButton.setBorder(showResultActiveBorder);
            showResultButton.setDisable(false);
        } else {
            neuralFileLabel.setText("No file selected");
            showResultButton.setBorder(null);
            showResultButton.setDisable(true);
        }
    }

    @FXML
    void teachAction(ActionEvent event) throws IOException {
        try {
            MainGameStarter.playerName = validate(playerNameTextField.getText());
        } catch (IllegalArgumentException e) {
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

    private String validate(String text) throws IllegalArgumentException {
        if (text.equals("") || text.isEmpty()) {
            throw new IllegalArgumentException();
        }
        text = text.trim();
        text = text.replaceAll("\\.", "");
        text = text.replaceAll("\\s", "");
        text = text.replaceAll("/", "");
        text = text.replaceAll(System.getProperty("path.separator"), "");
        return text;
    }

    private void wrongFileAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Ooops, there was an error !");
        alert.setContentText(msg);
        alert.showAndWait();
        selectedNeuralFile = null;
    }
}
