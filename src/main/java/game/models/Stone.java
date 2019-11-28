package game.models;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class Stone extends GameObject{
    private Canvas canvas;
    public Stone(){
        canvas = new Canvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Random random = new Random();
        int size = random.nextInt(3);
        int y = random.nextInt(3);

    }
}
