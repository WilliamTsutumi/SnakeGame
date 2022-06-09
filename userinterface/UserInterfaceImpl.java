/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javax.swing.Timer;
import snake.constants.SnakeMovement;

/**
 *
 * @author William-UEM
 */
public class UserInterfaceImpl implements ActionListener {    
    
    private static final int  SCENE_WIDTH = 600;
    private static final int SCENE_HEIGHT = 600;
    private static final int    UNIT_SIZE = 25;
    private static final int    MAX_UNITS = (SCENE_HEIGHT*SCENE_WIDTH)/UNIT_SIZE;
    private static final int        DELAY = 50;
    
    private final Group  root;
    private final Stage stage;
//    o i-ésimo elemento destes arrays armazena
//    a posição do i-ésimo segmento da cobra
    int[] x = new int[MAX_UNITS];
    int[] y = new int[MAX_UNITS];
    SnakeMovement move;
    int bodyParts = 6;
    int foodEaten;
    int foodX;
    int foodY;
    
    boolean running;
    
    Timer timer;
    Random random;
    KeyListener listener;
    
    public UserInterfaceImpl(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        startGame();
        drawBackground(root);
        stage.show();
    }
    
    private void drawBackground(Group root) {
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.BLACK);
//        Grid lines
//        for(int i = 1;i < (SCENE_WIDTH / UNIT_SIZE); i++) {
//            Rectangle verticalLine = new Rectangle(
//                    i * UNIT_SIZE,
//                    0,
//                    1,
//                    SCENE_HEIGHT
//            );
//            verticalLine.setFill(Color.WHITE);
//            
//            root.getChildren().add(verticalLine);
//        }
//        
//        for(int i = 1;i < (SCENE_HEIGHT / UNIT_SIZE); i++) {
//            Rectangle horizontalLine = new Rectangle(
//                    0,
//                    i * UNIT_SIZE,
//                    SCENE_WIDTH,
//                    1
//            );
//            horizontalLine.setFill(Color.WHITE);
//            
//            root.getChildren().add(horizontalLine);
//        }
        
        stage.setTitle("Snake Game");
        stage.setScene(scene);
    }

    private void startGame() {
        random = new Random();
        newFood();
        move = SnakeMovement.RIGHT;
        running = true;
        
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    private void newFood() {
        foodX = random.nextInt(SCENE_WIDTH/UNIT_SIZE);
        foodY = random.nextInt(SCENE_HEIGHT/UNIT_SIZE);
    }
    
    private void moveSnake() {
        switch(move) {
            case RIGHT:
                x[0]++;
                break;
                
            case LEFT:
                x[0]--;
                break;
                
            case UP:
                y[0]++;
                break;
                
            case DOWN:
                y[0]--;
                break;
        }
        //cada bodypart recebe as coordenadas do próximo bodypart
        //exceto a cebeça, que já foi ajustada
        for (int i = 1; i < bodyParts; i++) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
    }
    
    private void checkFood() {
        if ((x[0] == foodX) && (y[0] == foodY)) {
            newFood();
            //posição da nova parte da cobra é igual à última
            x[bodyParts] = x[bodyParts-1];
            y[bodyParts] = y[bodyParts-1];
            bodyParts++;
        }
    }
    
    private void checkCollisions() {
        int lastHorizontalUnit = SCENE_WIDTH/UNIT_SIZE;
        int   lastVerticalUnit = SCENE_HEIGHT/UNIT_SIZE;
        //colidiu com os limites da tela
        if ((x[0] > lastHorizontalUnit) || (y[0] > lastVerticalUnit)) {
            running = false;
        }
        //colidiu com o próprio corpo
        for(int i = 0; i < bodyParts; i++) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
    }
    
    private void updateView() {
        //snake view
        for (int i = 0; i < bodyParts; i++) {
            Rectangle bodyPart = new Rectangle(
                    x[i] * UNIT_SIZE,
                    y[i] * UNIT_SIZE,
                    UNIT_SIZE,
                    UNIT_SIZE
            );
            bodyPart.setFill(Color.GREEN);
            
            root.getChildren().add(bodyPart);
        }
        //food view
        Circle food = new Circle(
                (foodX+0.5)*UNIT_SIZE,
                (foodY+0.5)*UNIT_SIZE,
                UNIT_SIZE/2,
                Color.RED
        );
        
        root.getChildren().add(food);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(running) {
            moveSnake();
            checkFood();
//            checkCollisions();
        }
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateView();
            }
        });
    }
}
