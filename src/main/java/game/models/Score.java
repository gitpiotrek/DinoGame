package game.models;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.io.InputStream;

public class Score {
    private double score = 0;
    private Font fontSmall;

    public Score(){
        view.setTranslateY(50);
        view.setTranslateX(600);
        InputStream stream = this.getClass().getResourceAsStream("/font/PressStart2P-Regular.ttf");
        this.fontSmall = Font.loadFont(stream, 14.0);
        view.setFont(fontSmall);
    }

    private Label view = new Label();

    public Label getView() {
        return view;
    }

    public void onUpdate(double speed){
        score+=(speed/36);
        view.setText(Integer.toString((int)score));
    }

    public String getNumericScore(){
        return Integer.toString((int)score);
    }
}