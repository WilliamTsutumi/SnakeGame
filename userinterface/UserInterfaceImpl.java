/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.userinterface;

import java.util.EventListener;
import java.util.Random;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javax.management.timer.Timer;

/**
 *
 * @author William-UEM
 */
public class UserInterfaceImpl implements EventHandler<KeyEvent> {    
    
    private static final int  SCENE_WIDTH = 600;
    private static final int SCENE_HEIGHT = 600;
    private static final int    UNIT_SIZE = 25;
    private static final int    MAX_UNITS = (SCENE_HEIGHT*SCENE_WIDTH)/UNIT_SIZE;
    private static final int        DELAY = 50;
    
    Group  root = new Group();
    Stage stage = new Stage();
//    o i-ésimo elemento destes arrays armazena
//    a posição do i-ésimo segmento da cobra
    int[] x = new int[MAX_UNITS];
    int[] y = new int[MAX_UNITS];
    int bodyParts = 6;
    int foodEaten;
    int foodX;
    int foodY;
    
    Timer timer;
    Random random;
    
    boolean running;
    
    public UserInterfaceImpl() {
        startGame(root);
        drawBackground(root);
        stage.show();
    }
    
    private void drawBackground(Group root) {
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.BLACK);
        
        for(int i = 1;i < (SCENE_WIDTH / UNIT_SIZE); i++) {
            Rectangle verticalLine = new Rectangle(
                    i * UNIT_SIZE,
                    0,
                    1,
                    SCENE_HEIGHT
            );
            verticalLine.setFill(Color.WHITE);
            
            root.getChildren().add(verticalLine);
        }
        
        for(int i = 1;i < (SCENE_HEIGHT / UNIT_SIZE); i++) {
            Rectangle horizontalLine = new Rectangle(
                    0,
                    i * UNIT_SIZE,
                    SCENE_WIDTH,
                    1
            );
            horizontalLine.setFill(Color.WHITE);
            
            root.getChildren().add(horizontalLine);
        }
        
        stage.setTitle("Snake Game");
        stage.setScene(scene);
    }

    private void startGame(Group root) {
        random = new Random();
        newFood(root);
//        running = true;
//        timer = new Timer(DELAY, this);
    }
    
    private void newFood(Group root) {
        foodX = random.nextInt(SCENE_HEIGHT/UNIT_SIZE);
        foodY = random.nextInt(SCENE_HEIGHT/UNIT_SIZE);
        
        Circle food = new Circle(
                (foodX+0.5)*UNIT_SIZE,
                (foodY+0.5)*UNIT_SIZE,
                UNIT_SIZE/2,
                Color.RED
        );
        
        root.getChildren().add(food);
    }

    @Override
    public void handle(KeyEvent event) {
        
    }
}
