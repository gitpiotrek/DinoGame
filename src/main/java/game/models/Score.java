package game.models;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.io.InputStream;

public class Score {
    private double score=0;
    public Score(){
        view.setTranslateY(50);
        view.setTranslateX(600);
        InputStream stream = this.getClass().getResourceAsStream("/font/PressStart2P-Regular.ttf");
        this.fontSmall = Font.loadFont(stream, 14.0);
        view.setFont(fontSmall);
    }
    private Font fontSmall;

    private Label view = new Label();

    public Label getView() {
        return view;
    }

    public void setView(Label view) {
        this.view = view;
    }
    public void resetScore(){
        this.score =0;
    }
    public void onUpdate(double speed){
        score+=(speed/36);
        view.setText(Integer.toString((int)score));
    }
}