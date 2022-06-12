/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.userinterface;

import java.awt.event.*;
import java.util.Random;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;;
import javafx.scene.*;
import javafx.scene.shape.*;
import snake.game.Snake;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.Timer;
import snake.constants.SnakeMovement;

/**
 *
 * @author William-UEM
 */
public class UserInterfaceImpl implements ActionListener,
        EventHandler<KeyEvent> {
    
    private static final int        SCENE_WIDTH = 1200;
    private static final int       SCENE_HEIGHT = 700;
    private static final int          UNIT_SIZE = 25;
    private static final int              DELAY = 50;
    private static final int INITIAL_BODY_PARTS = 6;
    private static final int     MAX_BODY_PARTS = (SCENE_HEIGHT*SCENE_WIDTH)/(UNIT_SIZE*UNIT_SIZE);
    
    private final Group  root;
    private final Stage stage;
//    o i-ésimo elemento destes arrays armazena
//    a posição do i-ésimo segmento da cobra
    Snake snake;
    int score;
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
        //Não encontrei como eu poderia chamar o método 
        //handle da classe UserInterfaceImpl, acabou
        //que ficou esta gambiarra
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (!timer.isRunning()) startGame();
            
            snake.updateMove(e);
        });
    }
    
    private void drawBackground(Group root) {
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.BLACK);

        stage.setTitle("Snake Game");
        stage.setScene(scene);
    }

    private void startGame() {
        random = new Random();
        running = true;
        score = 0;
        
        newFood();
        snake = new Snake(
                INITIAL_BODY_PARTS,
                MAX_BODY_PARTS,
                SnakeMovement.RIGHT
        );
        
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    private void newFood() {
        foodX = random.nextInt(SCENE_WIDTH/UNIT_SIZE);
        foodY = random.nextInt(SCENE_HEIGHT/UNIT_SIZE);
    }
    
    private void checkFood(int[] x, int[] y, int bodyParts) {
        // Se a cabeça está na mesma posição que a comida
        if ((x[0] == foodX) && (y[0] == foodY)) {
            newFood();
            // A nova parte do snake recebe as coordenadas da última parte
            x[bodyParts] = x[bodyParts-1];
            y[bodyParts] = y[bodyParts-1];
            
            snake.setX(x);
            snake.setY(y);
            snake.setBodyParts(++bodyParts);
            score++;
        }
    }
    
    private void checkCollisions(int[] x, int[] y, int bodyParts) {
        int lastHorizontalUnit = SCENE_WIDTH/UNIT_SIZE;
        int   lastVerticalUnit = SCENE_HEIGHT/UNIT_SIZE;
        //Colidiu com as bordas de tela
        if (
                (x[0] >= lastHorizontalUnit) 
                || (y[0] >= lastVerticalUnit)
                || (x[0] < 0)
                || (y[0] < 0)
        ){
            running = false;
        }
        //Colidiu com o próprio corpo
        for(int i = 1; i < bodyParts; i++) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        if (!running) timer.stop();
    }
    
    private void updateView() {
        root.getChildren().clear();
        
        if (!running) drawGameOverScreen();
        
        int[] x = snake.getX();
        int[] y = snake.getY();
        int bodyParts = snake.getBodyParts();
        
        drawFood();
        drawSnake(x, y, bodyParts);
        showScore(root);
//        drawGridLines();
    }

    private void drawSnake(int[] x, int[] y, int bodyParts) {
        for (int i = 0; i < bodyParts; i++) {
            Rectangle bodyPart = new Rectangle(
                    x[i] * UNIT_SIZE,
                    y[i] * UNIT_SIZE,
                    UNIT_SIZE,
                    UNIT_SIZE
            );
            // Pinta a cabeça de GREEN e o resto do corpo de DARKGREEN
            if (i == 0)
                bodyPart.setFill(Color.GREEN);
            else
                bodyPart.setFill(Color.DARKGREEN);
            
            root.getChildren().add(bodyPart);
        }        
    }
    
    private void drawFood() {
        Circle food = new Circle(
                (foodX+0.5)*UNIT_SIZE,
                (foodY+0.5)*UNIT_SIZE,
                UNIT_SIZE/2,
                Color.RED
        );
        
        root.getChildren().add(food);
    }
    
    private void drawGridLines() {
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
    }
    
    private void drawGameOverScreen() {
        Label text = new Label("Game Over");
        text.setTextFill(Color.RED);
        
        Font font = Font.font("Ink Free", FontWeight.BOLD, 40);

        text.setFont(font);
        text.setTranslateX((SCENE_WIDTH/2)-100);
        text.setTranslateY((SCENE_HEIGHT/2)-50);
        
        root.getChildren().add(text);
    }
    
    private void showScore(Group root) {
        Label score = new Label();
        Font font = Font.font("Ink Free", FontWeight.BOLD, 40);
        
        score.setText("Score: "+this.score);
        score.setTextFill(Color.RED);
        score.setFont(font);
        score.setTranslateX((SCENE_WIDTH/2)-100);
        
        root.getChildren().add(score);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(running) {
            int[] x = Snake.getX();
            int[] y = Snake.getY();
            int bodyParts = Snake.getBodyParts();
            
            snake.move();
            checkFood(x, y, bodyParts);
            checkCollisions(x, y, bodyParts);
        }
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateView();
            }
        });
    }
    
    @Override
    public void handle(KeyEvent event) {
        System.out.println("Botao apertado");
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            
        }
    }
}