package game;

import game.controllers.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainGameStarter extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/GameView.fxml"));
        Pane root = loader.load();
        Scene scene = new Scene(root);
        scene.onKeyPressedProperty().bind(root.onKeyPressedProperty());
        scene.onKeyReleasedProperty().bind(root.onKeyReleasedProperty());
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image(this.getClass().getResource("/drawable/dino0000.png").openStream()));
        primaryStage.setTitle("DinoGame");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
