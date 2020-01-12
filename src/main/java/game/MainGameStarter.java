package game;

import game.models.GameState;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainGameStarter extends Application {

    public static final StackPane stackPane = new StackPane();
    public static GameState gameState;
    public static String playerName = "player";

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/MenuView.fxml"));
        Pane root = loader.load();
        stackPane.getChildren().add(root);
        Scene scene = new Scene(stackPane);
        // scene.onKeyPressedProperty().bind(root.onKeyPressedProperty());
        // scene.onKeyReleasedProperty().bind(root.onKeyReleasedProperty());
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(400.0);
        primaryStage.setMinWidth(700.0);
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        primaryStage.getIcons().add(new Image(this.getClass().getResource("/drawable/dino.png").openStream()));
        primaryStage.setTitle("DinoGame");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
