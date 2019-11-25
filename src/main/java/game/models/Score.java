package game.models;

import javafx.scene.control.Label;

public class Score {
    private int score=0;
    public Score(){
        view.setTranslateY(50);
        view.setTranslateX(600);
    }


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
        score+=(speed);
        view.setText(Integer.toString(score));
    }


}
